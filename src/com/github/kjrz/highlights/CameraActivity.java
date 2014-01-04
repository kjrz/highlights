package com.github.kjrz.highlights;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.github.kjrz.highlights.cam.CameraPreview;

public class CameraActivity extends Activity {

	private Camera camera;
	private CameraPreview cameraPreview;

	private int startStop = R.string.shooter_one;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// Create an instance of Camera
		initializeCamera();

		// Create our Preview view and set it as the content of our activity.
		cameraPreview = new CameraPreview(this, camera);
		FrameLayout cameraPreviewFrame = (FrameLayout) findViewById(R.id.camera_preview);
		cameraPreviewFrame.addView(cameraPreview);

		setupActionBar();
	}

	private void initializeCamera() {
		camera = getCameraInstance();
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem score = menu.findItem(R.id.capture);
		score.setTitle(startStop);
		return true;
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cam, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public void onPause() {
		super.onPause();

		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	@Override public void onResume() {
		super.onResume();

		if (camera == null) {
			initializeCamera();
		}
	}

	public void startStopClicked(MenuItem item) {
		if (startStop == R.string.shooter_one) {
			startStop = R.string.shooter_two;
		} else {
			startStop = R.string.shooter_one;
		}

		invalidateOptionsMenu();
	}
}

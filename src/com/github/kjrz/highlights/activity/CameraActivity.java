package com.github.kjrz.highlights.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.kjrz.highlights.R;
import com.github.kjrz.highlights.listener.CameraFragmentListener;

public class CameraActivity extends Activity implements CameraFragmentListener {
	public static final String TAG = "highlights/CameraActivity";

	// private static final int PICTURE_QUALITY = 90;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_camera);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem score = menu.findItem(R.id.capture);
		score.setTitle(R.string.shooter);
		return true;
	}

	@Override
	public void onCameraError() {
		Toast.makeText(this, getString(R.string.toast_error_camera_preview),
				Toast.LENGTH_SHORT).show();

		finish();
	}

	public void shooterClicked(MenuItem item) {
		Toast.makeText(this, R.string.shot_taken, Toast.LENGTH_SHORT).show();
	}

	public void takePicture(View view) {
		// view.setEnabled(false);
		//
		// CameraFragment fragment = (CameraFragment) getFragmentManager()
		// .findFragmentById(R.id.camera_fragment);
		//
		// fragment.takePicture();
	}

	@Override
	public void onPictureTaken(Bitmap bitmap) {
		// File mediaStorageDir = new File(
		// Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
		// getString(R.string.app_name));
		//
		// if (!mediaStorageDir.exists()) {
		// if (!mediaStorageDir.mkdirs()) {
		// showSavingPictureErrorToast();
		// return;
		// }
		// }
		//
		// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		// .format(new Date());
		// File mediaFile = new File(mediaStorageDir.getPath() + File.separator
		// + "MUSTACHE_" + timeStamp + ".jpg");
		//
		// try {
		// FileOutputStream stream = new FileOutputStream(mediaFile);
		// bitmap.compress(CompressFormat.JPEG, PICTURE_QUALITY, stream);
		// } catch (IOException exception) {
		// showSavingPictureErrorToast();
		//
		// Log.w(TAG, "IOException during saving bitmap", exception);
		// return;
		// }
		//
		// MediaScannerConnection.scanFile(this,
		// new String[] { mediaFile.toString() },
		// new String[] { "image/jpeg" }, null);
		//
		// Intent intent = new Intent(this, PhotoActivity.class);
		// intent.setData(Uri.fromFile(mediaFile));
		// startActivity(intent);
		//
		// finish();
	}

	// private void showSavingPictureErrorToast() {
	// Toast.makeText(this, getText(R.string.toast_error_save_picture),
	// Toast.LENGTH_SHORT).show();
	// }
}

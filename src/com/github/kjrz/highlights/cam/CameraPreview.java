package com.github.kjrz.highlights.cam;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = "com.github.kjrz.highlights.CameraPreview";

	private final SurfaceHolder holder;
	private final Camera camera;

	public CameraPreview(Context context, Camera cameraParam) {
		super(context);
		camera = cameraParam;

		holder = getHolder();
		holder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		// mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. releasing the Camera preview taken care of in your activity.
	}

	@Override public void surfaceChanged(SurfaceHolder holderParam, int format,
			int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (holder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			camera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here
		// camera.setDisplayOrientation(-90);
		// Size size = camera.getParameters().getSupportedPictureSizes().get(6);
		// camera.getParameters().setPreviewSize(50, 50);
		// List<Size> sizes = camera.getParameters().getSupportedPictureSizes();

		// start preview with new settings
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}
}
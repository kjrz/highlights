package com.github.kjrz.highlights.camera;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.github.kjrz.highlights.listener.CameraFragmentListener;
import com.github.kjrz.highlights.listener.CameraOrientationListener;

/**
 * Fragment for displaying the camera preview.
 * 
 * @author Sebastian Kaspari <sebastian@androidzeitgeist.com>
 */
public class CameraFragment extends Fragment implements SurfaceHolder.Callback,
		Camera.PictureCallback {
	public static final String TAG = "Mustache/CameraFragment";

	private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
	private static final int PREVIEW_SIZE_MAX_WIDTH = 640;

	private int cameraId;
	private Camera camera;
	private SurfaceHolder surfaceHolder;
	private CameraFragmentListener listener;
	private int displayOrientation;
	private int layoutOrientation;

	private CameraOrientationListener orientationListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof CameraFragmentListener)) {
			throw new IllegalArgumentException(
					"Activity has to implement CameraFragmentListener interface");
		}

		listener = (CameraFragmentListener) activity;

		orientationListener = new CameraOrientationListener(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		CameraPreview previewView = new CameraPreview(getActivity());

		previewView.getHolder().addCallback(this);

		return previewView;
	}

	@Override
	public void onResume() {
		super.onResume();

		orientationListener.enable();

		try {
			camera = Camera.open(cameraId);
		} catch (Exception exception) {
			Log.e(TAG, "Can't open camera with id " + cameraId, exception);

			listener.onCameraError();
			return;
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		orientationListener.disable();

		stopCameraPreview();
		camera.release();
	}

	private synchronized void startCameraPreview() {
		determineDisplayOrientation();
		setupCamera();

		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception exception) {
			Log.e(TAG, "Can't start camera preview due to Exception", exception);

			listener.onCameraError();
		}
	}

	private synchronized void stopCameraPreview() {
		try {
			camera.stopPreview();
		} catch (Exception exception) {
			Log.i(TAG, "Exception during stopping camera preview");
		}
	}

	public void determineDisplayOrientation() {
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(cameraId, cameraInfo);

		int rotation = getActivity().getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;

		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;

			case Surface.ROTATION_90:
				degrees = 90;
				break;

			case Surface.ROTATION_180:
				degrees = 180;
				break;

			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int displayOrientation;

		if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			displayOrientation = (cameraInfo.orientation + degrees) % 360;
			displayOrientation = (360 - displayOrientation) % 360;
		} else {
			displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
		}

		this.displayOrientation = displayOrientation;
		this.layoutOrientation = degrees;

		camera.setDisplayOrientation(displayOrientation);
	}

	public void setupCamera() {
		Camera.Parameters parameters = camera.getParameters();

		Size bestPreviewSize = determineBestPreviewSize(parameters);
		Size bestPictureSize = determineBestPictureSize(parameters);

		parameters
				.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
		parameters
				.setPictureSize(bestPictureSize.width, bestPictureSize.height);

		camera.setParameters(parameters);
	}

	private Size determineBestPreviewSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPreviewSizes();

		return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
	}

	private Size determineBestPictureSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPictureSizes();

		return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
	}

	protected Size determineBestSize(List<Size> sizes, int widthThreshold) {
		Size bestSize = null;

		for (Size currentSize : sizes) {
			boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 3);
			boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
			boolean isInBounds = currentSize.width <= PICTURE_SIZE_MAX_WIDTH;

			if (isDesiredRatio && isInBounds && isBetterSize) {
				bestSize = currentSize;
			}
		}

		if (bestSize == null) {
			listener.onCameraError();

			return sizes.get(0);
		}

		return bestSize;
	}

	public void takePicture() {
		orientationListener.rememberOrientation();

		camera.takePicture(null, null, this);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

		int rotation = (displayOrientation
				+ orientationListener.getRememberedOrientation() + layoutOrientation) % 360;

		if (rotation != 0) {
			Bitmap oldBitmap = bitmap;

			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);

			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, false);

			oldBitmap.recycle();
		}

		listener.onPictureTaken(bitmap);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.surfaceHolder = holder;

		startCameraPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// The interface forces us to have this method but we don't need it
		// up to now.
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// We don't need to handle this case as the fragment takes care of
		// releasing the camera when needed.
	}
}

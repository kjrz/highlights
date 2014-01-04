package com.github.kjrz.highlights.camera;

import android.graphics.Bitmap;

public interface CameraFragmentListener {

	public void onCameraError();

	public void onPictureTaken(Bitmap bitmap);
}

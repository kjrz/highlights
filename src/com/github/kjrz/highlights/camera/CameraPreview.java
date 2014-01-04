package com.github.kjrz.highlights.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView {
	private static final double ASPECT_RATIO = 3.0 / 4.0;

	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CameraPreview(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);

		if (width > height * ASPECT_RATIO) {
			width = (int) (height * ASPECT_RATIO + .5);
		} else {
			height = (int) (width / ASPECT_RATIO + .5);
		}

		setMeasuredDimension(width, height);
	}
}
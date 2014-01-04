package com.github.kjrz.highlights.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.github.kjrz.highlights.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	public void startCam(View view) {
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}

	public void startSound(View view) {
		// TODO

		Toast.makeText(this, R.string.enter_sound, Toast.LENGTH_SHORT).show();
	}
}

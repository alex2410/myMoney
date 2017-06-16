package com.example.activities;

import com.example.R;

import android.app.Activity;
import android.os.Bundle;

public class BlankActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
	}

	@Override
	protected Class<? extends Activity> getPrevious() {
		return null;
	}

}

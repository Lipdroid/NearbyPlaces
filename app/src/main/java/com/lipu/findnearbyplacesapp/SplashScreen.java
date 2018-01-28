package com.lipu.findnearbyplacesapp;

import com.lipu.findnearbyplacesapp.Permissions.RequestUserPermission;
import com.lipu.findnearbyplacesapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.splash);
		if( getIntent().getBooleanExtra("Exit me", false)){
	        finish();
	        return; // add this to prevent from doing unnecessary stuffs
	    }
		Thread t = new Thread() {

			public void run() {

				try {

					sleep(2000);
					finish();
					Intent cv = new Intent(SplashScreen.this,
							TypeList.class/* otherclass */);
					startActivity(cv);
				}

				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();

	}



}

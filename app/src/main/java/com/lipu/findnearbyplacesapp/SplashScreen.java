package com.lipu.findnearbyplacesapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.lipu.findnearbyplacesapp.utils.CorrectSizeUtil;

public class SplashScreen extends Activity {
    CorrectSizeUtil mCorrectSize = null;
    ImageView marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.splash);

        marker = (ImageView) findViewById(R.id.marker);
        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, .02f);
        mAnimation.setDuration(500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        marker.setAnimation(mAnimation);

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }
        Thread t = new Thread() {

            public void run() {

                try {

                    sleep(3000);
                    finish();
                    Intent cv = new Intent(SplashScreen.this,
                            TypeList.class/* otherclass */);
                    startActivity(cv);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();

    }

}

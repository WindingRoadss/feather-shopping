package com.hwc.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hwc.main.R;

public class Splash_MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__main);
        ImageView image = (ImageView) findViewById(R.id.logobackground);
        Animation alphaAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_first);
        image.startAnimation(alphaAnim);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Splash_MainActivity.this, Splash_BlackActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3150);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

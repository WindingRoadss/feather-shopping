package com.hwc.nfc;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hwc.main.R;

public class NFCSplash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcsplash);
        ImageView img_taghand = (ImageView) findViewById(R.id.img_nfcfont);
        Animation alphaAnim = AnimationUtils.loadAnimation(this, R.anim.my_blinking_drawable);
        img_taghand.startAnimation(alphaAnim);
    }
}

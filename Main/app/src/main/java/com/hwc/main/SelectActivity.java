package com.hwc.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hwc.cart.CartActivity;
import com.hwc.nfc.NFCActivity;
import com.hwc.paid.PaidActivity;
import com.hwc.tagging.TaggingActivity;

public class SelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Button btnNFCInfo = (Button) findViewById(R.id.btnNFCInfo);
        btnNFCInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SelectActivity.this, NFCActivity.class);
                startActivity(intent);
            }
        });

        Button btnCartInfo = (Button) findViewById(R.id.btnCart);
        btnCartInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        Button btnTaggingProduct = (Button) findViewById(R.id.btnTagging);
        btnTaggingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TaggingActivity.class);
                startActivity(intent);
            }
        });

        Button btnPaying = (Button) findViewById(R.id.btnPaid);
        btnPaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PaidActivity.class);
                startActivity(intent);
            }
        });

    }
}

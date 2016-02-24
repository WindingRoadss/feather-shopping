package com.hwc.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hwc.cart.CartActivity;
import com.hwc.paid.PaidActivity;
import com.hwc.shared.LoginSession;
import com.hwc.tagging.TaggingSplash;

import java.util.HashMap;

public class SelectActivity_customer extends Activity {
    public static ProgressDialog progDialog;
    private TextView tvUserName;
    private Button btnLogout;
    private LoginSession loginSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
/*
        Button btnNFCInfo = (Button) findViewById(R.id.btnNFCInfo);
        btnNFCInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SelectActivity_customer.this, NFCSplash.class);
                //Intent intent = new Intent(SelectActivity.this, NFCActivity.class);
                startActivity(intent);
            }
        });*/

        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> infoListFormPref = loginSession.getPreferencesResultHashMap();

        String userName = infoListFormPref.get("name");

        tvUserName = (TextView)findViewById(R.id.tvUserName);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        tvUserName.setText(userName);

        Button btnTaggingProduct = (Button) findViewById(R.id.btnTagging);
        btnTaggingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TaggingSplash.class);
                //Intent intent = new Intent(getBaseContext(), TaggingActivity.class);
                startActivity(intent);
            }
        });

        Button btnCartInfo = (Button) findViewById(R.id.btnCart);
        btnCartInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progDialog = new ProgressDialog(SelectActivity_customer.this);
                progDialog.setMessage("로딩중입니다......");
                progDialog.show();
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                startActivity(intent);
            }
        });


        Button btnPaying = (Button) findViewById(R.id.btnPaid);
        btnPaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progDialog = new ProgressDialog(SelectActivity_customer.this);
                progDialog.setMessage("로딩중입니다......");
                progDialog.show();
                Intent intent = new Intent(getBaseContext(), PaidActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progDialog = new ProgressDialog(SelectActivity.this);
                //progDialog.setMessage("로그아웃 중입니다......");
                //progDialog.show();
                loginSession.clearPreferences();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

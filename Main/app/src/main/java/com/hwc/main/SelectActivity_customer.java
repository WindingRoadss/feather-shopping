package com.hwc.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hwc.cart.CartActivity;
import com.hwc.paid.PaidActivity;
import com.hwc.shared.LoginSession;
import com.hwc.tagging.TaggingSplash;

import java.util.HashMap;

public class SelectActivity_customer extends Activity {
    public static ProgressDialog progDialog2;
    private TextView tvUserName;
    private Button btnLogout;
    private LoginSession loginSession;
    public static boolean flag2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);

        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> infoListFormPref = loginSession.getPreferencesResultHashMap();

        String userName = infoListFormPref.get("name");

        tvUserName = (TextView) findViewById(R.id.tvUserName);
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
                flag2 = true;
                progDialog2 = new ProgressDialog(SelectActivity_customer.this);
                progDialog2.setMessage("로딩중입니다......");
                progDialog2.show();
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                startActivity(intent);
            }
        });


        Button btnPaying = (Button) findViewById(R.id.btnPaid);
        btnPaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag2 = true;
                progDialog2 = new ProgressDialog(SelectActivity_customer.this);
                progDialog2.setMessage("로딩중입니다......");
                progDialog2.show();
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
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(SelectActivity_customer.this);
                alertDlg.setIcon(R.mipmap.ic_launcher);
                alertDlg.setMessage("로그아웃 하시겠습니까?");
                alertDlg.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        });
                alertDlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                loginSession.clearPreferences();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog alert = alertDlg.create();
                alert.setTitle("깃털쇼핑_1조");
                alert.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
                alertDlg.setIcon(R.mipmap.ic_launcher);
                alertDlg.setMessage("종료하시겠습니까?");
                alertDlg.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        });
                alertDlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                System.exit(0);
                            }
                        });
                AlertDialog alert = alertDlg.create();
                alert.setTitle("깃털쇼핑_1조");
                alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}

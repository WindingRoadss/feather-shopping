package com.hwc.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public EditText et_id;
    public EditText et_password;
    public Button bt_enter;
    public Button bt_karttemp;
    private String id, password;
    private ArrayList<String> arrayMemberIdName; //회원의 ID와 이름 저장
    private boolean mChecked = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectDB.setActivity(this); // 네트워크 연결확인 하기 위한
        ConnectDB.deleteAllActList(); // 남아있는 activity 삭제하고 시작

        bt_enter = (Button) findViewById(R.id.bt_enter);
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadLogin t_login = new ThreadLogin();
                if (mChecked == true) {
                    t_login.interrupt();
                    Log.d("HWC", "스레드 죽임");
                }
                Dialog mProgress = new Dialog(MainActivity.this, R.style.MyDialog);
                mProgress.setCancelable(true);
                mProgress.addContentView(new ProgressBar(MainActivity.this),
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                mProgress.show();
                et_id = (EditText) findViewById(R.id.et_id);
                id = String.valueOf(et_id.getText());
                et_password = (EditText) findViewById(R.id.et_password);
                id = String.valueOf(et_id.getText());
                if (ConnectDB.isNetworkAvailable()) {

                    t_login.start();
                    mProgress.dismiss();
                } else
                    Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        bt_karttemp = (Button) findViewById(R.id.bt_karttemp);
        bt_karttemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    class ThreadLogin extends Thread {
        @Override
        public void run() {
            et_id = (EditText) findViewById(R.id.et_id);
            id = String.valueOf(et_id.getText());
            et_password = (EditText) findViewById(R.id.et_password);
            password = String.valueOf(et_password.getText());
            try {
                arrayMemberIdName = ConnectDB.login(id, password);
                Log.d("set ID Test", "try 전0" + arrayMemberIdName.get(0));
                Log.d("set ID Test", "try 전1");
                if (arrayMemberIdName.get(0) == "OK") { //login 성공
                    ConnectDB.setId(id);
                    ConnectDB.setName(arrayMemberIdName.get(1));
                    ConnectDB.setEmail(arrayMemberIdName.get(2));
                    Intent intent = new Intent(getBaseContext(), SelectActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    failedLogin();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mChecked = true;
        }
    }

    public void failedLogin() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "로그인에 실패하였습니다", Toast.LENGTH_LONG).show();
            }
        }, 0);
    }
}
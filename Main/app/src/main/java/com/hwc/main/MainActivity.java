package com.hwc.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hwc.dao.common.CommonDao;
import com.hwc.dao.login.LoginDao;
import com.hwc.shared.LoginSession;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends Activity {
    public EditText et_id;
    public EditText et_password;
    public Button bt_enter;
    public Button bt_karttemp;
    private String id, password;
    private HashMap<String, String>[] hashMapLoginResult; //회원의 ID와 이름 저장
    private boolean mChecked = false;
    public ProgressDialog progDialog;

    // LoginSession 유지를 위한 변수
    private LoginSession loginSession;
    private HashMap<String, String> infoList = new HashMap<String, String>(); // 사용자 정보 리스트
    private HashMap<String, String> infoListFormPref; // 사용자 정보 Preference에서 받아온 결과

    // Database Access Object
    private LoginDao loginDao;
    private CommonDao commonDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commonDao = new CommonDao();
        commonDao.setCurrentActivity(this);

        loginDao = new LoginDao();
        loginSession = new LoginSession(getApplicationContext());
        loginSession.clearPreferences(); // 자동 로그인 취소

        et_id = (EditText) findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);

        // id, password 초기화
        et_id.setText("");
        et_password.setText("");

        // SharedPreference에 저장된 LoginSession 정보 받아온다
        infoListFormPref = loginSession.getPreferencesResultHashMap();

        if (infoListFormPref.get("id").length() != 0) { // 저장된 id가 있다면
            if(infoListFormPref.get("isAdmin").equals("1")) { // 관리자인 경우
                Intent intent = new Intent(getBaseContext(), SelectActivity.class);
                startActivity(intent);
                finish();
            }
            else { // 사용자인 경우
                Intent intent = new Intent(getBaseContext(), SelectActivity_customer.class);
                startActivity(intent);
                finish();
            }
        }

        // 로그인 버튼
        bt_enter = (Button) findViewById(R.id.bt_enter);
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadLogin t_login = new ThreadLogin();
                if (mChecked == true) {
                    t_login.interrupt();
                }
                Dialog mProgress = new Dialog(MainActivity.this, R.style.MyDialog);
                mProgress.setCancelable(true);
                mProgress.addContentView(new ProgressBar(MainActivity.this),
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                mProgress.show();
                id = String.valueOf(et_id.getText());
                id = String.valueOf(et_id.getText());

                if (commonDao.isNetworkAvailable()) {
                    t_login.start();
                    mProgress.dismiss();
                } else
                    Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 로그인 기능 수행 스레드
    class ThreadLogin extends Thread {
        @Override
        public void run() {
            progressDialog();
            et_id = (EditText) findViewById(R.id.et_id);
            id = String.valueOf(et_id.getText());
            et_password = (EditText) findViewById(R.id.et_password);
            password = String.valueOf(et_password.getText());
            try {
                hashMapLoginResult = loginDao.selectLoginUserInfo(id, password);
                if(hashMapLoginResult != null) {
                    if (hashMapLoginResult[0].get("status") == "OK") { //login 성공
                        // SharedPreference에 Data put
                        infoList.put("id", id);
                        infoList.put("isAdmin", hashMapLoginResult[0].get("admin"));
                        infoList.put("name", hashMapLoginResult[0].get("name"));
                        if(hashMapLoginResult[0].get("brand") != null)
                            infoList.put("brand", hashMapLoginResult[0].get("brand"));

                        loginSession = new LoginSession(getApplicationContext(), infoList);
                        infoListFormPref = loginSession.getPreferencesResultHashMap();

                        if (hashMapLoginResult[0].get("admin").equals("1")) { // 관리자인 경우
                            Intent intent = new Intent(getBaseContext(), SelectActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getBaseContext(), SelectActivity_customer.class);
                            startActivity(intent);
                            finish();
                        }
                        progDialog.dismiss();
                    } else { // 에러 메시지 출력
                        failedLogin(hashMapLoginResult[0].get("message"));
                        progDialog.dismiss();
                    }
                }
                else { // 로그인 실패 정보 메시지 출력
                    failedLogin("로그인 정보를 다시 확인해주세요");
                    progDialog.dismiss();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mChecked = true;
        }
    }

    // 다이얼로그 시작
    public void progressDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progDialog = new ProgressDialog(MainActivity.this);
                progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDialog.setMessage("로딩중입니다......");
                progDialog.show();
            }
        }, 0);
    }

    // 실패한 로그인인 경우 수행
    public void failedLogin(final String message) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(getBaseContext(), "로그인에 실패하였습니다", Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        }, 0);
    }

    // Progress Dialog onKeyDown 정의
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



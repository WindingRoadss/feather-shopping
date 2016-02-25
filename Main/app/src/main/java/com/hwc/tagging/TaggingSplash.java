package com.hwc.tagging;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import com.hwc.dao.nfc.NFCDao;
import com.hwc.main.R;
import com.hwc.nfc.NFCActivity;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;

public class TaggingSplash extends Activity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    // 입력할 url, aar
    private String url = "url";
    private String aar = "aar";

    // used 정보와 tagid 정보를 담을 bundle
    private String strTagId;
    private boolean boolIsUsed;

    private ThreadSelectIsUsed threadSelectIsUsed;
    private boolean checkIsUsed = false;

    private NFCDao nfcDao;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taggingsplash);
        ImageView img_taghand = (ImageView) findViewById(R.id.img_taghand);
        Animation alphaAnim = AnimationUtils.loadAnimation(this, R.anim.my_blinking_drawable);
        img_taghand.startAnimation(alphaAnim);

        /* NFC 연결 부분 코드 시작 지점*/
        nfcDao = new NFCDao();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent = new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        /* NFC 연결 부분 코드 종료 지점*/
    }

    /* NFC 연결 부분 코드 시작 지점*/

    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
        super.onResume();
    }

    // NFC 태그 스캔시 호출되는 메소드
    @Override
    protected void onNewIntent(Intent intent) {

        if (intent != null) {
            processTag(intent); // processTag 메소드 호출
        }
        super.onNewIntent(intent);
    }

    // onNewIntent 메소드 수행 후 호출되는 메소드
    private void processTag(Intent intent) {

        // 감지된 태그를 가리키는 객체
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // 감지된 태그 Read
        readTag(detectedTag);

    }

    // 감지된 태그에 NdefMessage를 쓰는 메소드
    public boolean readTag(Tag tag) {

        bundle = new Bundle();
        Intent intent = new Intent(getBaseContext(), TaggingActivity.class);

        try {

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) { // 새로운 NFC chip일 때

                byte[] tagId = tag.getId();

                strTagId = byteArrayToHex(tagId);
                Toast.makeText(getApplicationContext(), "포맷이 필요합니다", Toast.LENGTH_SHORT).show();

                return true;

            }

            // 어떤 data가 들어있으면
            else { // ndef != null

                ndef.connect();

                byte[] tagId = tag.getId();

                strTagId = byteArrayToHex(tagId); // tvTagId 세팅

                threadSelectIsUsed = new ThreadSelectIsUsed();

                threadSelectIsUsed.start();
                threadSelectIsUsed.join();

                putDataIntoBundle(intent);
                startActivity(intent);

                return true;
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }
    }


    private String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    private void putDataIntoBundle(Intent intent) {
        bundle.putString("tagId", strTagId);
        bundle.putBoolean("boolIsUsed", boolIsUsed);
        intent.putExtras(bundle);
    }

    class ThreadSelectIsUsed extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = strTagId; // tagId 가져온다

                final HashMap<String, String>[] result = nfcDao.selectIsUsed(tagId);

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            //printToastInThread("ThreadSelectIsUsed Success");
                            if(hashMap.get("used").equals("1"))
                                boolIsUsed = true;
                            else
                                boolIsUsed = false;
                        } else {
                            printToastInThread("ThreadSelectIsUsed Fail");
                        }
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkIsUsed = true;
        }
    }

    public void printToastInThread(final String message) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        }, 0);
    }
    /* NFC 연결 부분 코드 종료 지점*/

}

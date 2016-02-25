package com.hwc.nfc;

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

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;

public class NFCSplash extends Activity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    // 입력할 url, aar
    private String url = "url";
    private String aar = "aar";

    // used 정보와 tagid 정보를 담을 bundle
    private Bundle bundle;
    private String strTagId;
    private boolean boolIsUsed;
    private boolean boolIsEmptyChip;

    private ThreadSelectIsUsed threadSelectIsUsed;
    private boolean checkIsUsed = false;

    private NFCDao nfcDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcsplash);
        ImageView img_taghand = (ImageView) findViewById(R.id.img_nfcfont);
        Animation alphaAnim = AnimationUtils.loadAnimation(this, R.anim.my_blinking_drawable);
        img_taghand.startAnimation(alphaAnim);

        /* NFC 연결 부분 코드 시작 지점*/
        nfcDao = new NFCDao();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent = new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

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
            try {
                processTag(intent); // processTag 메소드 호출
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onNewIntent(intent);

    }

    // onNewIntent 메소드 수행 후 호출되는 메소드
    private void processTag(Intent intent) throws InterruptedException {

        // EditText에 입력된 값이 있는지 없는지 알기 위한 변수
        //String s_url = url.getText().toString();
        //String s_aar = aar.getText().toString();
        String s_url = url;
        String s_aar = aar;

        // 감지된 태그를 가리키는 객체
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        // 빈 칸이 있다면 태그에 쓰지 않음
        if ( (s_url.equals("http://") || s_url.equals("") || s_aar.equals(""))  ) {
            Toast.makeText(getApplicationContext(), "URL과 AAR을 입력하세요!", Toast.LENGTH_SHORT).show();
        }

        // 입력받은 값을 감지된 태그에 씀
        else {
            // NdefMessage에 담기 위한 NdefRecord 만들기
            NdefRecord record[] = new NdefRecord[2];

            // 0은 URL
            // s는 textview에 입력된 문자열이 들어간다.
            record[0] = createTextRecord(s_url, Locale.KOREAN, true);

            // 1은 AAR
            // textview에 어플리케이션 이름이 들어간 내용이 NdefRecord로 들어간다.
            // record[1] = NdefRecord.createApplicationRecord(((TextView) findViewById(R.id.writeAAR)).getText().toString());
            record[1] = NdefRecord.createApplicationRecord(aar);

            // 만들어진 NdefRecord를 NdefMessage로 포장한다.
            NdefMessage message = new NdefMessage(record);

            // 감지된 태그에 NdefMessage를 쓴다.
            writeTag(message, detectedTag);
        }
    }

    // 감지된 태그에 NdefMessage를 쓰는 메소드
    public boolean writeTag(NdefMessage message, Tag tag) throws InterruptedException {

        // 메시지의 byte크기를 얻어옴
        int size = message.toByteArray().length;

        try {

            Ndef ndef = Ndef.get(tag);

            // NFCActivity에 bundle과 인텐트로 값 넘겨준다
            bundle = new Bundle();
            Intent intent = new Intent(getBaseContext(), NFCActivity.class);

            if (ndef == null) { // 새로운 NFC chip일 때

                // formatiing executed
                NdefFormatable formatable = NdefFormatable.get(tag);
                if (formatable != null) {
                    try {
                        formatable.connect();
                        formatable.format(message);
                        Toast.makeText(getApplicationContext(),
                                "태그가 성공적으로 정렬되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }

                ndef.connect(); // ndef 연결

                // 쓰기가 불가능하면
//                if (!ndef.isWritable()) {
//                    Toast.makeText(getApplicationContext(), "Error: tag not writable",
//                            Toast.LENGTH_SHORT).show();
//                    return false;
//                }

                // Tag에 들어간 Ndef메시지의 크기가 허용된 최대 크기보다 크면
//                if (ndef.getMaxSize() < size) {
//                    Toast.makeText(getApplicationContext(),
//                            "Error: tag too small",
//                            Toast.LENGTH_SHORT).show();
//                    return false;
//                }

                //NdefMessage를 얻어온 Tag에 입력
                //ndef.writeNdefMessage(message);

                //Toast.makeText(getApplicationContext(), "쓰기 성공!", Toast.LENGTH_SHORT).show();

                byte[] tagId = tag.getId();
                // 이 부분 주석 안하면 tag read시 에러남
                Toast.makeText(getApplicationContext(), "사용되지 않은 NFC 태그입니다.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), byteArrayToHex(tagId), Toast.LENGTH_SHORT).show();
                //tvTagId.setText(byteArrayToHex(tagId)); // tvTagId 세팅
//                Toast.makeText(getApplicationContext(), tvTagId.getText().toString(), Toast.LENGTH_SHORT).show();


                //threadTest(); // test 중

                strTagId = byteArrayToHex(tagId);
                boolIsUsed = false;
                boolIsEmptyChip = true;

                putDataIntoBundle(intent);
                startActivity(intent);

//                execForUnusedTagThread();


                // 읽기 전용 checkbox 체크시
//                if(readOnlyIsChecked) {
//                    ndef.makeReadOnly();
//                }
                // 포맷 chechbox 체크시
//                if(formatIsChecked) {
//                    // 감지된 태그를 포맷
//                    FormatNFC(message,tag);
//                }

                return true;

            }

            // 어떤 data가 들어있으면
            else { // ndef != null

                ndef.connect();

                //NdefMessage를 얻어온 Tag에 입력
                //ndef.writeNdefMessage(message);

                byte[] tagId = tag.getId();
                // 이 부분 주석 안하면 tag read시 에러남
                //Toast.makeText(getApplicationContext(), byteArrayToHex(tagId), Toast.LENGTH_SHORT).show();

                strTagId = byteArrayToHex(tagId);

                //tvTagId.setText(byteArrayToHex(tagId)); // tvTagId 세팅
//                Toast.makeText(getApplicationContext(), tvTagId.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "쓰기 성공!", Toast.LENGTH_SHORT).show();

                // 최초일 때만 InsertTag

                //threadTest(); // test 중

                threadSelectIsUsed = new ThreadSelectIsUsed();

                threadSelectIsUsed.start();
                threadSelectIsUsed.join();

                // 브랜드 가져온다
                if(boolIsUsed == true) { // used가 1이면
                    Toast.makeText(getApplicationContext(), "사용된 NFC 태그입니다.", Toast.LENGTH_SHORT).show();
                    boolIsUsed = true;
                    boolIsEmptyChip = false;
                    putDataIntoBundle(intent);
                    startActivity(intent);
                    //execForUsedTagThread();
                }
                else { // used가 0이면
                    Toast.makeText(getApplicationContext(), "사용되지 않은 NFC 태그입니다.", Toast.LENGTH_SHORT).show();
                    boolIsUsed = false;
                    boolIsEmptyChip = false;
                    putDataIntoBundle(intent);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    //execForUnusedTagThread();
                }
                //execInitThread();

                // 읽기 전용 checkbox 체크시
//                if(readOnlyIsChecked) {
//                    ndef.makeReadOnly();
//                }
                // 포맷 chechbox 체크시
//                if(formatIsChecked) {
//                    // 감지된 태그를 포맷
//                    FormatNFC(message,tag);
//                }

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

    // 입력받은 URL을 NdefRecord로 만드는 메서드
    private NdefRecord createTextRecord(String text, Locale locale, boolean encodeInUtf8) {

        final byte[] langBytes = locale.getLanguage().getBytes(Charsets.US_ASCII);
        final Charset utfEncoding = encodeInUtf8 ? Charsets.UTF_8 : Charset.forName("UTF-16");
        final byte[] textBytes = text.getBytes(utfEncoding);
        final int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        final char status = (char) (utfBit + langBytes.length);
        final byte[] data = Bytes.concat(new byte[]{(byte) status}, langBytes, textBytes);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);

    }

    private void putDataIntoBundle(Intent intent) {
        bundle.putString("tagId", strTagId);
        bundle.putBoolean("boolIsUsed", boolIsUsed);
        bundle.putBoolean("boolIsEmptyChip", boolIsEmptyChip);
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

                //printToastInThread("ThreadSelectIsUsed In");

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

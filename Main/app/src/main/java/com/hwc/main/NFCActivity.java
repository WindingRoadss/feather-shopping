package com.hwc.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import com.hwc.dao.nfc.NFCDao;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class NFCActivity extends Activity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    //EditText url;           // url 입력 받는 부분
    //EditText aar;           // AAR 입력 받는 부분
    /* branch test */
    private String url;
    private String aar;
    private String selectedBrand = "GAP";
    private String selectedProductName = "무지티셔츠";
    private String selectedSerial = "AC12";
    private String selectedSize = "L";
    private String selectedColor = "RED";

    private TextView tvTagId, tvTestResult, tvPrice, tvStock;
    private Spinner spinBrand, spinProductName, spinSerial, spinSize, spinColor;

    private boolean checkShowBrandThread = false; //showBrandThreadCheck
    private boolean checkInsertTagThread = false; //showBrandThreadCheck
    private boolean checkSelectAllBrandThread = false; //showBrandThreadCheck
    private boolean checkSelectProudctNameThread = false; //showBrandThreadCheck
    private boolean checkSelectSerial = false; //showBrandThreadCheck
    private boolean checkSelectSize = false; //showBrandThreadCheck
    private boolean checkSelectColor = false; //showBrandThreadCheck
    private boolean checkSelectPriceStock = false; //showBrandThreadCheck

    private ThreadSelectAllBrand threadSelectAllBrand;
    private ThreadInsertTag threadInsertTag;
    private ThreadSelectProductName threadSelectProductName;
    private ThreadSelectSerial threadSelectSerial;
    private ThreadSelectSize threadSelectSize;
    private ThreadSelectColor threadSelectColor;
    private ThreadSelectPriceStock threadSelectPriceStock;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        tvTagId = (TextView) findViewById(R.id.tvTagId);
        tvTestResult = (TextView) findViewById(R.id.tvTestResult);
        spinBrand = (Spinner) findViewById(R.id.spinBrand);
        spinProductName = (Spinner) findViewById(R.id.spinProductName);
        spinSerial = (Spinner) findViewById(R.id.spinSerial);
        spinSize = (Spinner) findViewById(R.id.spinSize);
        spinColor = (Spinner) findViewById(R.id.spinColor);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvStock = (TextView) findViewById(R.id.tvStock);

        // 입력할 url, aar
        url = "url";
        aar = "aar";

//        Log.d("ked", "test1");

//        if (showBrandThreadCheck == true) {
//            threadShowBrand.interrupt();
//        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent = new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    }


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
    public boolean writeTag(NdefMessage message, Tag tag) {

        // 메시지의 byte크기를 얻어옴
        int size = message.toByteArray().length;

        try {

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) { // 새로운 NFC chip일 때

                // formatiing executed
                NdefFormatable formatable = NdefFormatable.get(tag);
                if (formatable != null) {
                    try {
                        formatable.connect();
                        formatable.format(message);
                        Toast.makeText(getApplicationContext(),
                                "tag is formatted successfully",
                                Toast.LENGTH_SHORT).show();
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }

                ndef.connect();

                // 쓰기가 불가능하면
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(), "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                // Tag에 들어간 Ndef메시지의 크기가 허용된 최대 크기보다 크면
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                //NdefMessage를 얻어온 Tag에 입력
                ndef.writeNdefMessage(message);

                Toast.makeText(getApplicationContext(), "쓰기 성공!", Toast.LENGTH_SHORT).show();

                byte[] tagId = tag.getId();
                // 이 부분 주석 안하면 tag read시 에러남
                Toast.makeText(getApplicationContext(), byteArrayToHex(tagId), Toast.LENGTH_SHORT).show();
                tvTagId.setText(byteArrayToHex(tagId)); // tvTagId 세팅


                threadTest(); // test 중

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

                // 쓰기가 불가능하면
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(), "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                // Tag에 들어간 Ndef메시지의 크기가 허용된 최대 크기보다 크면
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                //NdefMessage를 얻어온 Tag에 입력
                ndef.writeNdefMessage(message);

                byte[] tagId = tag.getId();
                // 이 부분 주석 안하면 tag read시 에러남
                Toast.makeText(getApplicationContext(), byteArrayToHex(tagId), Toast.LENGTH_SHORT).show();

                tvTagId.setText(byteArrayToHex(tagId)); // tvTagId 세팅

                Toast.makeText(getApplicationContext(), "쓰기 성공!", Toast.LENGTH_SHORT).show();

                // 최초일 때만 InsertTag

                threadTest(); // test 중


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

    // 포맷하는 메서드
    public void FormatNFC(NdefMessage message, Tag tag) {

        NdefFormatable formatable = NdefFormatable.get(tag);

        if (formatable != null) {
            try {

                formatable.connect();
                formatable.formatReadOnly(message);
                formatable.format(message);
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }
            catch (FormatException fe) {
                fe.printStackTrace();
            }
        }
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

    class ThreadInsertTag extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = tvTagId.getText().toString(); // tagId 가져온다

                final HashMap<String, String>[] result = nfcDao.insertTag(tagId);

                if(result[0].get("status") == "OK") {
                    handler.post(new Runnable() {
                        public void run() {
                            printToastInThread("insertTag Success");
                        }
                    });
                }
                else {
                    printToastInThread("insertTag Fail");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkInsertTagThread = true;
        }
    }

    class ThreadSelectAllBrand extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                HashMap<String, String>[] result = nfcDao.selectAllBrand();
                //hashMapBrandList = result;
                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();

                for(int i = 0; i < result.length; i++)
                    Log.d("brand list", result[i].get("brand"));

                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        itemList.add(hashMap.get("brand")); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("Fail");
                    }
                }

                final ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(NFCActivity.this,
                        android.R.layout.simple_spinner_item, itemList);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                handler.post(new Runnable() {
                    public void run() {
                        spinBrand.setAdapter(adapterBrand);
                    }
                });

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectAllBrandThread = true;
        }
    }

    class ThreadSelectProductName extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectProductName(selectedBrand);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();

                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        itemList.add(hashMap.get("name")); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("selectProductName Fail");
                    }
                }

                final ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(NFCActivity.this,
                        android.R.layout.simple_spinner_item, itemList);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                handler.post(new Runnable() {
                    public void run() {
                        spinProductName.setAdapter(adapterBrand);
                    }
                });

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectProudctNameThread = true;
        }
    }

    class ThreadSelectSerial extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectSerial(selectedBrand, selectedProductName);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();

                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        itemList.add(hashMap.get("serial")); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("selectSerial Fail");
                    }
                }

                final ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(NFCActivity.this,
                        android.R.layout.simple_spinner_item, itemList);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                handler.post(new Runnable() {
                    public void run() {
                        spinSerial.setAdapter(adapterBrand);
                    }
                });

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectSerial = true;
        }
    }

    class ThreadSelectSize extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectSize(selectedBrand, selectedSerial);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();

                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        itemList.add(hashMap.get("size")); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("selectSize Fail");
                    }
                }

                final ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(NFCActivity.this,
                        android.R.layout.simple_spinner_item, itemList);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                handler.post(new Runnable() {
                    public void run() {
                        spinSize.setAdapter(adapterBrand);
                    }
                });

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectSize = true;
        }
    }

    class ThreadSelectColor extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectColor(selectedBrand, selectedSerial, selectedSize);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();

                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        itemList.add(hashMap.get("color")); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("selectColor Fail");
                    }
                }

                final ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(NFCActivity.this,
                        android.R.layout.simple_spinner_item, itemList);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                handler.post(new Runnable() {
                    public void run() {
                        spinColor.setAdapter(adapterBrand);
                    }
                });

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectColor = true;
        }
    }

    class ThreadSelectPriceStock extends Thread {
        @Override
        public void run() {
            try {
                NFCDao nfcDao = new NFCDao(); // DB 접근 객체 생성

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectPriceStock(selectedBrand, selectedSerial
                        , selectedSize, selectedColor);

                //ArrayList<String> itemList = null;
                String stock = null;
                String price = null;

                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        stock = hashMap.get("stock"); // 브랜드 리스트
                        price = hashMap.get("price"); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("selectColor Fail");
                    }
                }

                final String finStock = stock;
                final String finPrice = price;

                handler.post(new Runnable() {
                    public void run() {
                        tvStock.setText(finStock);
                        tvPrice.setText(finPrice);
                    }
                });

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectPriceStock = true;
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


    public void threadTest() {

        // 최초일 때만 InsertTag
        if (checkInsertTagThread == true) {
            threadInsertTag.interrupt();
        }

        if (checkSelectAllBrandThread == true) {
            threadSelectAllBrand.interrupt();
        }

        if (checkSelectProudctNameThread == true) {
            threadSelectProductName.interrupt();
        }

        if(checkSelectSerial == true) {
            threadSelectSerial.interrupt();
        }

        if(checkSelectSize == true) {
            threadSelectSize.interrupt();
        }

        if(checkSelectColor == true) {
            threadSelectColor.interrupt();
        }

        if(checkSelectPriceStock == true) {
            threadSelectPriceStock.interrupt();
        }

        threadInsertTag = new ThreadInsertTag();
        threadInsertTag.start();

        threadSelectAllBrand = new ThreadSelectAllBrand();
        threadSelectAllBrand.start();

        threadSelectProductName = new ThreadSelectProductName();
        threadSelectProductName.start();

        threadSelectSerial = new ThreadSelectSerial();
        threadSelectSerial.start();

        threadSelectSize = new ThreadSelectSize();
        threadSelectSize.start();

        threadSelectColor = new ThreadSelectColor();
        threadSelectColor.start();

        threadSelectPriceStock = new ThreadSelectPriceStock();
        threadSelectPriceStock.start();

    }


}
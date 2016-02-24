package com.hwc.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import com.hwc.dao.common.CommonDao;
import com.hwc.dao.nfc.NFCDao;
import com.hwc.main.R;
import com.hwc.shared.LoginSession;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.ClientProtocolException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class NFCActivity extends Activity {

    private CommonDao commonDao;
    private NFCDao nfcDao;

    private int PICK_IMAGE_REQUEST = 1;
    public static final String UPLOAD_KEY = "image";
    //ProgressDialog prgDialog;

    //String strImageEncoded;
    //RequestParams params = new RequestParams();

    String strImageEncoded, strSelecteProductImagPath = null, strSelecteProductImagName = null;

    //EditText url;           // url 입력 받는 부분
    //EditText aar;           // AAR 입력 받는 부분
    /* branch test */
    private String selectedBrand = null;
    private String selectedProductName = null;
    private String selectedSerial = null;
    private String selectedSize = null;
    private String selectedColor = null;
    private String selectedProductImage = null;

    private TextView tvTagId, tvTestResult, tvPrice, tvStock;
    private Spinner spinBrand, spinProductName, spinSerial, spinSize, spinColor;
    private Button btnSave, btnShowSelectedPrImage, btnUploadSelectedPrImage;
    private ImageView ivSelectedPrImage;
    private Bitmap bitmapSelectedPrImage;

    private boolean boolIsUsed = false;

    private boolean checkShowBrandThread = false;
    private boolean checkInsertTagThread = false;
    private boolean checkSelectAllBrandThread = false;
    private boolean checkSelectProudctNameThread = false;
    private boolean checkSelectSerial = false;
    private boolean checkSelectSize = false;
    private boolean checkSelectColor = false;
    private boolean checkSelectPriceStock = false;
    private boolean checkUpdateProductInfo = false;
    private boolean checkSelectProductInfo = false;
    private boolean checkIsUsed = false;

    private ThreadSelectAllBrand threadSelectAllBrand;
    private ThreadInsertTag threadInsertTag; // no array
    private ThreadSelectProductName threadSelectProductName;
    private ThreadSelectSerial threadSelectSerial;
    private ThreadSelectSize threadSelectSize;
    private ThreadSelectColor threadSelectColor;
    private ThreadSelectPriceStock threadSelectPriceStock;
    private ThreadUpdateProductInfo threadUpdateProductInfo; // no array
    private ThreadSelectProductInfo threadSelectProductInfo;
    private ThreadSelectIsUsed threadSelectIsUsed;
    private ThreadLoadProductImage threadLoadProductImage;

    // bundle 용
    private String strTagIdFromBundle;
    private boolean boolIsUsedFromBundle;
    private boolean boolIsEmptyChipFromBundle;
    private Bundle bundle;

    private LoginSession loginSession;
    private HashMap<String, String> infoListFormPref;

    private AdapterView.OnItemSelectedListener onItemSelectedListenerBrand = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //enableSpinner(spinProductName);

            selectedBrand = (String)spinBrand.getSelectedItem();

            threadSelectProductName = new ThreadSelectProductName();

            if (checkSelectAllBrandThread == true) {
                threadSelectProductName.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadSelectProductName.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    private AdapterView.OnItemSelectedListener onItemSelectedListenerProductName = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //enableSpinner(spinSerial);

            selectedProductName = (String)spinProductName.getSelectedItem();

            threadSelectSerial = new ThreadSelectSerial();

            if (checkSelectProudctNameThread == true) {
                threadSelectSerial.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadSelectSerial.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    private AdapterView.OnItemSelectedListener onItemSelectedListenerSerial = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //enableSpinner(spinSize);

            selectedSerial = (String)spinSerial.getSelectedItem();

            threadSelectSize = new ThreadSelectSize();

            if (checkSelectSize == true) {
                threadSelectSize.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadSelectSize.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    private AdapterView.OnItemSelectedListener onItemSelectedListenerSize = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //enableSpinner(spinColor);

            selectedSize = (String)spinSize.getSelectedItem();

            threadSelectColor = new ThreadSelectColor();

            if (checkSelectColor == true) {
                threadSelectColor.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadSelectColor.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    private AdapterView.OnItemSelectedListener onItemSelectedListenerColor = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //enableSpinner(spinColor);

            selectedColor = (String)spinColor.getSelectedItem();

            threadSelectPriceStock = new ThreadSelectPriceStock();

            if (checkSelectPriceStock == true) {
                threadSelectPriceStock.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadSelectPriceStock.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    View.OnClickListener onClickListenerSave = new View.OnClickListener(){
        public void onClick(View v) {

            threadUpdateProductInfo = new ThreadUpdateProductInfo();

            if (checkUpdateProductInfo == true) {
                threadUpdateProductInfo.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadUpdateProductInfo.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener onClickListenerShowPrImage = new View.OnClickListener(){
        public void onClick(View v) {

            if (commonDao.isNetworkAvailable()) {
                Toast.makeText(getBaseContext(), "갤러리로 연결합니다", Toast.LENGTH_SHORT).show();
                //int PICK_IMAGE_REQUEST = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener onClickListenerUploadPrImage = new View.OnClickListener(){
        public void onClick(View v) {

            if (commonDao.isNetworkAvailable()) {
                Toast.makeText(getBaseContext(), "업로드 테스트 중", Toast.LENGTH_SHORT).show();
                //uploadImage();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    };

    // 갤러리에서 정보 가져온다
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            //strSelecteProductImagName = data.getData();
            //bitmapSelectedPrImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uriSelecteProductImage);
            //ivSelectedPrImage.setImageBitmap(bitmapSelectedPrImage);

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            strSelecteProductImagPath = cursor.getString(columnIndex);
            cursor.close();

             ImageView imgView = (ImageView) findViewById(R.id.ivSelectedPrImage);
            // imgView.setImageBitmap(BitmapFactory.decodeFile(strSelecteProductImagPath));

            Bitmap unscaledBitmap = BitmapFactory.decodeFile(strSelecteProductImagPath);
            Log.d("unscaled height", Integer.toString(unscaledBitmap.getHeight()));
            Log.d("unscaled width", Integer.toString(unscaledBitmap.getWidth()));

            int operandForResizeImg = getOperandForResizeImg(unscaledBitmap);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, unscaledBitmap.getWidth() / operandForResizeImg,
                    unscaledBitmap.getHeight() / operandForResizeImg, ScalingUtilities.ScalingLogic.FIT);
            //Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, imgView.getWidth(), imgView.getHeight(), ScalingUtilities.ScalingLogic.FIT);
            Log.d("scaled height", Integer.toString(scaledBitmap.getHeight()));
            Log.d("scaled width", Integer.toString(scaledBitmap.getWidth()));

            imgView.setImageBitmap(scaledBitmap);

            // Set the Image in ImageView

            // Get the Image's file name
            String fileNameSegments[] = strSelecteProductImagPath.split("/");
            strSelecteProductImagName = fileNameSegments[fileNameSegments.length - 1];

            printToastInThread(strSelecteProductImagName);
            // Put file name in Async Http Post Param which will used in Php web app
            // params.put("filename", strSelecteProductImagName);
            strImageEncoded = encodeImagetoString(scaledBitmap); // String 형태로 변환된 이미지
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        bundle = getIntent().getExtras();
        strTagIdFromBundle = bundle.getString("tagId");
        boolIsUsedFromBundle = bundle.getBoolean("boolIsUsed");
        boolIsEmptyChipFromBundle = bundle.getBoolean("boolIsEmptyChip");

        commonDao = new CommonDao();
        commonDao.setCurrentActivity(this);

        nfcDao = new NFCDao(); // DB 접근 객체 생성

        tvTagId = (TextView) findViewById(R.id.tvTagId);
        tvTestResult = (TextView) findViewById(R.id.tvTestResult);
        spinBrand = (Spinner) findViewById(R.id.spinBrand);
        spinProductName = (Spinner) findViewById(R.id.spinProductName);
        spinSerial = (Spinner) findViewById(R.id.spinSerial);
        spinSize = (Spinner) findViewById(R.id.spinSize);
        spinColor = (Spinner) findViewById(R.id.spinColor);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvStock = (TextView) findViewById(R.id.tvStock);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnShowSelectedPrImage = (Button) findViewById(R.id.btnShowSelectedPrImage);
        btnUploadSelectedPrImage = (Button) findViewById(R.id.btnUploadSelectedPrImage);

        ivSelectedPrImage = (ImageView)findViewById(R.id.ivSelectedPrImage) ;

        // 입력할 url, aar

        spinBrand.setOnItemSelectedListener(onItemSelectedListenerBrand);
        spinProductName.setOnItemSelectedListener(onItemSelectedListenerProductName);
        spinSerial.setOnItemSelectedListener(onItemSelectedListenerSerial);
        spinSize.setOnItemSelectedListener(onItemSelectedListenerSize);
        spinColor.setOnItemSelectedListener(onItemSelectedListenerColor);

        btnSave.setOnClickListener(onClickListenerSave);
        btnShowSelectedPrImage.setOnClickListener(onClickListenerShowPrImage);
        btnUploadSelectedPrImage.setOnClickListener(onClickListenerUploadPrImage);

        // Bundle로 넘겨받은 값 Set
        tvTagId.setText(strTagIdFromBundle);
        boolIsUsed = boolIsUsedFromBundle;

        loginSession = new LoginSession(getApplicationContext());
        infoListFormPref = loginSession.getPreferencesResultHashMap();

        if(boolIsEmptyChipFromBundle == true) // 비어있는 칩이면 == true
            execForUnusedTagThread();
        else {
            if(boolIsUsed == false) { // 사용하지 않았으면 == false
                execForUnusedTagThread();
            }
            else { // 사용했으면 == true
                try {
                    execForUsedTagThread();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Used인지 Unused인지 판단


        //disableSpinner(spinBrand);
        //disableSpinner(spinProductName);
        //disableSpinner(spinSerial);
        //disableSpinner(spinSize);
        //disableSpinner(spinColor);


//        Log.d("ked", "test1");

//        if (showBrandThreadCheck == true) {
//            threadShowBrand.interrupt();
//        }

    }


    // 포맷하는 메서드
//    public void FormatNFC(NdefMessage message, Tag tag) {
//
//        NdefFormatable formatable = NdefFormatable.get(tag);
//
//        if (formatable != null) {
//            try {
//
//                formatable.connect();
//                formatable.formatReadOnly(message);
//                formatable.format(message);
//            }
//
//            catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            catch (FormatException fe) {
//                fe.printStackTrace();
//            }
//        }
//    }


    class ThreadInsertTag extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = tvTagId.getText().toString(); // tagId 가져온다

                final HashMap<String, String> result = nfcDao.insertTag(tagId);

                if(result != null) {
                    if (result.get("status") == "OK") {
                        handler.post(new Runnable() {
                            public void run() {
                                printToastInThread("insertTag Success" + " Message : " + result.get("message"));
                            }
                        });
                    } else {
                        printToastInThread("insertTag Fail" + " Message : " + result.get("message"));
                    }
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

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                HashMap<String, String>[] result = nfcDao.selectAllBrand();
                //hashMapBrandList = result;
                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();
                itemList.add("");
                itemList.add(infoListFormPref.get("brand"));

                for(int i = 0; i < result.length; i++)
                    Log.d("brand list", result[i].get("brand"));

                /*
                for(HashMap<String, String> hashMap : result) {
                    if (hashMap.get("status") == "OK") {
                        itemList.add(hashMap.get("brand")); // 브랜드 리스트
                    }
                    else {
                        printToastInThread("Fail");
                    }
                }
                */

                final ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(NFCActivity.this,
                        android.R.layout.simple_spinner_item, itemList);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                boolean post = handler.post(new Runnable() {
                    public void run() {
                        spinBrand.setAdapter(adapterBrand);
                        if (selectedBrand != null) {
                            if (!selectedBrand.equals("")) {
                                int spinPosition = adapterBrand.getPosition(selectedBrand);
                                spinBrand.setSelection(spinPosition);
                            }
                        }
                        //spinBrand.setSelection(null);
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

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectProductName(selectedBrand);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();
                itemList.add("");

                if(result != null) {

                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            itemList.add(hashMap.get("name")); // 브랜드 리스트
                        } else {
                            printToastInThread("selectProductName Fail");
                        }
                    }

                    final ArrayAdapter<String> adapterProductName = new ArrayAdapter<String>(NFCActivity.this,
                            android.R.layout.simple_spinner_item, itemList);
                    adapterProductName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    Log.d("ThreadSelectProductName", "executed 1");

                    handler.post(new Runnable() {
                        public void run() {
                            spinProductName.setAdapter(adapterProductName);
                            if(selectedProductName != null) {
                                if (!selectedProductName.equals("")) {
                                    int spinPosition = adapterProductName.getPosition(selectedProductName);
                                    spinProductName.setSelection(spinPosition);
                                }
                            }
                        }
                    });

                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            selectedProductName = null;
                            selectedSerial = null;
                            selectedSize = null;
                            selectedColor = null;
                            deleteItemsInSpin(spinProductName); // Item delete
                            deleteItemsInSpin(spinSerial); // Item delete
                            deleteItemsInSpin(spinSize); // Item delete
                            deleteItemsInSpin(spinColor); // Item delete
                            tvPrice.setText(null);
                            tvStock.setText(null);
                        }
                    });
                }

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

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectSerial(selectedBrand, selectedProductName);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();
                itemList.add("");

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            itemList.add(hashMap.get("serial")); // 브랜드 리스트
                        } else {
                            printToastInThread("selectSerial Fail");
                        }
                    }

                    final ArrayAdapter<String> adapterSerial = new ArrayAdapter<String>(NFCActivity.this,
                            android.R.layout.simple_spinner_item, itemList);
                    adapterSerial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    handler.post(new Runnable() {
                        public void run() {
                            spinSerial.setAdapter(adapterSerial);
                            if(selectedSerial != null) {
                                if (!selectedSerial.equals("")) {
                                    int spinPosition = adapterSerial.getPosition(selectedSerial);
                                    spinSerial.setSelection(spinPosition);
                                }
                            }
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            selectedSerial = null;
                            selectedSize = null;
                            selectedColor = null;
                            deleteItemsInSpin(spinSerial); // Item delete
                            deleteItemsInSpin(spinSize); // Item delete
                            deleteItemsInSpin(spinColor); // Item delete
                            tvPrice.setText(null);
                            tvStock.setText(null);
                        }
                    });
                }

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

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectSize(selectedBrand, selectedSerial);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();
                itemList.add("");

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            itemList.add(hashMap.get("size")); // 브랜드 리스트
                        } else {
                            printToastInThread("selectSize Fail");
                        }
                    }

                    final ArrayAdapter<String> adapterSize = new ArrayAdapter<String>(NFCActivity.this,
                            android.R.layout.simple_spinner_item, itemList);
                    adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    handler.post(new Runnable() {
                        public void run() {
                            spinSize.setAdapter(adapterSize);
                            if(selectedSize != null) {
                                if (!selectedSize.equals("")) {
                                    int spinPosition = adapterSize.getPosition(selectedSize);
                                    spinSize.setSelection(spinPosition);
                                }
                            }
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            selectedSize = null;
                            selectedColor = null;
                            deleteItemsInSpin(spinSize); // Item delete
                            deleteItemsInSpin(spinColor); // Item delete
                            tvPrice.setText(null);
                            tvStock.setText(null);
                        }
                    });
                }

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

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectColor(selectedBrand, selectedSerial, selectedSize);

                ArrayList<String> itemList = null;
                itemList = new ArrayList<String>();
                itemList.add("");

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            itemList.add(hashMap.get("color")); // 브랜드 리스트
                        } else {
                            printToastInThread("selectColor Fail");
                        }
                    }

                    final ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(NFCActivity.this,
                            android.R.layout.simple_spinner_item, itemList);
                    adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    handler.post(new Runnable() {
                        public void run() {
                            spinColor.setAdapter(adapterColor);
                            if(selectedColor != null) {
                                if (!selectedColor.equals("")) {
                                    int spinPosition = adapterColor.getPosition(selectedColor);
                                    spinColor.setSelection(spinPosition);
                                }
                            }
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            selectedColor = null;
                            deleteItemsInSpin(spinColor); // Item delete
                            tvPrice.setText(null);
                            tvStock.setText(null);
                        }
                    });
                }

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

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                HashMap<String, String>[] result = nfcDao.selectPriceStock(selectedBrand, selectedSerial
                        , selectedSize, selectedColor);

                //ArrayList<String> itemList = null;
                String stock = null;
                String price = null;

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            stock = hashMap.get("stock"); // 브랜드 리스트
                            price = hashMap.get("price"); // 브랜드 리스트
                        } else {
                            printToastInThread("selectColor Fail");
                        }
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

    // 사진도 같이 업로드 해야 함
    class ThreadUpdateProductInfo extends Thread {
        @Override
        public void run() {
            try {

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = tvTagId.getText().toString(); // tagId 가져온다
                String extName = null, noSpacePrName = null;


                if(strSelecteProductImagName != null)
                    extName = extractExtName(strSelecteProductImagName);
                if(strSelecteProductImagName != null)
                    noSpacePrName = convertStrNoSpace(selectedProductName);

                //printToastInThread("blank : " + noSpacePrName);
                // printToastInThread(selectedProductName);
                // printToastInThread(extName);

                final HashMap<String, String> result = nfcDao.updateProductInfo(tagId,
                        selectedBrand, noSpacePrName, selectedSerial, selectedSize, selectedColor, strImageEncoded, extName);
                        //selectedBrand, selectedProductName, selectedSerial, selectedSize, selectedColor, strImageEncoded, extName);

                if(result != null) {
                    if (result.get("status") == "OK") {
                        handler.post(new Runnable() {
                            public void run() {
                            printToastInThread("productInfo save Success" + " Message : " + result.get("message"));
                            }
                        });
                    } else {
                        printToastInThread("productInfo save Fail" + " Message : " + result.get("message"));
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkUpdateProductInfo = true;
        }
    }

    class ThreadSelectProductInfo extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = tvTagId.getText().toString(); // tagId 가져온다

                final HashMap<String, String>[] result = nfcDao.selectProductInfo(tagId);

                //printToastInThread("ThreadSelectProductInfo In");

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            selectedSerial = hashMap.get("serial");
                            selectedColor = hashMap.get("color");
                            selectedSize = hashMap.get("size");
                            selectedProductName = hashMap.get("name");
                            selectedBrand = hashMap.get("brand");
                            selectedProductImage = hashMap.get("image");
                            setTextView(hashMap.get("price"), tvPrice);
                            setTextView(hashMap.get("stock"), tvStock);
                            //printToastInThread("ThreadSelectProductInfo Success");
                        } else {
                            printToastInThread("ThreadSelectProductInfo Fail");
                        }
                    }
                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            selectedSerial = "";
                            selectedColor = "";
                            selectedSize = "";
                            selectedProductName = "";
                            selectedBrand = "";
                            deleteItemsInSpin(spinColor); // Item delete
                            tvPrice.setText(null);
                            tvStock.setText(null);
                        }
                    });
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkSelectProductInfo = true;
        }
    }

    class ThreadSelectIsUsed extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = tvTagId.getText().toString(); // tagId 가져온다

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

    // 이미지 불러와준다
    class ThreadLoadProductImage extends Thread {
        @Override
        public void run() {
            // Looper.getMainLooper() : main UI 접근하기 위함
            // main UI 내의 요소를 변경하기 위한 핸들러
            Handler handler = new Handler(Looper.getMainLooper());

            bitmapSelectedPrImage = commonDao.loadBitmap(selectedProductImage);

            handler.post(new Runnable() {
                public void run() {
                    ivSelectedPrImage.setImageBitmap(bitmapSelectedPrImage);
                }
            });
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

    private void setTextView(final String text, final TextView textView) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        }, 0);
    }

    public void execForUnusedTagThread() {

        // 최초일 때만 InsertTag
//        if (checkInsertTagThread == true) {
//            threadInsertTag.interrupt();
//        }

        // Looper.getMainLooper() : main UI 접근하기 위함
        // main UI 내의 요소를 변경하기 위한 핸들러
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            public void run() {
                selectedBrand = null;
                selectedProductName = null;
                selectedSerial = null;
                selectedSize = null;
                selectedColor = null;
                ivSelectedPrImage.setImageResource(android.R.color.transparent); // 이미지 투명으로 없앤다

                deleteItemsInSpin(spinProductName); // Item delete
                deleteItemsInSpin(spinSerial); // Item delete
                deleteItemsInSpin(spinSize); // Item delete
                deleteItemsInSpin(spinColor); // Item delete
                tvPrice.setText(null);
                tvStock.setText(null);
            }
        });

        if (checkSelectAllBrandThread == true) {
            threadSelectAllBrand.interrupt();
        }

//        threadInsertTag = new ThreadInsertTag();
//        threadInsertTag.start();

        threadSelectAllBrand = new ThreadSelectAllBrand();
        threadSelectAllBrand.start();

    }

    public void execForUsedTagThread() throws InterruptedException {

        threadSelectProductInfo = new ThreadSelectProductInfo();
        threadSelectProductInfo.start();
        threadSelectProductInfo.join(); // threadSelectProductInfo가 끝날 때까지 기다림

        threadLoadProductImage = new ThreadLoadProductImage(); // 상품 이미지 불러온다
        threadLoadProductImage.start();

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

        if(checkSelectProductInfo == true) {
            threadSelectProductInfo.interrupt();
        }

//        threadInsertTag = new ThreadInsertTag();
//        threadInsertTag.start();

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

    private void deleteItemsInSpin(Spinner spinner) {
        spinner.setAdapter(null);
    }

    private void deleteSpinnerItemsExceptSelected(Spinner spinner, String selectedItem) {
        ArrayList<String> itemList = null;
        itemList = new ArrayList<String>();
        itemList.add("");
        itemList.add(selectedItem);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NFCActivity.this,
                    android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void disableSpinner(Spinner spinner) {
        spinner.getSelectedView();
        spinner.setEnabled(false);
    }

    private void enableSpinner(Spinner spinner) {
        spinner.setEnabled(true);
    }

    private String encodeImagetoString(Bitmap scaledBitmap) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        // bitmap = BitmapFactory.decodeFile(strSelecteProductImagPath, options);
        // bitmap = scaledBitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        return Base64.encodeToString(byte_arr, 0);
    }

    private String extractExtName(String fileName) {
        String extName = null;
        extName = Files.getFileExtension(fileName);
        return extName;
    }

    private String convertStrNoSpace(String productName) {
        String noSpaceStr = productName.replace(" ", "");
        return noSpaceStr;
        //replaceAll("\\s", "")
    }

    private int getOperandForResizeImg(Bitmap bitmap) {
        int maxHeight = 100;
        int operand = operand = bitmap.getHeight() / maxHeight;
        return operand;
    }

    /*
    private void setAllSelectedData(String serial, String color, String size, String productName,
                                    String brand, String price, String stock) {
        selectedSerial = serial;
        selectedColor = color;
        selectedSize = size;
        selectedProductName = productName;
        selectedBrand = brand;
        tvPrice.setText(price);
        tvStock.setText(stock);
    }
    */

    /*
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            //RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NFCActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                printToastInThread(uploadImage);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                // String result = commonDao.sendPostRequest(UPLOAD_URL,data);
                // pkPath
                // String pkPath = "/images/php/tagging/insertToPaidCart.php";
                String result = commonDao.sendPostRequest(data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmapSelectedPrImage); // 실제 thread 실행
    }
    */

    // When Upload button is clicked
    /*
    public void uploadImage() {
        // When Image is selected from Gallery

        if (strSelecteProductImagPath != null && !strSelecteProductImagPath.isEmpty()) {
            //prgDialog.setMessage("Converting Image to Binary Data");
            //prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        }
        else {
            Toast.makeText( getApplicationContext(), "You must select image from gallery before you try to upload", Toast.LENGTH_LONG).show();
        }
    }
    */

    // AsyncTask - To convert Image to String
    /*
    private String encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(strSelecteProductImagPath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                strImageEncoded = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                //prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }
    */

    /*
    public void triggerImageUpload() {
        makeHTTPCall();
    }
    */

    /*
    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        //prgDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/NFC/addImage.php",
        params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http
            // response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //prgDialog.hide();
                Toast.makeText(getApplicationContext(), "onSucceess In", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }

            // When the response returned by REST has Http
            // response code other than '200' such as '404',
            // '500' or '403' etc
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                //prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(),
                            "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    */

    /*
    // progress bar 꺼지게 하는 곳
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            //prgDialog.dismiss();
        }
    }
    */


}
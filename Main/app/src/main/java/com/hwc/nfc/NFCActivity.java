package com.hwc.nfc;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.io.Files;
import com.hwc.dao.common.CommonDao;
import com.hwc.dao.nfc.NFCDao;
import com.hwc.main.R;
import com.hwc.shared.LoginSession;

import org.apache.http.client.ClientProtocolException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NFCActivity extends Activity {

    private CommonDao commonDao;
    private NFCDao nfcDao;

    private int PICK_IMAGE_REQUEST = 1;
    public static final String UPLOAD_KEY = "image";

    String strImageEncoded, strSelecteProductImagPath = null, strSelecteProductImagName = null;

    private String selectedBrand = null;
    private String selectedProductName = null;
    private String selectedSerial = null;
    private String selectedSize = null;
    private String selectedColor = null;
    private String selectedProductImage = null;
    private TextView tvTagId;
    private TextView tvTestResult, tvPrice, tvStock;
    private Spinner spinBrand, spinProductName, spinSerial, spinSize, spinColor;
    private Button btnSave, btnShowSelectedPrImage;
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
    private ThreadUpdateProductInfo threadUpdateProductInfo;
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
                Toast.makeText(getBaseContext(), "갤러리로 연결합니다.", Toast.LENGTH_SHORT).show();
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
        spinBrand = (Spinner) findViewById(R.id.spinBrand);
        spinProductName = (Spinner) findViewById(R.id.spinProductName);
        spinSerial = (Spinner) findViewById(R.id.spinSerial);
        spinSize = (Spinner) findViewById(R.id.spinSize);
        spinColor = (Spinner) findViewById(R.id.spinColor);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvStock = (TextView) findViewById(R.id.tvStock);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnShowSelectedPrImage = (Button)findViewById(R.id.btnShowSelectedPrImage);


        ivSelectedPrImage = (ImageView)findViewById(R.id.ivSelectedPrImage) ;

        spinBrand.setOnItemSelectedListener(onItemSelectedListenerBrand);
        spinProductName.setOnItemSelectedListener(onItemSelectedListenerProductName);
        spinSerial.setOnItemSelectedListener(onItemSelectedListenerSerial);
        spinSize.setOnItemSelectedListener(onItemSelectedListenerSize);
        spinColor.setOnItemSelectedListener(onItemSelectedListenerColor);

        btnSave.setOnClickListener(onClickListenerSave);
        btnShowSelectedPrImage.setOnClickListener(onClickListenerShowPrImage);

        // Bundle로 넘겨받은 값 Set
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

    }

    class ThreadInsertTag extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = strTagIdFromBundle; // tagId 가져온다

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

                /* 브랜드 정보 모두다 가져오는 부분
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

                HashMap<String, String>[] result = nfcDao.selectPriceStock(selectedBrand, selectedSerial
                        , selectedSize, selectedColor);

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

    class ThreadUpdateProductInfo extends Thread {
        @Override
        public void run() {
            try {

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = strTagIdFromBundle; // tagId 가져온다
                String extName = null, noSpacePrName = null;


                if(strSelecteProductImagName != null)
                    extName = extractExtName(strSelecteProductImagName);
                if(strSelecteProductImagName != null)
                    noSpacePrName = convertStrNoSpace(selectedProductName);

                final HashMap<String, String> result = nfcDao.updateProductInfo(tagId,
                        selectedBrand, noSpacePrName, selectedSerial, selectedSize, selectedColor, strImageEncoded, extName);

                if(result != null) {
                    if (result.get("status") == "OK") {
                        handler.post(new Runnable() {
                            public void run() {
                                printToastInThread(result.get("message"));
                            }
                        });
                    } else {
                        printToastInThread(result.get("message"));
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
                String tagId = strTagIdFromBundle; // tagId 가져온다

                final HashMap<String, String>[] result = nfcDao.selectProductInfo(tagId);

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            setTextView(tagId, tvTagId);
                            selectedSerial = hashMap.get("serial");
                            selectedColor = hashMap.get("color");
                            selectedSize = hashMap.get("size");
                            selectedProductName = hashMap.get("name");
                            selectedBrand = hashMap.get("brand");
                            selectedProductImage = hashMap.get("image");
                            setTextView(hashMap.get("price"), tvPrice);
                            setTextView(hashMap.get("stock"), tvStock);
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
                String tagId = strTagIdFromBundle; // tagId 가져온다

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
        int maxHeight = 280;
        int operand = operand = bitmap.getHeight() / maxHeight;
        return operand;
    }

}
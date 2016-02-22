package com.hwc.tagging;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hwc.dao.common.CommonDao;
import com.hwc.dao.nfc.NFCDao;
import com.hwc.dao.tagging.TaggingDao;
import com.hwc.main.R;
import com.hwc.shared.LoginSession;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TaggingActivity extends Activity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private CommonDao commonDao;
    private NFCDao nfcDao;
    private TaggingDao taggingDao;

    // SharedPrefence를 위한 멤버 변수
    private LoginSession loginSession;
    private HashMap<String, String> infoList = new HashMap<String, String>();
    private HashMap<String, String> infoListFormPref; //= new HashMap<String, String>();

    //EditText url;           // url 입력 받는 부분
    //EditText aar;           // AAR 입력 받는 부분
    /* branch test */

    private String selectedBrand;
    private String selectedProductName;
    private String selectedSerial;
    private String selectedSize;
    private String selectedColor;
    private String selectedCount;
    private String selectedProductImage;

    private Bitmap bitmapSelectedPrImage;

    private String userId = "default"; // test용

    private TextView tvTagId, tvTestResult, tvPrice, tvStock;
    private TextView tvBrand, tvProductName, tvSerial; //tvSize, tvColor;
    private Spinner spinSize, spinColor;
    //private EditText edtRequestCount;
    private TextView tvRequestCount;
    private ImageView ivSelectedPrImage;

    private Button btnPaying, btnCart, btnIncPrCount, btnDecPrCount;
    private final Integer MAX_COUNT = 20;
    private int maxProductCount = 1;

    private boolean boolIsUsed = false;

    private boolean checkSelectProductInfo = false;
    private boolean checkInsertProductIntoCart = false;
    private boolean checkInsertProductPaying = false;

    private boolean checkSelectSize = false;
    private boolean checkSelectColor = false;
    private boolean checkSelectPriceStock = false;
    private boolean checkIsUsed = false;

    private ThreadSelectSize threadSelectSize;
    private ThreadSelectColor threadSelectColor;
    private ThreadSelectPriceStock threadSelectPriceStock;

    private ThreadSelectProductInfo threadSelectProductInfo;
    private ThreadInsertProductIntoCart threadInsertProductIntoCart;
    private ThreadInsertProductPaying threadInsertProductPaying;
    private ThreadSelectIsUsed threadSelectIsUsed;
    private ThreadLoadProductImage threadLoadProductImage;

//    private TextWatcher twTotalPriceCalculator;
//
//    private void twTotalPriceCalculatorGenerator() {
//        twTotalPriceCalculator = new TextWatcher() {
//            public void afterTextChanged(Editable editableObj) {
//                String inputText = edtRequestCount.getText().toString();
//                if (inputText.equals("")) { // editText가 비어있으면
//                    edtRequestCount.setText("개수를 입력하세요");
//                }
//                else {
//                    // 장바구니에 담을 상품 개수
//                    int productCount = Integer.parseInt(edtRequestCount.getText().toString());
//                    if(productCount > 0 && productCount <= 50) { // 가격 계산
//                        tvPrice.setText(Integer.toString(productCount * Integer.parseInt(tvPrice.getText().toString())));
//                    }
//                    else
//                        edtRequestCount.setText("올바른 개수를 입력하세요");
//                }
//
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                //Do something or nothing.
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //Do something or nothing
//            }
//        };
//
//    }

    View.OnClickListener onClickListenerCart = new View.OnClickListener(){
        public void onClick(View v) {

            threadInsertProductIntoCart = new ThreadInsertProductIntoCart();

            if (checkInsertProductIntoCart == true) {
                threadInsertProductIntoCart.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadInsertProductIntoCart.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener onClickListenerPaying = new View.OnClickListener(){
        public void onClick(View v) {

            threadInsertProductPaying = new ThreadInsertProductPaying();

            if (checkInsertProductPaying == true) {
                threadInsertProductPaying.interrupt();
            }
            if (commonDao.isNetworkAvailable()) {
                threadInsertProductPaying.start();
            }
            else {
                Toast.makeText(getBaseContext(), "네트워크 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener onClickListenerIncPrCount = new View.OnClickListener(){
        public void onClick(View v) {
            int count = Integer.valueOf(tvRequestCount.getText().toString());
            String tvStockStr = tvStock.getText().toString();

            if(tvStockStr.equals("") || tvStockStr.equals("null"))
                maxProductCount = 1;
            else
                maxProductCount = Integer.valueOf(tvStockStr);

            //if(count < MAX_COUNT) {
            if(count < maxProductCount) {
                tvRequestCount.setText(Integer.toString(count + 1));
            }
        }
    };

    View.OnClickListener onClickListenerDecPrCount = new View.OnClickListener(){
        public void onClick(View v) {
            int count = Integer.valueOf(tvRequestCount.getText().toString());
            if(count > 1) {
                tvRequestCount.setText(Integer.toString(count - 1));
            }
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);

        commonDao = new CommonDao();
        commonDao.setCurrentActivity(this);

        nfcDao = new NFCDao();
        taggingDao = new TaggingDao(); // DB 접근 객체 생성

        // 로그인 정보 가져오는 부분
        loginSession = new LoginSession(getApplicationContext());
        infoListFormPref = loginSession.getPreferencesResultHashMap();
        userId = infoListFormPref.get("id");

        tvTagId = (TextView) findViewById(R.id.tvTagId);
        tvTestResult = (TextView) findViewById(R.id.tvTestResult);
        tvBrand = (TextView) findViewById(R.id.tvResultBrand);
        tvProductName = (TextView) findViewById(R.id.tvResultProductName);
        tvSerial = (TextView) findViewById(R.id.tvResultSerial);
        spinColor = (Spinner) findViewById(R.id.spinColor);
        spinSize = (Spinner) findViewById(R.id.spinSize);
        //tvSize = (TextView) findViewById(R.id.tvResultSize);
        //tvColor = (TextView) findViewById(R.id.tvResultColor);
        tvPrice = (TextView) findViewById(R.id.tvResultPrice);
        tvStock = (TextView) findViewById(R.id.tvResultStock);
        //edtRequestCount = (EditText) findViewById(R.id.edtRequestCount);
        tvRequestCount = (TextView) findViewById(R.id.tvRequestCount);
        btnPaying = (Button)findViewById(R.id.btnPaying);
        btnCart = (Button)findViewById(R.id.btnCart);
        btnIncPrCount = (Button) findViewById(R.id.btnIncreaseProductCount);
        btnDecPrCount = (Button) findViewById(R.id.btnDecreaseProductCount);

        btnCart.setOnClickListener(onClickListenerCart); // 장바구니 onClickListener 연결
        btnPaying.setOnClickListener(onClickListenerPaying); // 결제 onClickListener 연결

        btnIncPrCount.setOnClickListener(onClickListenerIncPrCount); // 상품 개수 증가 (플러스 버튼)
        btnDecPrCount.setOnClickListener(onClickListenerDecPrCount); // 상품 개수 감소 (마이너스 버튼)

        spinColor.setOnItemSelectedListener(onItemSelectedListenerColor);
        spinSize.setOnItemSelectedListener(onItemSelectedListenerSize);

        ivSelectedPrImage = (ImageView)findViewById(R.id.ivSelectedPrImage) ;

//        twTotalPriceCalculatorGenerator();
//        edtRequestCount.addTextChangedListener(twTotalPriceCalculator);

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

        // 감지된 태그를 가리키는 객체
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // 감지된 태그 Read
        readTag(detectedTag);
    }

    // 감지된 태그에 NdefMessage를 쓰는 메소드
    public boolean readTag(Tag tag) {

        try {

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) { // 새로운 NFC chip일 때

                byte[] tagId = tag.getId();

                tvTagId.setText("포맷이 필요합니다" + byteArrayToHex(tagId)); // tvTagId 세팅
                //Toast.makeText(getApplicationContext(), tvTagId.getText().toString(), Toast.LENGTH_SHORT).show();

                return true;

            }

            // 어떤 data가 들어있으면
            else { // ndef != null

                ndef.connect();

                byte[] tagId = tag.getId();

                //Toast.makeText(getApplicationContext(), byteArrayToHex(tagId), Toast.LENGTH_SHORT).show();

                tvTagId.setText(byteArrayToHex(tagId)); // tvTagId 세팅
                //Toast.makeText(getApplicationContext(), tvTagId.getText().toString(), Toast.LENGTH_SHORT).show();

                    threadSelectIsUsed = new ThreadSelectIsUsed();

                    threadSelectIsUsed.start();
                    threadSelectIsUsed.join();

                    if(boolIsUsed == true) { // used가 1이면
                        Toast.makeText(getApplicationContext(), "used tag", Toast.LENGTH_SHORT).show();
                        tvRequestCount.setText("1");
                        execSelectProductInfoThread();
                    }
                    else { // used가 0이면
                        Toast.makeText(getApplicationContext(), "unused tag", Toast.LENGTH_SHORT).show();
                        execSelectProductInfoThread();
                        ivSelectedPrImage.setImageResource(android.R.color.transparent);
                        // 상품 정보 보여준 것 초기화
                    tvRequestCount.setText("1");
                    tvBrand.setText("");
                    tvProductName.setText("");
                    tvSerial.setText("");
                }

                //execSelectProductInfoThread();

                return true;
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }
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

    class ThreadSelectProductInfo extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());
                String tagId = tvTagId.getText().toString(); // tagId 가져온다

                final HashMap<String, String>[] result = taggingDao.selectProductInfo(tagId);

                // printToastInThread("ThreadSelectProductInfo In");

                if(result != null) {
                    for (HashMap<String, String> hashMap : result) {
                        if (hashMap.get("status") == "OK") {
                            selectedSerial = hashMap.get("serial");
                            selectedColor = hashMap.get("color");
                            selectedSize = hashMap.get("size");
                            selectedProductName = hashMap.get("name");
                            selectedBrand = hashMap.get("brand");
                            selectedProductImage = hashMap.get("image");
                            setTextView(hashMap.get("serial"), tvSerial);
//                            setTextView(hashMap.get("color"), tvColor);
//                            setTextView(hashMap.get("size"), tvSize);
                            setTextView(hashMap.get("name"), tvProductName);
                            setTextView(hashMap.get("brand"), tvBrand);
                            setTextView(hashMap.get("price"), tvPrice);
                            setTextView(hashMap.get("stock"), tvStock);

                            printToastInThread("ThreadSelectProductInfo Success");
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
                            //deleteItemsInSpin(spinColor); // Item delete
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

    //checkInsertProductIntoCart
    class ThreadInsertProductIntoCart extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                //selectedCount = edtRequestCount.getText().toString(); // 장바구니에 넣을 개수
                selectedCount = tvRequestCount.getText().toString(); // 장바구니에 넣을 개수

                Log.d("cart insert", "userId : " + userId);
                Log.d("cart insert", "selectedCount : " + selectedCount);
                Log.d("cart insert", "selectedSerial : "+ selectedSerial);
                Log.d("cart insert", "selectedSize : " + selectedSize);
                Log.d("cart insert", "selectedColor : " + selectedColor);

                final HashMap<String, String> result = taggingDao.insertProductIntoCart(userId,
                        selectedCount, selectedSerial, selectedSize, selectedColor);

                // printToastInThread("ThreadInsertProductIntoCart In");

                if(result != null) {
                    if (result.get("status") == "OK") {
                        printToastInThread("ThreadInsertProductIntoCart Success" + " Message : " + result.get("message"));
                    } else {
                        printToastInThread("ThreadInsertProductIntoCart Fail" + " Message : " + result.get("message"));
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
                            //deleteItemsInSpin(spinColor); // Item delete
                            tvPrice.setText(null);
                            tvStock.setText(null);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkInsertProductIntoCart = true;
        }
    }

    //checkInsertProductIntoCart
    class ThreadInsertProductPaying extends Thread {
        @Override
        public void run() {
            try {
                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                //selectedCount = edtRequestCount.getText().toString(); // 구매할 개수
                selectedCount = tvRequestCount.getText().toString(); // 구매할 개수

                final HashMap<String, String> result = taggingDao.insertProductPaying(userId,
                        selectedCount, selectedSerial, selectedSize, selectedColor);

                // printToastInThread("ThreadInsertProductPaying In");

                if(result != null) {
                    if (result.get("status") == "OK") {
                        printToastInThread("ThreadInsertProductPaying Success" + " Message : " + result.get("message"));
                    } else {
                        printToastInThread("ThreadInsertProductPaying Fail" + " Message : " + result.get("message"));
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
                        //deleteItemsInSpin(spinColor); // Item delete
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
            checkInsertProductPaying = true;
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

                    final ArrayAdapter<String> adapterSize = new ArrayAdapter<String>(TaggingActivity.this,
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

                    final ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(TaggingActivity.this,
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

    public void execSelectProductInfoThread() throws InterruptedException {

        threadSelectProductInfo = new ThreadSelectProductInfo();
        threadSelectProductInfo.start();
        threadSelectProductInfo.join(); // threadSelectProductInfo가 끝날 때까지 기다림

        threadLoadProductImage = new ThreadLoadProductImage(); // 상품 이미지 불러온다
        threadLoadProductImage.start();

        Log.d("cart insert", "threadSelectProductInfo killed");

        if(checkSelectProductInfo == true) {
            threadSelectProductInfo.interrupt();
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

    private void disableSpinner(Spinner spinner) {
        spinner.getSelectedView();
        spinner.setEnabled(false);
    }

    private void enableSpinner(Spinner spinner) {
        spinner.setEnabled(true);
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


}


package com.hwc.cart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwc.dao.cart.CartDao;
import com.hwc.main.R;
import com.hwc.paid.PaidActivity;
import com.hwc.shared.LoginSession;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
 * Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
 * img_test.setImageBitmap(bmp);
 */

public class CartActivity extends Activity {
    public ListView_custom adapter;
    public String myJSON;

    public static final String HWC = "HWC";
    public static final String TAG_RESULTS = "result";
    public static final String TAG_COLOR = "PR_COLOR";
    public static final String TAG_SIZE = "PR_SIZE";
    public static final String TAG_NAME = "PR_NAME";
    public static final String TAG_BRAND = "PR_BRAND";
    public static final String TAG_IMAGE = "PR_IMAGE";
    public static final String TAG_PRICE = "PR_PRICE";
    public static final String TAG_SNUM = "PR_SNUM";
    public ArrayList<String> data_name = new ArrayList<>();
    public ArrayList<String> data_size = new ArrayList<>();
    public ArrayList<String> data_color = new ArrayList<>();
    public ArrayList<String> data_brand = new ArrayList<>();
    public ArrayList<String> data_image = new ArrayList<>();
    public ArrayList<String> data_price = new ArrayList<>();
    public ArrayList<String> data_snum = new ArrayList<>();


    public static ArrayList<Integer> data_intprice = new ArrayList<>();

    private CartDao CartDao;

    public static JSONArray cart = null;
    public static int rowLength = 0;

    public ListView list;
    public ProgressDialog progDialog;

    public Button btnSend;
    public Button bt_request;
    public String imgUrl = "http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";
    public Bitmap btp_test;
    public ImageView img_test;

    // SharedPrefence를 위한 멤버 변수
    private String userId = "default";
    private LoginSession loginSession;
    private HashMap<String, String> infoList = new HashMap<String, String>();
    private HashMap<String, String> infoListFormPref; //= new HashMap<String, String>();

    private HashMap<String, String>[] hashMapResult = null;

    private ThreadSelectProductInfo threadSelectProductInfo;

    public static TextView txt_intprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setMessage("로딩중입니다......");
        progDialog.show();

        loginSession = new LoginSession(getApplicationContext());
        infoListFormPref = loginSession.getPreferencesResultHashMap();
        userId = infoListFormPref.get("id");

        txt_intprice = (TextView) findViewById(R.id.txt_intprice);
        //txt_intprice.setText("가격 : 테스트중");
        CartDao = new CartDao();
        //cartList = new ArrayList<>();

        //getData("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/cart/cart.php");
        //try {

        //} catch (IOException e) {
        //   e.printStackTrace();
        //}

        threadSelectProductInfo = new ThreadSelectProductInfo();
        try {
            threadSelectProductInfo.start();
            threadSelectProductInfo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showList(hashMapResult);

        bt_request = (Button) findViewById(R.id.bt_request);
        bt_request.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cfrmDialog();
            }
        });

/*        while(true) {
            try {
                Log.d(HWC, "Activity에서의 data_checked = " + data_checked);
                Thread.sleep(3000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
    }

    public void showList(HashMap<String, String>[] hashMapResult) {
        if (hashMapResult != null)
            rowLength = Integer.valueOf(hashMapResult[0].get("resultNum"));

//        try {
//            JSONObject jsonObj = new JSONObject(myJSON);
//            cart = jsonObj.getJSONArray(TAG_RESULTS);

        adapter = new ListView_custom(getApplicationContext());
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

        try {
            //*JSON 언어*//*
            //for (int i = 0; i < cart.length(); i++) {
            for (int i = 0; i < rowLength; i++) {
                //JSONObject c = cart.getJSONObject(i);
                data_name.add(hashMapResult[i].get(TAG_NAME));
                data_size.add(hashMapResult[i].get(TAG_SIZE));
                data_color.add(hashMapResult[i].get(TAG_COLOR));
                data_brand.add(hashMapResult[i].get(TAG_BRAND));
                data_image.add(hashMapResult[i].get(TAG_IMAGE));
                data_price.add(hashMapResult[i].get(TAG_PRICE));
                data_snum.add(hashMapResult[i].get(TAG_SNUM));

                //Log.d("showList i", Integer.toString(i));
                //Log.d("showList", hashMapResult[i].get(TAG_PRICE));


                data_intprice.add(Integer.parseInt(data_price.get(i)));
                //price_sum += data_intprice.get(i);
            }
        } catch (Exception e) {
            Toast.makeText(CartActivity.this, "등록된 상품이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        //Log.d(HWC, "data_price의 값 : " + data_price);

        //Log.d(HWC, "snum의 값 : " + data_snum);

        txt_intprice.setText("가격 : " + Integer.toString(adapter.price_sum));

        for (int i = 0; i < rowLength; i++) {
            ListView_getset u = new ListView_getset(data_name.get(i), data_size.get(i),
                    data_color.get(i), data_brand.get(i), data_image.get(i), data_price.get(i), data_snum.get(i));
            adapter.add(u);
        }

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


   /* public void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            cart = jsonObj.getJSONArray(TAG_RESULTS);
            adapter = new ListView_custom(getApplicationContext());
            list = (ListView) findViewById(R.id.listView);
            list.setAdapter(adapter);

            /*//*JSON 언어*//**//*
            for (int i = 0; i < rowLength; i++) {
                JSONObject c = cart.getJSONObject(i);
                data_name.add(c.getString(TAG_NAME));
                data_size.add(c.getString(TAG_SIZE));
                data_color.add(c.getString(TAG_COLOR));
                data_brand.add(c.getString(TAG_BRAND));
                data_image.add(c.getString(TAG_IMAGE));
                data_price.add(c.getString(TAG_PRICE));
                data_snum.add(c.getString(TAG_SNUM));

                data_intprice.add(Integer.parseInt(data_price.get(i)));
                //price_sum += data_intprice.get(i);
            }
            Log.d(HWC, "data_price의 값 : " + data_price);

            Log.d(HWC, "snum의 값 : " + data_snum);

            txt_intprice.setText("가격 : " + Integer.toString(adapter.price_sum));

            for (int i = 0; i < rowLength; i++) {
                ListView_getset u = new ListView_getset(data_name.get(i), data_size.get(i),
                        data_color.get(i), data_brand.get(i), data_image.get(i), data_price.get(i), data_snum.get(i));
                adapter.add(u);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    static public void setTextPrice(final int input) {
        txt_intprice.setText("가격 : " + Integer.toString(input));
    }

/*    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
                progDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                adapter.price_sum = 0;
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void executeTheadTest() {
        class TheadTest extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    insertPay();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                Intent intent = new Intent(CartActivity.this, PaidActivity.class);
                startActivity(intent);
            }
        }
        TheadTest theadTest = new TheadTest();
        theadTest.execute();
    }

    public void cfrmDialog() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setIcon(R.mipmap.ic_launcher);
        alertDlg.setMessage("결제하시겠습니까?");
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
                        AlertDialog.Builder alertDlg = new AlertDialog.Builder(CartActivity.this);
                        alertDlg.setIcon(R.mipmap.ic_launcher);
                        alertDlg.setMessage("결제가 완료되었습니다. 구매 목록에서 확인하세요.");
                        alertDlg.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        /*php 불러옴*/
                                        executeTheadTest();

                                        //getCfrmRequest("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/cart/insertToPaidCartFromCart.php");
                                        /*Activity는 여기서 바꾸면 됨*/
//                                        Intent intent = new Intent(CartActivity.this, PaidActivity.class);
//                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alert = alertDlg.create();
                        alert.setTitle("깃털쇼핑_1조");
                        alert.show();
                    }
                });
        AlertDialog alert = alertDlg.create();
        alert.setTitle("깃털쇼핑_1조");
        alert.show();
    }

    public void insertPay() throws IOException {
        HashMap<String, String>[] result = new HashMap[rowLength];
        //boolean queryResult = false;
        for (int i = 0; i < rowLength; i++) {
            result[i] = new HashMap<String, String>();
            //HashMap<String, String>[] result = CartDao.insertProductPaying(data_snum.get(i), data_size.get(i), data_color.get(i));
            if (ListView_custom.data_checked.get(i) == true)
                result[i] = CartDao.insertProductPaying(data_snum.get(i), data_size.get(i), data_color.get(i), userId);
        }
    }

    class ThreadSelectProductInfo extends Thread {
        @Override
        public void run() {
            try {

                // Looper.getMainLooper() : main UI 접근하기 위함
                // main UI 내의 요소를 변경하기 위한 핸들러
                Handler handler = new Handler(Looper.getMainLooper());

                // test : GAP
                //HashMap<String, String>[] result = nfcDao.selectProductName(selectedBrand);
                hashMapResult = CartDao.selectProductsInCart(userId);

                if (hashMapResult != null) {

                    for (HashMap<String, String> hashMap : hashMapResult) {
                        if (hashMap.get("status") == "OK") {
                            //itemList.add(hashMap.get("name")); // 브랜드 리스트
                        } else {
                            //printToastInThread("selectProductName Fail");
                        }
                    }
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


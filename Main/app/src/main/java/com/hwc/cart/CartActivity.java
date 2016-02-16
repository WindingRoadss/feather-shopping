package com.hwc.cart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hwc.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
    // public static final String TAG_SNUM = "PR_SNUM";
    public static final String TAG_COLOR = "PR_COLOR";
    public static final String TAG_SIZE = "PR_SIZE";
    public static final String TAG_NAME = "PR_NAME";
    public static final String TAG_BRAND = "PR_BRAND";
    public static final String TAG_IMAGE = "PR_IMAGE";
    public static final String TAG_PRICE = "PR_PRICE";
    public ArrayList<String> data_name = new ArrayList<>();
    public ArrayList<String> data_size = new ArrayList<>();
    public ArrayList<String> data_color = new ArrayList<>();
    public ArrayList<String> data_brand = new ArrayList<>();
    public ArrayList<String> data_image = new ArrayList<>();
    public ArrayList<String> data_price = new ArrayList<>();
    public static ArrayList<Integer> data_intprice = new ArrayList<>();
    public ArrayList<Boolean> data_checked = new ArrayList<>();

    public static JSONArray cart = null;
    public ListView list;

    public Button btnSend;
    public String imgUrl = "http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";
    public Bitmap btp_test;
    public ImageView img_test;

    public static TextView txt_intprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txt_intprice = (TextView) findViewById(R.id.txt_intprice);
        //txt_intprice.setText("가격 : 테스트중");

        //cartList = new ArrayList<>();
        getData("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/cart/cart.php");
    }

    public void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            cart = jsonObj.getJSONArray(TAG_RESULTS);
            adapter = new ListView_custom(getApplicationContext());
            list = (ListView) findViewById(R.id.listView);
            list.setAdapter(adapter);

            //*JSON 언어*//*
            for (int i = 0; i < cart.length(); i++) {
                JSONObject c = cart.getJSONObject(i);
                data_name.add(c.getString(TAG_NAME));
                data_size.add(c.getString(TAG_SIZE));
                data_color.add(c.getString(TAG_COLOR));
                data_brand.add(c.getString(TAG_BRAND));
                data_image.add(c.getString(TAG_IMAGE));
                data_price.add(c.getString(TAG_PRICE));

                data_intprice.add(Integer.parseInt(data_price.get(i)));
                //price_sum += data_intprice.get(i);
            }
            //Log.d(HWC, "data_image의 주소 : " + data_image);

            //Log.d(HWC, "price_sum의 값 : " + adapter.price_sum);

            txt_intprice.setText("가격 : " + Integer.toString(adapter.price_sum));

            for (int i = 0; i < cart.length(); i++) {
                ListView_getset u = new ListView_getset(data_name.get(i), data_size.get(i), data_color.get(i), data_brand.get(i), data_image.get(i), data_price.get(i));
                adapter.add(u);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static public void setTextPrice(final int input) {
        txt_intprice.setText("가격 : " + Integer.toString(input));
    }

    public void getData(String url) {
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
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                adapter.price_sum = 0;
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
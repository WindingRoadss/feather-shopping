package com.hwc.cart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.hwc.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
 * Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
 * imageView.setImageBitmap(bmp);
 */

public class CartActivity extends Activity {
    public ListView_custom adapter;
    public String myJSON;

    public static final String HWC = "HWC";
    private static final String TAG_RESULTS = "result";
    // private static final String TAG_SNUM = "PR_SNUM";
    private static final String TAG_COLOR = "PR_COLOR";
    private static final String TAG_SIZE = "PR_SIZE";
    private static final String TAG_NAME = "PR_NAME";
    private static final String TAG_BRAND = "PR_BRAND";
    private static final String TAG_IMAGE = "PR_IMAGE";
    ArrayList<String> data_name = new ArrayList<>();
    ArrayList<String> data_size = new ArrayList<>();
    ArrayList<String> data_color = new ArrayList<>();
    ArrayList<String> data_brand = new ArrayList<>();

    public JSONArray cart = null;
    private ListView list;

    private Button btnSend;
    public String imgUrl = "http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";
    public Bitmap btp_test;
    public ImageView img_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //cartList = new ArrayList<>();
        getData("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/test.php");
        getBitmap(imgUrl);
    }

    protected void showList() {
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
            }

            for (int i = 0; i < data_name.size(); i++) {
                ListView_getset u = new ListView_getset(data_name.get(i), data_size.get(i), data_color.get(i), data_brand.get(i));
                adapter.add(u);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    public Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream(); // get inputstream
            retBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }
}
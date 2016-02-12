package com.hwc.cart;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hwc.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
 Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
 imageView.setImageBitmap(bmp);
 */

public class CartActivity extends Activity {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    // private static final String TAG_SNUM = "PR_SNUM";
    private static final String TAG_COLOR = "PR_COLOR";
    private static final String TAG_SIZE = "PR_SIZE";
    private static final String TAG_NAME = "PR_NAME";
    private static final String TAG_BRAND = "PR_BRAND";

    JSONArray cart = null;

    ArrayList<HashMap<String, String>> cartList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        list = (ListView) findViewById(R.id.listView);
        cartList = new ArrayList<HashMap<String, String>>();
        getData("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/test.php");
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            cart = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < cart.length(); i++) {
                JSONObject c = cart.getJSONObject(i);

                String name = c.getString(TAG_NAME);
                String size = c.getString(TAG_SIZE);
                String color = c.getString(TAG_COLOR);
                String brand = c.getString(TAG_BRAND);

                HashMap<String, String> cart = new HashMap<String, String>();

                cart.put(TAG_NAME, name);
                cart.put(TAG_SIZE, size);
                cart.put(TAG_COLOR, color);
                cart.put(TAG_BRAND, brand);

                cartList.add(cart);
            }

            ListAdapter adapter = new SimpleAdapter(
                    CartActivity.this, cartList, R.layout.list_item,
                    new String[]{TAG_NAME, TAG_SIZE, TAG_COLOR, TAG_BRAND},
                    new int[]{R.id.name, R.id.size, R.id.color, R.id.brand}
            );

            list.setAdapter(adapter);

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
}
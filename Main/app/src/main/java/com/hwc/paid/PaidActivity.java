package com.hwc.paid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hwc.dao.paid.PaidDao;
import com.hwc.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class PaidActivity extends Activity {
    public PaidListView_custom adapter;
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
    public static final String TAG_SNUM = "PR_SNUM";
    public static final String TAG_BRDEL = "CA_BRDEL";
    public ArrayList<String> data_name = new ArrayList<>();
    public ArrayList<String> data_size = new ArrayList<>();
    public ArrayList<String> data_color = new ArrayList<>();
    public ArrayList<String> data_brand = new ArrayList<>();
    public ArrayList<String> data_image = new ArrayList<>();
    public ArrayList<String> data_price = new ArrayList<>();
    public ArrayList<String> data_snum = new ArrayList<>();
    public ArrayList<String> data_brdel = new ArrayList<>();

    public Button bt_bring;
    public Button bt_delivery;
    public static JSONArray paid = null;
    public ListView list;
    public ProgressDialog progDialog;
    public static TextView txt_intprice;
    private PaidDao PaidDao;
    public boolean ifViewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);
        progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setMessage("로딩중입니다......");
        progDialog.show();

        txt_intprice = (TextView) findViewById(R.id.txt_intprice);
        bt_bring = (Button) findViewById(R.id.bt_bring);
        bt_delivery = (Button) findViewById(R.id.bt_delivery);
        //txt_intprice.setText("가격 : 테스트중");
        PaidDao = new PaidDao();
        //cartList = new ArrayList<>();
        getData("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/paid/paid.php");
        bt_bring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.flag == false) {
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(PaidActivity.this);
                    alertDlg.setIcon(R.mipmap.ic_launcher);
                    alertDlg.setMessage("체크된 상품이 없습니다.");
                    alertDlg.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // 이벤트 실행
                                }
                            });
                    AlertDialog alert = alertDlg.create();
                    alert.setTitle("깃털쇼핑_1조");
                    alert.show();
                }
                if (adapter.flag == true) {
                    askBringDialog();
                }
            }
        });

        bt_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.flag == false) {
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(PaidActivity.this);
                    alertDlg.setIcon(R.mipmap.ic_launcher);
                    alertDlg.setMessage("체크된 상품이 없습니다.");
                    alertDlg.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // 이벤트 실행
                                }
                            });
                    AlertDialog alert = alertDlg.create();
                    alert.setTitle("깃털쇼핑_1조");
                    alert.show();
                }
                if (adapter.flag == true) {
                    askDeliveryDialog();
                }
            }
        });
    }

    public void showList() {

        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            paid = jsonObj.getJSONArray(TAG_RESULTS);
            adapter = new PaidListView_custom(getApplicationContext());
            list = (ListView) findViewById(R.id.listView);
            list.setAdapter(adapter);

            //*JSON 언어*//*
            for (int i = 0; i < paid.length(); i++) {
                JSONObject c = paid.getJSONObject(i);
                data_name.add(c.getString(TAG_NAME));
                data_size.add(c.getString(TAG_SIZE));
                data_color.add(c.getString(TAG_COLOR));
                data_brand.add(c.getString(TAG_BRAND));
                data_image.add(c.getString(TAG_IMAGE));
                data_price.add(c.getString(TAG_PRICE));
                data_snum.add(c.getString(TAG_SNUM));
                data_brdel.add(c.getString(TAG_BRDEL));
            }

            for (int i = 0; i < paid.length(); i++) {
                PaidListView_getset u = new PaidListView_getset(data_name.get(i), data_size.get(i), data_color.get(i), data_brand.get(i), data_image.get(i), data_price.get(i), data_snum.get(i), data_brdel.get(i));
                adapter.add(u);
            }
            ifViewed = true;

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
                progDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void askBringDialog() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(PaidActivity.this);
        alertDlg.setIcon(R.mipmap.ic_launcher);
        alertDlg.setMessage("수령하시겠습니까?");
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
                        // 이벤트 실행
                    }
                });
        AlertDialog alert = alertDlg.create();
        alert.setTitle("깃털쇼핑_1조");
        alert.show();
    }

    public void askDeliveryDialog() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(PaidActivity.this);
        alertDlg.setIcon(R.mipmap.ic_launcher);
        alertDlg.setMessage("배송으로 받으시겠습니까?");
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
                        // 이벤트 실행
                        executeTheadTest();
                        Intent intent = new Intent(getBaseContext(), PaidActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = alertDlg.create();
        alert.setTitle("깃털쇼핑_1조");
        alert.show();
    }

    public void executeTheadTest() {
        class TheadTest extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    insertDelivery();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }
        TheadTest theadTest = new TheadTest();
        theadTest.execute();
    }

    public void insertDelivery() throws IOException {
        HashMap<String, String>[] result = new HashMap[paid.length()];
        //boolean queryResult = false;
        for (int i = 0; i < paid.length(); i++) {
            result[i] = new HashMap<String, String>();
            //HashMap<String, String>[] result = CartDao.insertProductPaying(data_snum.get(i), data_size.get(i), data_color.get(i));
            if (PaidListView_custom.data_checked.get(i) == true)
                result[i] = PaidDao.insertProductPaying(data_snum.get(i), data_size.get(i), data_color.get(i));
        }
    }
}
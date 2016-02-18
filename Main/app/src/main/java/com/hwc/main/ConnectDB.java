package com.hwc.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;

@SuppressWarnings("deprecation")
public class ConnectDB {
    private static String id = "main에서 처리해야 돼요"; //id 값 계속 갖고 있기
    private static String user_name = "main에서 처리해야 돼요"; //이름 값 계속 갖고 있기
    private static String email = "main에서 처리해야 돼요"; //이름 값 계속 갖고 있기
    private static ArrayList<Activity> actList = new ArrayList<Activity>(); // activity 전부 갖고 있기
    private static Activity currentActivity;

    public static void setActivity(Activity act) {
        currentActivity = act;
    }
    //setter getter
    public static void setId(String str_id) { // ID를 계속 갖고 있는다
        id = str_id;
    }
    public static String getId() {
        return id;
    }
    public static void setName(String str_name) { // ID를 계속 갖고 있는다
        user_name = str_name;
    }
    public static void setEmail(String str_email) { // ID를 계속 갖고 있는다
        email = str_email;
    }
    public static void deleteAllActList() {
        for (int i = 0; i < actList.size(); i++) {
            Log.d("deleteAllActList", "ActList Number : " + i);
            actList.get(i).finish();  //List가 Static 이므로, Class명.변수명.get으로 접근
            actList.remove(i);
        }
    }

    //main menu
    public static ArrayList<String> login(String id, String password)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        HttpPost request = makeHttpPost("id", id, "pwd", password,
                "http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/login/0217EncryptLogin.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("user_name");
        tags.add("email");
        Log.d("connecDB", "connectDB viewrsvhsp list");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //json parsing
    public static ArrayList<String> getResult(HttpResponse response, ArrayList<String> tags) //results가 여러개 넘어오기 때문에
            throws IllegalStateException, IOException {

        ArrayList<String> result = new ArrayList<String>();
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        Log.d("sb result", "sb result : " + sb.toString());
        JSONObject o;
        try {
            o = new JSONObject(sb.toString());
            if (o.getString("status").equals("OK")) {
                result.add("OK");
                JSONArray ja = o.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    for (String tag : tags) {
                        JSONObject jo = ja.getJSONObject(i);
                        result.add(jo.getString(tag));
                        Log.d("connecDB", "connectDB getresult list OK");
                        Log.d("connecDB", "result : " + jo.getString(tag));
                    }
                }
            } else if (o.getString("status").equals("FIN"))
                result.add("FIN");
            else {
                result.add("NO");
                Log.d("connecDB", "connectDB getresult list NO");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    //make entity
    private static HttpEntity makeEntity(Vector<NameValuePair> nameValue) {
        HttpEntity result = null;
        try {
            result = new UrlEncodedFormEntity(nameValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    // parameter 2
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    static boolean isNetworkAvailable() {
        ConnectivityManager connec = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connec.getNetworkInfo(0);
        NetworkInfo wifiInfo = connec.getNetworkInfo(1);
        NetworkInfo wimaxInfo = connec.getNetworkInfo(6);
        boolean bm = false;
        boolean bw = false;
        boolean bx = false;
        if (mobileInfo != null) bm = mobileInfo.isConnected();
        if (wimaxInfo != null) bx = wimaxInfo.isConnected();
        if (wifiInfo != null) bw = wifiInfo.isConnected();
        return (bm || bw || bx);
    }
}

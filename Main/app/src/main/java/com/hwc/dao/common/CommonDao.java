package com.hwc.dao.common;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class CommonDao {

    private String webServerURL = "http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com";
    private Activity currentActivity;

    public String getWebServerURL() {
        return webServerURL;
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public void setWebServerURL(String webServerURL) {
        this.webServerURL = webServerURL;
    }

    //make entity
    public HttpEntity makeEntity(Vector<NameValuePair> nameValue) {
        HttpEntity result = null;
        try {
            result = new UrlEncodedFormEntity(nameValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public HttpPost makeHttpPost(ArrayList<String> tagList,
                                         ArrayList<String> valueList, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();

        int size = tagList.size(); // NameValuePair의 총 갯수
        if(size != valueList.size()) // tagList와 valueList의 사이즈는 같아야 한다
            return null;
        else {
            for(int i = 0; i < size; i++) {
                nameValue.add(new BasicNameValuePair(tagList.get(i), valueList.get(i)));
            }
        }
//        nameValue.add(new BasicNameValuePair(t1, v1));
//        nameValue.add(new BasicNameValuePair(t2, v2));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    //json parsing
    public HashMap<String, String>[] getResult(HttpResponse response, ArrayList<String> tags) //results가 여러개 넘어오기 때문에
            throws IllegalStateException, IOException {

        HashMap<String, String> result[] = null; //= new HashMap<String, String>()[];
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
            String numResults = o.getString("num_results");
            // if(!numResults.equals("0"))
            result = new HashMap[Integer.valueOf(numResults)];
            for(int hashIndex = 0; hashIndex < result.length; hashIndex++) {
                result[hashIndex] = new HashMap<String, String>(); // 객체 생성
                if (o.getString("status").equals("OK")) {
                    result[hashIndex].put("status", "OK");
                    JSONArray ja = o.getJSONArray("results");
                    for (String tag : tags) {
                        JSONObject jo = ja.getJSONObject(hashIndex);
                        result[hashIndex].put(tag, jo.getString(tag));
                        Log.d("CommonDAO", "CommonDAO getresult list OK");
                        Log.d("CommonDAO", tag + " : " + jo.getString(tag));
                    }
                }
                else if (o.getString("status").equals("FIN")) {
                    result[hashIndex].put("status", "FIN");
                }
                else {
                    Log.d("CommonDao", "hashIndex : " + Integer.toString(hashIndex));
                    result[hashIndex].put("status", "NO");
                    Log.d("CommonDAO", "CommonDAO getresult list NO");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public boolean isNetworkAvailable() {
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


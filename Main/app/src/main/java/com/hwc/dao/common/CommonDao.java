package com.hwc.dao.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;


public class CommonDao {

    private String webServerURL = "http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com";
    private Activity currentActivity;

    /**********  File Path *************/
    final String uploadFilePath = "storage/emulated/0/";//경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보
    final String uploadFileName = "testimage.jpg"; //전송하고자하는 파일 이름

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

    //json parsing : 하나의 레코드만 반환받는다
    public HashMap<String, String> getResultNoArray(HttpResponse response, ArrayList<String> tags) //results가 여러개 넘어오기 때문에
            throws IllegalStateException, IOException {

        HashMap<String, String> result = null; //= new HashMap<String, String>()[];
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
            result = new HashMap<String, String>();

            result.put("message", o.getString("message")); // 메시지 저장

            if (o.getString("status").equals("OK")) {
                result.put("status", "OK");
                JSONArray ja = o.getJSONArray("results");
                if(!numResults.equals("0")) {
                    int tagLength = 0;
                    for (String tag : tags) {
                        JSONObject jo = ja.getJSONObject(tagLength);
                        result.put(tag, jo.getString(tag));
                        Log.d("CommonDAO", "CommonDAO get result no array list OK");
                        Log.d("CommonDAO", tag + " : " + jo.getString(tag));
                        tagLength++;
                    }
                }
            }
            else if (o.getString("status").equals("FIN")) {
                result.put("status", "FIN");
            }
            else {
                result.put("status", "NO");
                Log.d("CommonDAO", "CommonDAO getresult list NO");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
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
            if(numResults.equals("0"))
                return null;
             else {
                result = new HashMap[Integer.valueOf(numResults)];
            }
            for(int hashIndex = 0; hashIndex < result.length; hashIndex++) {
                result[hashIndex] = new HashMap<String, String>(); // 객체 생성
                if(!numResults.equals("0"))
                    result[0].put("resultNum", numResults);
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

    public Bitmap loadBitmap( String $imagePath ) {
        // TODO Auto-generated method stub
        InputStream inputStream = openHttpConnection( $imagePath ) ;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream) ;
        return bitmap;
    }

    public InputStream openHttpConnection(String $imagePath) {
        // TODO Auto-generated method stub
        InputStream stream = null ;
        try {
            Log.d("url encode", $imagePath);
            //Log.d("url encode", URLEncoder.encode($imagePath, "utf-8"));
            //URL url = new URL(URLEncoder.encode($imagePath, "utf-8"));
            URL url = new URL($imagePath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection() ;
            urlConnection.setRequestMethod( "GET" ) ;
            urlConnection.connect() ;
            if( urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                stream = urlConnection.getInputStream() ;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stream ;
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



    /*
    public String sendGetRequest(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String result;

            StringBuilder sb = new StringBuilder();

            while((result = bufferedReader.readLine())!=null){
                sb.append(result);
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
    */



    /*
    public String sendPostRequest(HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(webServerURL + "/php/tagging/insertToPaidCart.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    */

}


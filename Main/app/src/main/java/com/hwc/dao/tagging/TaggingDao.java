package com.hwc.dao.tagging;

import com.hwc.dao.common.CommonDao;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by K on 2016-02-12.
 */
public class TaggingDao {

    private CommonDao commonDao = new CommonDao();

    public HashMap<String, String>[] selectProductInfo(String tagId) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/usedNFCProductInfo.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("tag");
        valueList.add(tagId);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("serial");
        resultTags.add("color");
        resultTags.add("size");
        resultTags.add("name");
        resultTags.add("brand");
        resultTags.add("price");
        resultTags.add("stock");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String> insertProductPaying(String userId, String count, String serial
            , String size, String color) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/tagging/insertToPaidCart.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("userid");
        valueList.add(userId);
        tagList.add("count");
        valueList.add(count);
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);
        tagList.add("color");
        valueList.add(color);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        //resultTags.add("price");
        //resultTags.add("stock");
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }

    public HashMap<String, String> insertProductIntoCart(String userId, String count, String serial
            , String size, String color) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/tagging/insertToCart.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("userid");
        valueList.add(userId);
        tagList.add("count");
        valueList.add(count);
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);
        tagList.add("color");
        valueList.add(color);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        //resultTags.add("price");
        //resultTags.add("stock");
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }

}

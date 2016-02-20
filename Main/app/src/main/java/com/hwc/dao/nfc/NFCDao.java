package com.hwc.dao.nfc;

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
public class NFCDao {

    private CommonDao commonDao = new CommonDao();

    public HashMap<String, String> insertTag(String tagId)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/addNewTagIdToTBNFC.php";

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
//        resultTags.add("user_name");
//        resultTags.add("email");
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectAllBrand()
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listBrand.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("brand");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectProductName(String brandName)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listName.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("brand");
        valueList.add(brandName);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("name");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectSerial(String brandName, String productName)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listSerial.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("name");
        valueList.add(productName);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("serial");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectSize(String brandName, String serial)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listSize.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("serial");
        valueList.add(serial);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("size");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectColor(String brandName, String serial, String size)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listColor.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("color");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectPriceStock(String brandName, String serial
            , String size, String color) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listSelectedProductInfo.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("brand");
        valueList.add(brandName);
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
        resultTags.add("price");
        resultTags.add("stock");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String> updateProductInfo(String tagId, String brandName, String productName, String serial
            , String size, String color, String image, String extName) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/updateTagIdToSelectedProductWithImage.php";

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("tag");
        valueList.add(tagId);
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("name");
        valueList.add(productName);
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);
        tagList.add("color");
        valueList.add(color);
        tagList.add("image");
        valueList.add(image);
        tagList.add("extname");
        valueList.add(extName);

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
        resultTags.add("image");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectIsUsed(String tagId) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/checkUsedNFC.php";

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
        resultTags.add("used");
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

}

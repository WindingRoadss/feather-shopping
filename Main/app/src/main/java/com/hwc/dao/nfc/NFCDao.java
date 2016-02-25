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

    // NFC Tag ID를 DB Table에 insert 해주는 함수
    public HashMap<String, String> insertTag(String tagId)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/addNewTagIdToTBNFC.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("tag");
        valueList.add(tagId);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터) (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }

    // DB에 있는 브랜드 리스트 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectAllBrand()
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listBrand.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("brand");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // 선택된 브랜드에 맞는 상품 이름 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectProductName(String brandName)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listName.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("brand");
        valueList.add(brandName);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("name");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // 선택된 상품 이름에 맞는 일련번호 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectSerial(String brandName, String productName)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listSerial.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("name");
        valueList.add(productName);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("serial");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // 선택된 일련번호에 맞는 사이즈 정보 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectSize(String brandName, String serial)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listSize.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("serial");
        valueList.add(serial);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("size");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // 선택된 사이즈 맞는 색상 정보 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectColor(String brandName, String serial, String size)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listColor.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("color");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // 선택된 상품 정보에 맞는 가격 및 재고 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectPriceStock(String brandName, String serial
            , String size, String color) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/listSelectedProductInfo.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("brand");
        valueList.add(brandName);
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);
        tagList.add("color");
        valueList.add(color);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("price");
        resultTags.add("stock");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // NFC 칩이 나타내는 상품 정보 수정해주는 쿼리 수행 함수
    public HashMap<String, String> updateProductInfo(String tagId, String brandName, String productName, String serial
            , String size, String color, String image, String extName) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/updateTagIdToSelectedProductWithImage.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
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

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();

        // 쿼리 수행후 받은 HashMap result
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }

    // NFC 칩이 나타내는 상품 정보 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectProductInfo(String tagId) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/usedNFCProductInfo.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("tag");
        valueList.add(tagId);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("serial");
        resultTags.add("color");
        resultTags.add("size");
        resultTags.add("name");
        resultTags.add("brand");
        resultTags.add("price");
        resultTags.add("stock");
        resultTags.add("image");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    public HashMap<String, String>[] selectIsUsed(String tagId) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/NFC/checkUsedNFC.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("tag");
        valueList.add(tagId);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("used");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

}

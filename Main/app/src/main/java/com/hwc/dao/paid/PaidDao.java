package com.hwc.dao.paid;

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
public class PaidDao {

    private CommonDao commonDao = new CommonDao();

    // 결제된 상품 정보 가져오는 쿼리 수행 함수
    public HashMap<String, String>[] selectProductsInCart(String userId)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/paid/paid_m.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("usid");
        valueList.add(userId);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("PR_COLOR");
        resultTags.add("PR_SIZE");
        resultTags.add("PR_NAME");
        resultTags.add("PR_BRAND");
        resultTags.add("PR_IMAGE");
        resultTags.add("PR_PRICE");
        resultTags.add("PR_SNUM");
        resultTags.add("CA_BRDEL");
        resultTags.add("CA_PRCNT");

        // 쿼리 수행후 받은 HashMap 배열 result
        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

    // 결제된 상품 배송 처리하는 쿼리 수행 함수
    public HashMap<String, String> insertProductPaying(String serial
            , String size, String color, String usid, String prcnt) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/paid/insertToDeliveryFromPaid.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);
        tagList.add("color");
        valueList.add(color);
        tagList.add("usid");
        valueList.add(usid);
        tagList.add("prcnt");
        valueList.add(prcnt);

        // 쿼리 수행후 받은 HashMap 배열 result
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();

        // 쿼리 수행후 받은 HashMap result
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }

    // 결제된 상품 수령 처리하는 쿼리 수행 함수
    public HashMap<String, String> Bring(String serial
            , String size, String color, String usid, String prcnt) throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/paid/Bring.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("serial");
        valueList.add(serial);
        tagList.add("size");
        valueList.add(size);
        tagList.add("color");
        valueList.add(color);
        tagList.add("usid");
        valueList.add(usid);
        tagList.add("prcnt");
        valueList.add(prcnt);

        // 쿼리 수행후 받은 HashMap 배열 result
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();

        // 쿼리 수행후 받은 HashMap result
        HashMap<String, String> result = commonDao.getResultNoArray(response, resultTags);
        return result;
    }
}

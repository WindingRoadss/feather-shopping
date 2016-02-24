package com.hwc.dao.login;

import com.hwc.dao.common.CommonDao;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginDao {

    private CommonDao commonDao = new CommonDao();

    public HashMap<String, String>[] selectLoginUserInfo(String userId, String password)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/login/encryptLogin.php";

        //getData("http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/php/cart/cart.php")

//        HttpPost request = makeHttpPost("id", id, "pwd", password,
//                paramURL);
        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입
        tagList.add("id");
        valueList.add(userId);
        tagList.add("pwd");
        valueList.add(password);

        /*
        data_name.add(c.getString(TAG_NAME));
        data_size.add(c.getString(TAG_SIZE));
        data_color.add(c.getString(TAG_COLOR));
        data_brand.add(c.getString(TAG_BRAND));
        data_image.add(c.getString(TAG_IMAGE));
        data_price.add(c.getString(TAG_PRICE));
        data_snum.add(c.getString(TAG_SNUM));
        */

        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("admin");

        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

}

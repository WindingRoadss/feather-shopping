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

    // 로그인 쿼리 수행 함수
    public HashMap<String, String>[] selectLoginUserInfo(String userId, String password)
            throws ClientProtocolException, IOException {
        // TODO Auto-generated method stub
        String paramURL = commonDao.getWebServerURL() + "/php/login/encryptLogin.php";

        ArrayList<String> tagList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();

        // 매개변수 List 데이터 삽입 php에 전송할 데이터
        tagList.add("id");
        valueList.add(userId);
        tagList.add("pwd");
        valueList.add(password);

        // 연결 및 request 수행
        HttpPost request = commonDao.makeHttpPost(tagList, valueList, paramURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // resultTags는 column 이름 (쿼리 수행 결과 데이터)
        ArrayList<String> resultTags = new ArrayList<String>();
        resultTags.add("admin");
        resultTags.add("name");
        resultTags.add("brand");

        HashMap<String, String>[] result = commonDao.getResult(response, resultTags);
        return result;
    }

}

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
    private static boolean petNumberAvailable = false;

    public static void setPetNumberAvailable(boolean bool) {
        petNumberAvailable = bool;
    }

    public static boolean getPetNumberAvailable() {
        return petNumberAvailable;
    }

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

    public static String getUserName() {
        return user_name;
    }

    public static void setEmail(String str_email) { // ID를 계속 갖고 있는다
        email = str_email;
    }

    public static String getUserEmail() {
        return email;
    }

    public static void addActList(Activity activity) {
        actList.add(activity);
    }

    public static void deleteAllActList() {
        for (int i = 0; i < actList.size(); i++) {
            Log.d("deleteAllActList", "ActList Number : " + i);
            actList.get(i).finish();  //List가 Static 이므로, Class명.변수명.get으로 접근
            actList.remove(i);
        }
    }

    public static void deleteActListExceptMain() {
        for (int i = actList.size() - 1; i > 1; i--) {
            Log.d("deleteAllActList", "ActList Total Number : " + actList.size());
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
                "http://ec2-52-36-28-13.us-west-2.compute.amazonaws.com/login.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("user_name");
        tags.add("email");
        Log.d("connecDB", "connectDB viewrsvhsp list");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    /* 회원 탈퇴 */
    public static ArrayList<String> withDraw(String id) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "http://210.94.181.168/member_delete.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    /*회원가입*/
    public static String addMember(String name, String id, String password, String user_name, String haveVisited, String email, String phone)
            throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pwd", password, "user", user_name, "visited", haveVisited,
                "email", email, "phone", phone, "http://210.94.181.168/member_join.php");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("id");
        tags.add("pwd");
        tags.add("user");
        tags.add("visited");
        tags.add("email");
        tags.add("phone");
        ArrayList<String> result = getResult(response, tags);
        return result.get(0);

    }

    //search pwd
    public static ArrayList<String> searchPassword(String id, String name, String email) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "user_name", name, "email", email, "http://210.94.181.168/search_pwd.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //pet Info
    public static String addPet(String id, String pet_name, String age,
                                String type, String detail, String sex, String specific) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "name", pet_name, "age", age, "pet_type", type,
                "pet_detail", detail, "sex", sex, "specific", specific, "http://210.94.181.168/pet_add.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("id");
        tags.add("name");
        tags.add("age");
        tags.add("pet_type");
        tags.add("pet_detail");
        tags.add("sex");
        tags.add("specific");
        ArrayList<String> result = getResult(response, tags);
        return result.get(0);
    }

    public static String deletePet(String id, String pet_name) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "http://210.94.181.168/pet_info_del.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("id");
        tags.add("pet_name");
        ArrayList<String> result = getResult(response, tags);
        return result.get(0);
    }

    public static ArrayList<String> getPetNames(String id) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "http://210.94.181.168/my_pet.php");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();

        tags.add("name");
        Log.d("connecDB", "connectDB getPetNames");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> getPetInfo(String id, String pet_name)
            throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "name", pet_name, "http://210.94.181.168/pet_info_view.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("pet_name");
        tags.add("age");
        tags.add("pet_type");
        tags.add("pet_detail");
        tags.add("sex");
        tags.add("specific");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //여기
    public static ArrayList<String> modifyPetInfo(String id, String pet_name, String age, String pet_type,
                                                  String pet_detail, String sex, String specific)
            throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "name", pet_name,
                "age", age, "pet_type", pet_type, "pet_detail", pet_detail,
                "sex", sex, "specific", specific, "http://210.94.181.168/pet_info_update.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        //tag 필요한가???
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //reservation
    /*
	public static ArrayList<String> getIsReserved(String year, String month,
			String day) throws ClientProtocolException, IOException{
		HttpPost request = makeHttpPost("year", year, "month", month, "day", day,
				 "http://210.94.181.168/show_hp_res.php");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		ArrayList<String> tags= new ArrayList<String>();
		tags.add("hour");

		ArrayList<String> result=getResult(response,tags);
		return result;
	}

	public static ArrayList<String> getIsReservedIng(String year, String month,
			String day) throws ClientProtocolException, IOException{
		HttpPost request = makeHttpPost("year", year, "month", month, "day", day,
				 "http://210.94.181.168/show_hp_res_ing.php");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		ArrayList<String> tags= new ArrayList<String>();
		tags.add("hour");
		ArrayList<String> result=getResult(response,tags);
		return result;
	}
	*/
    //reservation
    public static ArrayList<String> getIsReservedHpt(String year, String month,
                                                     String day) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("year", year, "month", month, "day", day,
                "http://210.94.181.168/show_hp_res.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("hour");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }
    public static ArrayList<String> getIsReservedIngHpt(String id, String year, String month,
                                                        String day) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "year", year, "month", month, "day", day,
                "http://210.94.181.168/show_hp_res_ing.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("hour");
        tags.add("id");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> getIsReservedBty(String year, String month,
                                                     String day) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("year", year, "month", month, "day", day,
                "http://210.94.181.168/show_bt_res.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("hour");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> getIsReservedIngBty(String id, String year, String month,
                                                        String day) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "year", year, "month", month, "day", day,
                "http://210.94.181.168/show_bt_res_ing.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("hour");
        tags.add("id");
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

    //추가
//	public static String hpReservePossiblity(String year, String month,
//			String day,String time) throws ClientProtocolException, IOException{
//		HttpPost request = makeHttpPost("year", year, "month", month, "day", day, "hour",time,
//				 "http://210.94.181.168/usuable_d_hp.php");
//		HttpClient client = new DefaultHttpClient();
//		ResponseHandler<String> reshandler = new BasicResponseHandler();
//		String result = client.execute(request, reshandler);
//		return result;
//	}
//	public static String btReservePossiblity(String year, String month,
//			String day,String time) throws ClientProtocolException, IOException{
//		HttpPost request = makeHttpPost("year", year, "month", month, "day", day, "hour",time,
//				 "http://210.94.181.168/usuable_d_bt.php");
//		HttpClient client = new DefaultHttpClient();
//		ResponseHandler<String> reshandler = new BasicResponseHandler();
//		String result = client.execute(request, reshandler);
//		return result;
//	}
    //추가
    public static ArrayList<String> hpReservePossiblity(String id, String year, String month,
                                                        String day, String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "year", year, "month", month, "day", day, "hour", time,
                "http://210.94.181.168/usuable_d_hp.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }
    //public static String deleteIsReservedIng (String year, String month, String day, String hour) {

    //뒤로 가기 버튼 눌렀을 때 예약 중 table에서 레코드 삭제
    public static ArrayList<String> deleteIsReservedIngHpt(String year, String month, String day,
                                                           String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("year", year, "month", month, "day", day, "hour", time,
                "http://210.94.181.168/hp_res_back.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> btReservePossiblity(String id, String year, String month,
                                                        String day, String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "year", year, "month", month, "day", day, "hour", time,
                "http://210.94.181.168/usuable_d_bt.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //뒤로 가기 버튼 눌렀을 때 예약 중 table에서 레코드 삭제
    public static ArrayList<String> deleteIsReservedIngBty(String year, String month, String day,
                                                           String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("year", year, "month", month, "day", day, "hour", time,
                "http://210.94.181.168/bt_res_back.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //hospital reserve
    public static ArrayList<String> reserveHospital(String year, String month, String day, String time,
                                                    String pet_name, String id, String user_name, String symptom) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "user_name", user_name,
                "year", year, "month", month, "day", day, "hour", time,
                "symptom", symptom, "http://210.94.181.168/hp_res.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //beauty reserve
    public static ArrayList<String> reserveBeauty(String year, String month, String day, String time,
                                                  String pet_name, String user_name, String id, String toDo) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "user_name", user_name,
                "year", year, "month", month, "day", day, "hour", time,
                "beauty", toDo, "http://210.94.181.168/bt_res.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //hotel reserve
    public static ArrayList<String> reserveHotel(String in_year, String in_month, String in_day, String out_year,
                                                 String out_month, String out_day, String id, String pet_name, String user_name) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "user_name", user_name,
                "in_year", in_year, "in_month", in_month, "in_day", in_day,
                "out_year", out_year, "out_month", out_month, "out_day", out_day,
                "http://210.94.181.168/hotel_res.php");

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //set visit
    public static ArrayList<String> setVisitRsvHsp(String id, String pet_name, String year, String month,
                                                   String day, String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "year", year, "month", month,
                "day", day, "hour", time, "http://210.94.181.168/hp_res_jinryo.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //delete reservation
    public static ArrayList<String> deleteRsvHsp(String id, String pet_name, String year, String month,
                                                 String day, String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "year", year, "month", month,
                "day", day, "hour", time, "http://210.94.181.168/hp_res_del.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> deleteRsvBty(String id, String pet_name, String year, String month,
                                                 String day, String time) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "year", year, "month", month,
                "day", day, "hour", time, "http://210.94.181.168/bt_res_del.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> deleteRsvHtl(String id, String pet_name, String in_year,
                                                 String in_month, String in_day, String out_year, String out_month, String out_day) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "in_year", in_year, "in_month", in_month,
                "in_day", in_day, "out_year", out_year, "out_month", out_month, "out_day", out_day, "http://210.94.181.168/hotel_res_del.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    //view reservation
    public static ArrayList<String> viewRsvHsp(String id) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "http://210.94.181.168/hp_res_check.php");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();

        tags.add("pet_name");
        tags.add("year");
        tags.add("month");
        tags.add("day");
        tags.add("hour");
        tags.add("symptom");
        Log.d("connecDB", "connectDB viewrsvhsp list");
        ArrayList<String> result = getResult(response, tags);
        return result;
    }

    public static ArrayList<String> viewRsvBty(String id) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "http://210.94.181.168/bt_res_check.php");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();

        tags.add("pet_name");
        tags.add("year");
        tags.add("month");
        tags.add("day");
        tags.add("hour");
        tags.add("beauty");
        Log.d("connecDB", "connectDB viewrsvbty list");
        ArrayList<String> result = getResult(response, tags);
        return result;

    }

    public static ArrayList<String> viewRsvHtl(String id) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "http://210.94.181.168/hotel_res_check.php");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();

        tags.add("in_year");
        tags.add("in_month");
        tags.add("in_day");
        tags.add("out_year");
        tags.add("out_month");
        tags.add("out_day");
        tags.add("pet_name");
        tags.add("approval");
        Log.d("connecDB", "connectDB viewrsvhtl list");

        ArrayList<String> result = getResult(response, tags);

        return result;
    }

    //view medical record
    public static ArrayList<String> viewMdcRecord(String id, String pet_name) throws ClientProtocolException, IOException {
        HttpPost request = makeHttpPost("id", id, "pet_name", pet_name, "http://210.94.181.168/pet_info_log_view.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("pet_name");
        tags.add("year");
        tags.add("month");
        tags.add("day");
        tags.add("hour");
        tags.add("symptom");
        Log.d("connecDB", "connectDB viewMdcRecord list");

        ArrayList<String> result = getResult(response, tags);
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

    // //////////post
    // make http post override
    // parameter 1
    private static HttpPost makeHttpPost(String t1, String v1, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        request.setEntity(makeEntity(nameValue));
        return request;
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

    // parameter 3
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    // parameter 4
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String t4, String v4, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        nameValue.add(new BasicNameValuePair(t4, v4));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    // parameter 5
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String t4, String v4, String t5, String v5, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        nameValue.add(new BasicNameValuePair(t4, v4));
        nameValue.add(new BasicNameValuePair(t5, v5));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    // parameter 6
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String t4, String v4, String t5,
                                         String v5, String t6, String v6, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        nameValue.add(new BasicNameValuePair(t4, v4));
        nameValue.add(new BasicNameValuePair(t5, v5));
        nameValue.add(new BasicNameValuePair(t6, v6));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    // parameter 7
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String t4, String v4, String t5,
                                         String v5, String t6, String v6, String t7, String v7, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        nameValue.add(new BasicNameValuePair(t4, v4));
        nameValue.add(new BasicNameValuePair(t5, v5));
        nameValue.add(new BasicNameValuePair(t6, v6));
        nameValue.add(new BasicNameValuePair(t7, v7));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    // parameter 8
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String t4, String v4, String t5,
                                         String v5, String t6, String v6, String t7, String v7, String t8,
                                         String v8, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        nameValue.add(new BasicNameValuePair(t4, v4));
        nameValue.add(new BasicNameValuePair(t5, v5));
        nameValue.add(new BasicNameValuePair(t6, v6));
        nameValue.add(new BasicNameValuePair(t7, v7));
        nameValue.add(new BasicNameValuePair(t8, v8));
        request.setEntity(makeEntity(nameValue));
        return request;
    }

    //parameter 9
    private static HttpPost makeHttpPost(String t1, String v1, String t2,
                                         String v2, String t3, String v3, String t4, String v4, String t5,
                                         String v5, String t6, String v6, String t7, String v7, String t8,
                                         String v8, String t9, String v9, String url) {
        // TODO Auto-generated method stub
        HttpPost request = new HttpPost(url);
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair(t1, v1));
        nameValue.add(new BasicNameValuePair(t2, v2));
        nameValue.add(new BasicNameValuePair(t3, v3));
        nameValue.add(new BasicNameValuePair(t4, v4));
        nameValue.add(new BasicNameValuePair(t5, v5));
        nameValue.add(new BasicNameValuePair(t6, v6));
        nameValue.add(new BasicNameValuePair(t7, v7));
        nameValue.add(new BasicNameValuePair(t8, v8));
        nameValue.add(new BasicNameValuePair(t9, v9));
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

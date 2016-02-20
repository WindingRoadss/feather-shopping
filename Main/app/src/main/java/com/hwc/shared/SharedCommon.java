package com.hwc.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by K on 2016-02-19.
 */
public class SharedCommon {

    public void putPreferences(Context context, String prefName, HashMap<String, String> hashMap, ArrayList<String> keyList)
    {
        SharedPreferences pref;
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        for(String strKey : keyList) {
            editor.putString(strKey, hashMap.get(strKey));
        }
        //editor.putString("Key2", "String 값 저장2");
        editor.commit();
    }

    public HashMap<String, String> getPreferences(Context context, String prefName, ArrayList<String> keyList)
    {
        HashMap<String, String> resultHashMap = new HashMap<String, String>();
        SharedPreferences pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);

        // Key1 태그에 저장되어있는 값을 불러온다. getString(tag, default)
        for(String strKey : keyList) {
            resultHashMap.put(strKey, pref.getString(strKey, ""));
        }

        Log.d("pref", pref.getString("id", ""));

        return resultHashMap;
    }

    public void removePreferences(Context context, String prefName, ArrayList<String> keyList)
    {
        SharedPreferences pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(String strKey : keyList) {
            editor.remove(strKey);
        }
        editor.commit();
    }

    public void clearPreferences(Context context, String prefName)
    {
        SharedPreferences pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}

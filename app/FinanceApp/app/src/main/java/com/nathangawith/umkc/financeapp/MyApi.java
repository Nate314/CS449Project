package com.nathangawith.umkc.financeapp;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.function.Consumer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MyApi {

    private static <T extends Object> Consumer<JSONObject> parse(T resp, Consumer<T> func) {
        return json -> {
            for (Field field : resp.getClass().getFields()) {
                System.out.println(field.getName());
                try {
                    Object o = json.get(field.getName());
                    field.set(resp, o);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            func.accept(resp);
        };
    }

    public static <T extends Object> void postLogin(Context context, T resp, String username, String password, Consumer<T> func) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\",\"%s\":\"%s\"}", "username", username, "password", password);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            resp.getClass().getFields();

            MyHttpClient.post(context, "/auth/login", entity, parse(resp, func), x -> {}, x -> {});
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}

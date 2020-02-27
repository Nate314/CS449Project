package com.nathangawith.umkc.financeapp;

import android.content.Context;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.function.Consumer;

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

    public static <T extends Object> void postLogin(Context context, T resp, String username, String password, Consumer<T> func, Consumer<Throwable> errorFunc) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\",\"%s\":\"%s\"}", "username", username, "password", password);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            resp.getClass().getFields();

            MyHttpClient.post(context, "/auth/login", entity, parse(resp, func), x -> {}, errorFunc, x -> {});
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public static <T extends Object> void postAddAccount(Context context, T resp, String accountDescription, Consumer<T> func, Consumer<T> errorFunc) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\"}", "Description", accountDescription);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            resp.getClass().getFields();

            MyHttpClient.post(context, "/settings/account/add", entity, parse(resp, func), x -> {}, x -> {}, parse(resp, errorFunc));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public static <T extends Object> void postAddCategory(Context context, T resp, boolean income, String accountDescription, Consumer<T> func, Consumer<T> errorFunc) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\"}", "Description", accountDescription);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            resp.getClass().getFields();

            String type = income ? MyConstants.INCOME : MyConstants.EXPENSE;
            MyHttpClient.post(context, "/settings/category/add?categoryType=" + type, entity, parse(resp, func), x -> {}, x -> {}, parse(resp, errorFunc));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}

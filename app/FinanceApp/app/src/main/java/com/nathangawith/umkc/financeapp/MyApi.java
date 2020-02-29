package com.nathangawith.umkc.financeapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MyApi {

    private static <T extends Object> Consumer<JSONObject> parse(T resp, Consumer<T> func) {
        return json -> {
            for (Field field : resp.getClass().getFields()) {
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

    private static <T extends Object> Consumer<JSONArray> parseCollection(Class<T> respType, Consumer<Collection<T>> func) {
        return jsonArray -> {
            try {
                ArrayList<T> result = new ArrayList<T>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    T resp = respType.newInstance();
                    JSONObject json = jsonArray.getJSONObject(i);
                    for (Field field : resp.getClass().getFields()) {
                        try {
                            Object o = json.get(field.getName());
                            field.set(resp, o);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    result.add(resp);
                }
                func.accept(result);
            } catch (JSONException exception) {
                exception.printStackTrace();
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            } catch (InstantiationException exception) {
                exception.printStackTrace();
            }
        };
    }

    public static void postLogin(Context context, TokenResponseDto resp, String username, String password, Consumer<TokenResponseDto> func, Consumer<Throwable> errorFunc) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\",\"%s\":\"%s\"}", "username", username, "password", password);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            resp.getClass().getFields();

            MyHttpClient.post(context, "/auth/login", entity, parse(resp, func), x -> {}, errorFunc, x -> {});
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public static void postAddAccount(Context context, GenericResponse resp, String accountDescription, Consumer<GenericResponse> func, Consumer<GenericResponse> errorFunc) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\"}", "Description", accountDescription);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            resp.getClass().getFields();

            MyHttpClient.post(context, "/settings/account/add", entity, parse(resp, func), x -> {}, x -> {}, parse(resp, errorFunc));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public static void getAllAccounts(Context context, Consumer<Collection<DBAccount>> func, Consumer<GenericResponse> errorFunc) {
        MyHttpClient.get(context, "/settings/accounts/all", x -> {}, parseCollection(DBAccount.class, func), x -> {}, parse(new GenericResponse(), errorFunc));
    }

    public static void postAddCategory(Context context, GenericResponse resp, boolean income, String accountDescription, Consumer<GenericResponse> func, Consumer<GenericResponse> errorFunc) {
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

    public static void getAllCategories(Context context, boolean income, Consumer<Collection<DBCategory>> func, Consumer<GenericResponse> errorFunc) {
        String type = income ? MyConstants.INCOME : MyConstants.EXPENSE;
        MyHttpClient.get(context, "/settings/categories/all?categoryType=" + type, x -> {}, parseCollection(DBCategory.class, func), x -> {}, parse(new GenericResponse(), errorFunc));
    }
}

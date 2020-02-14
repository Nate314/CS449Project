package com.nathangawith.umkc.financeapp;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MyHttpClient {

    private static final String BASE_URL = "http://10.0.0.26:9090";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        client.get(url, params, responseHandler);
    }

    public static void post(Context context, String url, ByteArrayEntity entity, Consumer<JSONObject> obCallback, Consumer<JSONArray> arrCallback, Consumer<Throwable> errCallback) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(context, url, entity, "application/json", getHandler(obCallback, arrCallback, errCallback));
    }

    public static void put(Context context, String url, ByteArrayEntity entity, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.put(context, url, entity, "application/json", responseHandler);
    }

    public static void delete(Context context, String url, ByteArrayEntity entity, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.delete(context, url, entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static AsyncHttpResponseHandler getHandler(Consumer<JSONObject> obCallback, Consumer<JSONArray> arrCallback, Consumer<Throwable> errCallback) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("asd", "---------------- this is response : " + response);

                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    System.out.println(serverResp.toString());
                    System.out.println(serverResp.get("token"));
                    obCallback.accept(serverResp);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                arrCallback.accept(timeline);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String msg, Throwable throwable) {
                System.out.println("-------- Failure --------");
                System.out.println(statusCode);
                for (Header header : headers) {
                    System.out.printf("%s: %s\n", header.getName(), header.getValue());
                }
                System.out.println(msg);
                System.out.println(throwable.getMessage());
                System.out.println("-------- Failure --------");
                errCallback.accept(throwable);
            }
        };
    }

}

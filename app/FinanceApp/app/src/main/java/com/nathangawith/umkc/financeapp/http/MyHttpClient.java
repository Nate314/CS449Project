package com.nathangawith.umkc.financeapp.http;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nathangawith.umkc.financeapp.constants.MyState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MyHttpClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, Consumer<JSONObject> obCallback, Consumer<JSONArray> arrCallback, Consumer<Throwable> errCallback, Consumer<JSONObject> errObCallback) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        client.addHeader("Authorization", String.format("Bearer %s", MyState.TOKEN));
        client.get(context, url, null, getHandler(obCallback, arrCallback, errCallback, errObCallback));
    }

    public static void post(Context context, String url, ByteArrayEntity entity, Consumer<JSONObject> obCallback, Consumer<JSONArray> arrCallback, Consumer<Throwable> errCallback, Consumer<JSONObject> errObCallback) {
        client.setMaxRetriesAndTimeout(3, 10);
        try {
            url = getAbsoluteUrl(url);
            System.out.println("Sending request to: " + url);
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.addHeader("Authorization", String.format("Bearer %s", MyState.TOKEN));
            client.post(context, url, entity, "application/json", getHandler(obCallback, arrCallback, errCallback, errObCallback));
        } catch (Exception ex) {
            errCallback.accept(null);
        }
    }

    public static void put(Context context, String url, ByteArrayEntity entity, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.addHeader("Authorization", String.format("Bearer %s", MyState.TOKEN));
        client.put(context, url, entity, "application/json", responseHandler);
    }

    public static void delete(Context context, String url, ByteArrayEntity entity, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        System.out.println("Sending request to: " + url);
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.addHeader("Authorization", String.format("Bearer %s", MyState.TOKEN));
        client.delete(context, url, entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return MyState.ROOT_API_URL + relativeUrl;
    }

    private static AsyncHttpResponseHandler getHandler(Consumer<JSONObject> obCallback, Consumer<JSONArray> arrCallback, Consumer<Throwable> errCallback, Consumer<JSONObject> errObCallback) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("asd", "---------------- this is response : " + response);

                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    System.out.println(serverResp.toString());
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

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                System.out.println("-------- obj Failure --------");
                System.out.println(statusCode);
                if (headers != null && headers.length > 0) {
                    for (Header header : headers) {
                        System.out.printf("%s: %s\n", header.getName(), header.getValue());
                    }
                }
                System.out.println(obj.toString());
                System.out.println(throwable.getMessage());
                System.out.println("-------- obj Failure --------");
                errObCallback.accept(obj);
            }
        };
    }

}

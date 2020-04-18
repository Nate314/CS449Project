package com.nathangawith.umkc.financeapp.http;

import android.content.Context;

import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.dtos.TransactionRequest;
import com.nathangawith.umkc.financeapp.dtos.GenericResponse;
import com.nathangawith.umkc.financeapp.dtos.ReportResponseDto;
import com.nathangawith.umkc.financeapp.dtos.TokenResponseDto;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MyApi {

    /**
     * "Nathan", ' ', 10 -> "    Nathan"
     * @param str string to right align
     * @param c character to fill on the left
     * @param length final length of the string
     * @return string with padding on the left
     */
    private static String fillLeft(String str, char c, int length) {
        String result = "";
        int extra = length - str.length();
        for (int i = 0; i < extra; i++) result += c;
        return result + str;
    }

    /**
     * @param date date to convert
     * @return string representation of date in the form YYYY-MM-DD
     */
    public static String dateToString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.format("%s-%s-%s", year, fillLeft(month + "", '0', 2), fillLeft(day + "", '0', 2));
    }

    private static <T extends Object> Consumer<JSONObject> parse(T resp, Consumer<T> func) {
        return json -> {
            System.out.println("-------- Parsing --------");
            for (Field field : resp.getClass().getFields()) {
                try {
                    Object o = json.get(field.getName());
                    System.out.println(o.toString());
                    System.out.println(o.getClass());
//                    System.out.println(o.getClass().equals("org.json.JSONArray"));
                    if (o.toString().charAt(0) == '[') {
//                        System.out.println("true");
//                        System.out.println(field.getType());
//                        System.out.println(field.getGenericType().getTypeName());
                        Class<T> collectionClass = (Class<T>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
//                        System.out.println(collectionClass);
//                        System.out.println(collectionClass.getClass().getName());
//                        System.out.println(collectionClass.getName());
                        parseCollection(collectionClass, x -> {
                            try {
                                field.set(resp, (List) x);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }).accept((JSONArray) o);
                    } else {
                        field.set(resp, o);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("-------- Parsing --------");
            System.out.println(resp.toString());
            System.out.println(resp == null);
            System.out.println(func == null);
            // System.out.println(resp != null ? ((GenericResponse) resp).response : "RESPONSE IS NULL");
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

    public static void postTransaction(Context context, String type, TransactionRequest transaction, Consumer<GenericResponse> func, Consumer<GenericResponse> errorFunc) {
        GenericResponse resp = new GenericResponse();
        if (type.equals(MyConstants.INCOME) || type.equals(MyConstants.EXPENSE) || type.equals(MyConstants.TRANSFER_ACCOUNT)|| type.equals(MyConstants.TRANSFER_CATEGORY)) {
            try {
                String jsonObjectString = String.format("{\"%s\":%s,\"%s\":%s,\"%s\":\"%s\",\"%s\":%s,\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}",
                        "AccountID", transaction.AccountID,
                        "CategoryID", transaction.CategoryID,
                        "AccountToID", transaction.AccountToID,
                        "AccountFromID", transaction.AccountFromID,
                        "CategoryToID", transaction.CategoryToID,
                        "CategoryFromID", transaction.CategoryFromID,
                        "Description", transaction.Description,
                        "Amount", transaction.Amount,
                        "Date", dateToString(transaction.Date));
                ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
                System.out.println("-------- Request: --------");
                System.out.println(jsonObjectString);
                MyHttpClient.post(context, "/transactions/add?transactionType=" + type, entity, parse(resp, func), x -> {}, x -> {}, parse(resp, errorFunc));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        } else {
            resp.response = "Only Income/Expense/Transfer[Account/Category] are valid options";
            parse(resp, errorFunc);
        }
    }

    public static void getTotal(Context context, Consumer<GenericResponse> func, Consumer<GenericResponse> errorFunc) {
        MyHttpClient.get(context, "/register/total", parse(new GenericResponse(), func), x -> {}, x -> {}, parse(new GenericResponse(), errorFunc));
    }

    public static void getTransactions(Context context, Consumer<Collection<TransactionDto>> func, Consumer<GenericResponse> errorFunc) {
        MyHttpClient.get(context, "/register/transactions", x -> {}, parseCollection(TransactionDto.class, func), x -> {}, parse(new GenericResponse(), errorFunc));
    }

    public static void postReport(Context context, Date StartDate, Date EndDate, String Breakpoint, String Type, Consumer<ReportResponseDto> func, Consumer<GenericResponse> errorFunc) {
        try {
            String jsonObjectString = String.format("{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}",
                    "StartDate", dateToString(StartDate),
                    "EndDate", dateToString(EndDate),
                    "Breakpoint", Breakpoint,
                    "Type", Type);
            ByteArrayEntity entity = new ByteArrayEntity(jsonObjectString.getBytes("UTF-8"));
            ReportResponseDto resp = new ReportResponseDto();
            GenericResponse errResp = new GenericResponse();
            System.out.println("-------- Request: --------");
            System.out.println(jsonObjectString);
            MyHttpClient.post(context, "/report/report", entity, parse(resp, func), x -> {}, x -> {}, parse(errResp, errorFunc));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}

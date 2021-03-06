package com.nathangawith.umkc;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Algorithms {

	// https://github.com/patrickfav/bcrypt
	public static String encryptPassword(String username, String password) {
		String usernameChars = username.substring(0, Math.min(username.length(), 16));
		String passwordChars = password.substring(0, Math.min(password.length(), 16 - usernameChars.length()));
		String paddingChars = "qVHD8w*^g@T*2X3Y".substring(usernameChars.length() + passwordChars.length());
		byte[] salt = (usernameChars + passwordChars + paddingChars).getBytes();
		byte[] bcryptHashString = BCrypt.withDefaults().hash(12, salt, password.getBytes(StandardCharsets.UTF_8));
		return new String(bcryptHashString);
	}
	
	/**
	 * uses reflection to convert java object to json string
	 * @param object object to stringify
	 * @return JSON.stringified object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> String toJSONObject(T object) {
		String result = "{";
        for (Field field : object.getClass().getFields()) {
            try {
                Object o = field.get(object);
                if (o != null) {
                	if (field.getType().getName().equals("java.util.List")) {
                		result += String.format("\"%s\":%s,", field.getName(), toJSONArray((Collection<Object>) o));
                	} else {
                        String format = field.getType().isPrimitive() || field.getType().toString().toUpperCase().contains("INTEGER") ? "%s" : "\"%s\"";
                        String value = field.getName().toUpperCase().contains("DATE")
                        	? String.format(format, Algorithms.dateToString((Date) o)) : String.format(format, o.toString());
                    	result += String.format("\"%s\":%s,", field.getName(), value);
                	}
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (object.getClass().getFields().length > 0)
        	result = result.substring(0, result.length() - 1);
		result += "}";
		System.out.println(result);
    	return result;
	}

	/**
	 * uses reflection to convert java collection of objects to json string
	 * @param array collection to stringify
	 * @return JSON.stringified array
	 */
	public static <T extends Object> String toJSONArray(Collection<T> array) {
		String result = "[";
		for (T object : array)
			result += String.format("%s,", Algorithms.toJSONObject(object));
		if (array.size() > 0)
			result = result.substring(0, result.length() - 1);
		result += "]";
    	return result;
	}
	
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
	    TimeZone tz = TimeZone.getDefault();
	    Calendar tzcal = GregorianCalendar.getInstance(tz);
	    int offsetInMillis = tz.getOffset(tzcal.getTimeInMillis()) * -1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MILLISECOND, offsetInMillis);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return String.format("%s-%s-%s", year, fillLeft(month + "", '0', 2), fillLeft(day + "", '0', 2));
	}

	/**
	 * returns the money string version of the double amount
	 * @param amount amount of money
	 * @return string version
	 */
    public static String formatAsMoney(double amount) {
        boolean negativeBalance = amount < 0;
        amount = negativeBalance ? -1 * amount : amount;
        String stramt = "" + amount;
        String[] parts = stramt.split("\\.");
        String dollars = parts.length > 0 ? parts[0] : "0";
        String cents = parts.length > 1 ? parts[1] : "00";
        cents = cents.substring(0, Math.min(2, cents.length()));
        cents = cents.length() <= 1 ? cents + "0" : cents;
        cents = cents.length() <= 1 ? cents + "0" : cents; 
        return String.format("%s$%s.%s", negativeBalance ? "-" : "", dollars, cents);
    }

	// used to collect objects into a list of strings
	private static List<String> concat(List<String> list, String str)
		{ list.add(str); return list; }
	private static String objectToString(Object o) { return o == null ? null : o.toString(); }
	public static List<String> params(Object o1)
		{ return concat(new ArrayList<String>(), objectToString(o1)); }
	public static List<String> params(Object o1, Object o2)
		{ return concat(params(o1), objectToString(o2)); }
	public static List<String> params(Object o1, Object o2, Object o3)
		{ return concat(params(o1, o2), objectToString(o3)); }
	public static List<String> params(Object o1, Object o2, Object o3, Object o4)
		{ return concat(params(o1, o2, o3), objectToString(o4)); }
	public static List<String> params(Object o1, Object o2, Object o3, Object o4, Object o5)
		{ return concat(params(o1, o2, o3, o4), objectToString(o5)); }
	public static List<String> params(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6)
		{ return concat(params(o1, o2, o3, o4, o5), objectToString(o6)); }
	public static List<String> params(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7)
		{ return concat(params(o1, o2, o3, o4, o5, o6), objectToString(o7)); }
}

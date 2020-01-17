package com.intl.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intl.base.IntlContext;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public class PrefUtils {
    private static final String KEY_PREF = "SF";

    public PrefUtils() {
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences("SF", 0);
    }

    public static void putObject(String key, Object value) {
        putObject(IntlContext.getApplicationContext(), key, value);
    }

    public static void putObject(Context context, String key, Object value) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreference(context);
        sharedPreferences.edit().putString(key, gson.toJson(value)).apply();
    }

    public static <T> T getObject(String key, Class<T> tClass) {
        return getObject(IntlContext.getApplicationContext(), key, tClass);
    }

//    public static List<DashBoardItem> getListObjectDB(String key) {
//        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
//        String dataRaw = sharedPreferences.getString(key, "");
//        Type listType = (new TypeToken<List<DashBoardItem>>() {
//        }).getType();
//        return (List)(new Gson()).fromJson(dataRaw, listType);
//    }

    public static <T> T getObject(Context context, String key, Class<T> tClass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SF", 0);
        String dataRaw = sharedPreferences.getString(key, "");
        Gson gson = new Gson();
        return gson.fromJson(dataRaw, tClass);
    }

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        return sharedPreferences.getString(key, "");
    }

    public static void putLong(String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        return sharedPreferences.getLong(key, 0L);
    }

    public static void addSeenId(List<String> list) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        sharedPreferences.edit().putString("SeenId", gson.toJson(list)).apply();
    }

    public static List<String> getListSeenId() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        String s = sharedPreferences.getString("SeenId", "[]");
        String[] strings = (String[])gson.fromJson(s, String[].class);
        return Arrays.asList(strings);
    }

    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreference(IntlContext.getApplicationContext());
        return sharedPreferences.getInt(key, 0);
    }

    public static void putBoolean(String key, boolean value) {
        putBoolean(IntlContext.getApplicationContext(), key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SF", 0);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(IntlContext.getApplicationContext(), key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SF", 0);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}

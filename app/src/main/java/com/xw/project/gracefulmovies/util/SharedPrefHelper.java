package com.xw.project.gracefulmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences操作类
 * <p/>
 * Created by woxingxiao on 2017-02-13.
 */
public final class SharedPrefHelper {

    private static SharedPreferences mSharePref;

    public static void init(Context context) {
        mSharePref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static void putInt(String key, int value) {
        mSharePref.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return mSharePref.getInt(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        mSharePref.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defaultValue) {
        return mSharePref.getFloat(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        mSharePref.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mSharePref.getBoolean(key, defaultValue);
    }

    public static void putString(String key, String value) {
        mSharePref.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return mSharePref.getString(key, defaultValue);
    }

}

package com.xw.project.gracefulmovies.util;

import android.content.Context;

import com.xw.project.gracefulmovies.R;

import static com.xw.project.gracefulmovies.util.SharedPrefHelper.getString;

/**
 * 方便app常用SharedPreferences直接操作类
 * <p/>
 * Created by woxingxiao on 2017-02-16.
 */
public final class PrefUtil {

    public static boolean checkFirstTime(Context context) {
        return SharedPrefHelper.getBoolean(context.getString(R.string.key_first_time), true);
    }

    public static void setNotFirstTime(Context context) {
        SharedPrefHelper.putBoolean(context.getString(R.string.key_first_time), false);
    }

    public static int[] getDayNightModeStartTime(Context context, boolean isDay) {
        int[] time = new int[2];
        String timeStr;
        if (isDay) {
            timeStr = getString(context.getString(R.string.key_day_mode_time), "8:00");
        } else {
            timeStr = getString(context.getString(R.string.key_night_mode_time), "18:00");
        }
        String[] str = timeStr.split(":");
        time[0] = Integer.valueOf(str[0]);
        time[1] = Integer.valueOf(str[1]);
        return time;
    }

    public static boolean isAutoDayNightMode(Context context) {
        return SharedPrefHelper.getBoolean(context.getString(R.string.key_auto_day_night), true);
    }

    public static void setAutoDayNightMode(Context context, boolean auto) {
        SharedPrefHelper.putBoolean(context.getString(R.string.key_auto_day_night), auto);
    }

    public static String getCity(Context context) {
        return SharedPrefHelper.getString(context.getString(R.string.key_city), "成都市");
    }

    public static String getCityShort(Context context) {
        return Util.trimCity(getCity(context));
    }

    public static void setCity(Context context, String city) {
        SharedPrefHelper.putString(context.getString(R.string.key_city), city);
    }

    public static void clearCity(Context context) {
        SharedPrefHelper.putString(context.getString(R.string.key_city), "");
        SharedPrefHelper.putString(context.getString(R.string.key_upper_city), "");
    }

    public static String getUpperCity(Context context) {
        return SharedPrefHelper.getString(context.getString(R.string.key_upper_city), "");
    }

    public static void setUpperCity(Context context, String city) {
        SharedPrefHelper.putString(context.getString(R.string.key_upper_city), city);
    }
}

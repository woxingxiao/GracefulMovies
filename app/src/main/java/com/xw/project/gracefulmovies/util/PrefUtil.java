package com.xw.project.gracefulmovies.util;

import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;

import static com.xw.project.gracefulmovies.util.SharedPrefHelper.getString;

/**
 * 方便app常用SharedPreferences直接操作类
 * <p/>
 * Created by woxingxiao on 2017-02-16.
 */
public final class PrefUtil {

    public static boolean checkFirstTime() {
        return SharedPrefHelper.getBoolean(GMApplication.sGMApplication.getString(R.string.key_first_time), true);
    }

    public static void setNotFirstTime() {
        SharedPrefHelper.putBoolean(GMApplication.sGMApplication.getString(R.string.key_first_time), false);
    }

    public static int[] getDayNightModeStartTime(boolean isDay) {
        int[] time = new int[2];
        String timeStr;
        if (isDay) {
            timeStr = getString(GMApplication.sGMApplication.getString(R.string.key_day_mode_time), "8:00");
        } else {
            timeStr = getString(GMApplication.sGMApplication.getString(R.string.key_night_mode_time), "18:00");
        }
        String[] str = timeStr.split(":");
        time[0] = Integer.valueOf(str[0]);
        time[1] = Integer.valueOf(str[1]);
        return time;
    }

    public static boolean isAutoDayNightMode() {
        return SharedPrefHelper.getBoolean(GMApplication.sGMApplication.getString(R.string.key_auto_day_night), true);
    }

    public static void setAutoDayNightMode(boolean auto) {
        SharedPrefHelper.putBoolean(GMApplication.sGMApplication.getString(R.string.key_auto_day_night), auto);
    }

    public static String getCity() {
        return SharedPrefHelper.getString(GMApplication.sGMApplication.getString(R.string.key_city), "成都市");
    }

    public static String getCityShort() {
        return Util.trimCity(getCity());
    }

    public static void setCity(String city) {
        SharedPrefHelper.putString(GMApplication.sGMApplication.getString(R.string.key_city), city);
    }

    public static void clearCity() {
        SharedPrefHelper.putString(GMApplication.sGMApplication.getString(R.string.key_city), "");
        SharedPrefHelper.putString(GMApplication.sGMApplication.getString(R.string.key_upper_city), "");
    }

    public static String getUpperCity() {
        return SharedPrefHelper.getString(GMApplication.sGMApplication.getString(R.string.key_upper_city), "");
    }

    public static void setUpperCity(String city) {
        SharedPrefHelper.putString(GMApplication.sGMApplication.getString(R.string.key_upper_city), city);
    }
}

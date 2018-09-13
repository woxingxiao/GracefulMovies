package com.xw.project.gracefulmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xw.project.gracefulmovies.GMApplication;

public final class NetWorkUtils {

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = getConnectivityManager();
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager manager = getConnectivityManager();
        if (manager != null) {
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return info != null && info.isConnected();
        }
        return false;
    }

    public static boolean isMobileConnected() {
        ConnectivityManager manager = getConnectivityManager();
        if (manager != null) {
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return info != null && info.isConnected();
        }
        return false;
    }

    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) GMApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
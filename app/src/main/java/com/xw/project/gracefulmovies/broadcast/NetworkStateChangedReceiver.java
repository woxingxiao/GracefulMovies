package com.xw.project.gracefulmovies.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.xw.project.gracefulmovies.util.NetWorkUtils;

public class NetworkStateChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetWorkUtils.isNetworkAvailable()) {
            Toast.makeText(context, "网络连接已断开，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }
}
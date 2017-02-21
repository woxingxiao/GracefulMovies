package com.xw.project.gracefulmovies.view.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.util.Logy;
import com.xw.project.gracefulmovies.util.PrefUtil;

/**
 * 定位服务
 * <p/>
 * Created by woxingxiao on 2017-02-19.
 */
public class LocationService extends Service {

    private AMapLocationClient mLocationClient;

    public static void start(Context context) {
        context.startService(new Intent(context, LocationService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Logy.w("LocationService", "================ onStart ===============");

        initLocation();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        mLocationClient.setLocationListener(locationListener);
        // 开始定位
        mLocationClient.startLocation();
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location == null) {
                Logy.w("LocationService", "------------定位失败，继续定位");
                return;
            }
            //解析定位结果
            if (location.getErrorCode() != 0) {
                Logy.w("LocationService", "------------定位失败：" + location.getLocationDetail());

                stopLocation();
                return;
            }
            if (location.getCity() != null && !location.getCity().isEmpty()) {
                String city = PrefUtil.getCity(LocationService.this);
                String newCity = trimCity(location.getCity());
                Logy.w("LocationService", "------------定位成功：" + location.getCity() + "，" + newCity);

                if (!city.equals(newCity)) {
                    PrefUtil.setCity(LocationService.this, newCity);
                    sendBroadcast(new Intent(getString(R.string.action_locate_succeed)));
                }

                stopLocation();
            } else {
                Logy.w("LocationService", "------------定位失败，无城市数据");
            }
        }
    };

    private String trimCity(String city) {
        if (city.endsWith("市")) {
            return city.substring(0, city.lastIndexOf("市"));
        }
        if (city.endsWith("区")) {
            return city.substring(0, city.lastIndexOf("区"));
        }
        if (city.endsWith("县")) {
            return city.substring(0, city.lastIndexOf("县"));
        }
        if (city.endsWith("镇")) {
            return city.substring(0, city.lastIndexOf("镇"));
        }
        if (city.endsWith("乡")) {
            return city.substring(0, city.lastIndexOf("乡"));
        }

        return city;
    }

    private void stopLocation() {
        mLocationClient.unRegisterLocationListener(locationListener);
        mLocationClient.stopLocation();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logy.w("LocationService", "=============== onDestroy ==============");

        mLocationClient.onDestroy();
    }
}

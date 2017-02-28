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

    // 直辖市、省会城市
    private static final String CAPITAL_CITIES = "北京，天津，上海，重庆，石家庄，太原，呼和浩特，沈阳，" +
            "长春，哈尔滨，南京，杭州，合肥，福州，南昌，济南，郑州，武汉，长沙，广州，南宁，海口，成都，" +
            "贵阳，昆明，西安，兰州，西宁，拉萨，银川，乌鲁木齐";

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
            if (locationListener == null)
                return;
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
                String newCity = location.getCity();

                boolean diffUpper = !newCity.equals(PrefUtil.getUpperCity(LocationService.this));
                if (diffUpper)
                    PrefUtil.setUpperCity(LocationService.this, newCity);

                /**
                 * 非直辖市和省会城市，使用定位城市的下级城市
                 */
                if (!CAPITAL_CITIES.contains(newCity)) {
                    newCity = location.getDistrict();
                }
                Logy.w("LocationService", "------------定位成功：" + location.getCity() + "，" + newCity);

                if (diffUpper && !newCity.equals(PrefUtil.getCity(LocationService.this))) {
                    PrefUtil.setCity(LocationService.this, newCity);
                    sendBroadcast(new Intent(getString(R.string.action_locate_succeed)));
                    Logy.w("LocationService", "-----------------sendBroadcast locate succeed-----------------");
                }

                stopLocation();
            } else {
                Logy.w("LocationService", "------------定位失败，无城市数据");
            }
        }
    };

    private void stopLocation() {
        mLocationClient.unRegisterLocationListener(locationListener);
        mLocationClient.stopLocation();
        locationListener = null;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logy.w("LocationService", "=============== onDestroy ==============");

        mLocationClient.onDestroy();
    }
}

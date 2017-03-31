package com.xw.project.gracefulmovies.view.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.NetLocResult;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.server.ApiSubscriber;
import com.xw.project.gracefulmovies.util.Logy;
import com.xw.project.gracefulmovies.util.PrefUtil;
import com.xw.project.gracefulmovies.util.Util;

import java.util.List;

/**
 * 定位服务
 * <p>
 * 由于本项目对位置精度要求不高，故采用网络接口转换经纬度得到位置信息，既减小apk大小，又可适配所有手机
 * 转换api来自阿里地图开发平台（大厂表示你们随便用，挤爆算我输<(￣︶￣)>）：
 * http://gc.ditu.aliyun.com/regeocoding?l=39.938133,116.395739&type=010
 * <p>
 * 手机GPS采集的是WGS84坐标系，需要通过偏移算法转为国测局的GCJ02坐标（俗称火星坐标，貌似国产地图除百度外都是
 * 这个坐标系），算法来源地址：
 * https://gist.github.com/jp1017/71bd0976287ce163c11a7cb963b04dd8，感谢原作者
 * <p/>
 * Created by woxingxiao on 2017-03-01.
 */
public class LocationService extends Service {

    // 直辖市、省会城市
    private static final String CAPITAL_CITIES = "北京，天津，上海，重庆，石家庄，太原，呼和浩特，沈阳，" +
            "长春，哈尔滨，南京，杭州，合肥，福州，南昌，济南，郑州，武汉，长沙，广州，南宁，海口，成都，" +
            "贵阳，昆明，西安，兰州，西宁，拉萨，银川，乌鲁木齐";
    private static final double pi = 3.1415926535897932384626; // π
    private static final double a = 6378245.0; // 长半轴
    private static final double ee = 0.00669342162296594323; // 扁率

    private LocationManager mLocationManager;
    private ApiSubscriber<NetLocResult> mSubscriber;

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

    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);

        String locationProvider;
        /**
         * 如果首选GPS定位，会存在这种情况，上次GPS启动采集数据在A地，本次在B地需要定位，但用户恰好在室内无
         * GPS信号，只好使用上次定位数据，就出现了地区级偏差。而网络定位则更具有实时性，在精度要求不高以及室内
         * 使用场景更多的前提下，首选网络定位
         */
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER; // 首选网络定位
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            locationProvider = LocationManager.PASSIVE_PROVIDER;
        }

        if (mLocationListener != null)
            mLocationManager.requestLocationUpdates(locationProvider, 2000, 10, mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (mLocationListener == null)
                return;
            if (location == null)
                return;

            if (mSubscriber != null && mSubscriber.isUnsubscribed())
                mSubscriber.unsubscribe();
            mSubscriber = new ApiSubscriber<NetLocResult>() {
                @Override
                public void onNext(NetLocResult netLocResult) {

                    if (netLocResult.getUpperCity() != null && !netLocResult.getUpperCity().isEmpty()) {
                        String newCity = netLocResult.getUpperCity();

                        boolean diffUpper = !newCity.equals(PrefUtil.getUpperCity());
                        if (diffUpper)
                            PrefUtil.setUpperCity(newCity);

                        /**
                         * 非直辖市和省会城市，使用定位城市的下级城市
                         */
                        if (!CAPITAL_CITIES.contains(Util.trimCity(newCity))) {
                            newCity = netLocResult.getCity();
                        }
                        Logy.i("LocationService", "------------定位成功：" + netLocResult.getUpperCity() + "，" + newCity);

                        if (diffUpper && !newCity.equals(PrefUtil.getCity())) {
                            PrefUtil.setCity(newCity);
                            sendBroadcast(new Intent(getString(R.string.action_locate_succeed)));
                            Logy.w("LocationService", "-----------------sendBroadcast locate succeed-----------------");
                        }
                    }

                }

                @Override
                protected void onError(String msg) {
                    Toast.makeText(LocationService.this, "定位失败，" + msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinally() {
                    stopLocation();
                }
            };

            Logy.i("LocationService", "------------ 原生经纬度：" + location.getLatitude() + "," + location.getLongitude());
            double[] latLng = WGS84ToGCJ02(location.getLatitude(), location.getLongitude());
            Logy.i("LocationService", "---------- 转换后经纬度：" + latLng[0] + "," + latLng[1]);

            ApiHelper.loadNetLoc(latLng[0] + "," + latLng[1])
                    .subscribe(mSubscriber);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void stopLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
            ApiHelper.releaseNetLocApi();
        }

        stopSelf();
    }

    /**
     * WGS84转GCJ02(火星坐标系)
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 火星坐标数组
     */
    private double[] WGS84ToGCJ02(double lat, double lng) {
        if (outOfChina(lat, lng)) {
            return new double[]{lat, lng};
        }
        double dLat = transformLat(lat - 35.0, lng - 105.0);
        double dLng = transformLng(lat - 35.0, lng - 105.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLng = lng + dLng;
        return new double[]{mgLat, mgLng};
    }

    /**
     * 纬度转换
     */
    private double transformLat(double lat, double lng) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换
     */
    private double transformLng(double lat, double lng) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 * Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    private boolean outOfChina(double lat, double lng) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        } else if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logy.w("LocationService", "=============== onDestroy ==============");
    }

}

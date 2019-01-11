package com.xw.project.gracefulmovies.data.api.service;

import com.xw.project.gracefulmovies.data.ao.ReGeoResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 经纬度逆转地点
 * <p>
 * Created by woxingxiao on 2019-01-10.
 */
public interface ReGeoService {

    @GET("geocode/regeo?key=3a567f0e2d856cbd164f8dbd9582f498")
    Observable<ReGeoResult> reGeoGet(@Query("location") String latLng);
}

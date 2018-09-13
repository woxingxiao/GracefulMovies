package com.xw.project.gracefulmovies.data.api.service;

import com.xw.project.gracefulmovies.data.ao.NetLocResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 网络转换定位接口
 * <p>
 * Created by woxingxiao on 2018-08-09.
 */
public interface NetLocService {

    @GET("regeocoding?type=010")
    Observable<NetLocResult> netLocGet(@Query("l") String latLng);
}

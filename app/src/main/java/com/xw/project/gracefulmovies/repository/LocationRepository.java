package com.xw.project.gracefulmovies.repository;

import com.xw.project.gracefulmovies.data.api.ApiClient;
import com.xw.project.gracefulmovies.data.api.service.NetLocService;
import com.xw.project.gracefulmovies.data.ao.NetLocResult;

import io.reactivex.Observable;

/**
 * <p>
 * Created by woxingxiao on 2018-08-18.
 */
public class LocationRepository {

    private NetLocService mService;

    public Observable<NetLocResult> fetchLocationInfo(String latLng) {
        if (mService == null) {
            mService = new ApiClient().createApi("http://gc.ditu.aliyun.com/", NetLocService.class);
        }
        return mService.netLocGet(latLng);
    }
}

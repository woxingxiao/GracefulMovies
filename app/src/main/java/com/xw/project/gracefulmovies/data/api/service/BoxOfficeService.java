package com.xw.project.gracefulmovies.data.api.service;

import com.xw.project.gracefulmovies.data.ao.bridge.ModelBridge;
import com.xw.project.gracefulmovies.data.db.entity.BoxOfficeEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * 票房接口
 * <p>
 * Created by woxingxiao on 2018-08-17.
 */
public interface BoxOfficeService {

    @GET("?appid=${your app id}")
    @Headers("Accept-Encoding:*")
    Observable<ModelBridge<List<BoxOfficeEntity>>> fetchBoxOfficeGet();
}

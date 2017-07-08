package com.xw.project.gracefulmovies.server.api;


import com.xw.project.gracefulmovies.model.BoxOfficeResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 票房接口
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public interface BoxOfficeApi {

    @GET("578-2")
    Observable<BoxOfficeResult> dayBoxOfficeGet(@Query("showapi_appid") String appId,
                                                @Query("showapi_sign") String apiKey);

}

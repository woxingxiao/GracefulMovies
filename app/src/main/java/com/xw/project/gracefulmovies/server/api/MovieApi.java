package com.xw.project.gracefulmovies.server.api;

import com.xw.project.gracefulmovies.model.MovieData;
import com.xw.project.gracefulmovies.model.MovieSearchModel;
import com.xw.project.gracefulmovies.model.RequestResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 电影列表接口
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
public interface MovieApi {

    @GET("onebox/movie/pmovie")
    Observable<RequestResult<MovieData>> movieInfoGet(@Query("key") String key, @Query("city") String city);

    @GET("onebox/movie/video")
    Observable<RequestResult<MovieSearchModel>> movieSearchGet(@Query("key") String key, @Query("q") String name);
}

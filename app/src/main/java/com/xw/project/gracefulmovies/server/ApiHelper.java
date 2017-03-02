package com.xw.project.gracefulmovies.server;


import com.xw.project.gracefulmovies.model.MovieData;
import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.model.MovieReleaseType;
import com.xw.project.gracefulmovies.model.NetLocResult;
import com.xw.project.gracefulmovies.model.RequestResult;
import com.xw.project.gracefulmovies.server.api.MovieApi;
import com.xw.project.gracefulmovies.server.api.NetLocApi;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Api操作助手类
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
public class ApiHelper {

    private static String API_KEY;
    private static MovieApi movieApi;
    private static NetLocApi netLocApi;

    public static void init(String apiKey) {
        API_KEY = apiKey;
    }

    private static MovieApi getMovieApi() {
        if (movieApi == null) {
            movieApi = new ApiClient().createApi(MovieApi.class);
        }

        return movieApi;
    }

    private static NetLocApi getNetLocApi() {
        if (netLocApi == null) {
            netLocApi = new ApiClient().createApi("http://gc.ditu.aliyun.com/", NetLocApi.class);
        }

        return netLocApi;
    }

    public static Observable<List<MovieModel>> loadBeReleasedMovies(String city) {
        return getMovieApi()
                .apiGet(API_KEY, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RequestResult, MovieData>() {
                    @Override
                    public MovieData call(RequestResult requestResult) {
                        if (requestResult.getResult() == null) {
                            /**
                             * 如果返回数据对象是null，则抛出业务异常
                             */
                            throw new ApiException(requestResult.getError_code(), requestResult.getReason());
                        }
                        return requestResult.getResult();
                    }
                })
                .map(new Func1<MovieData, MovieReleaseType>() {
                    @Override
                    public MovieReleaseType call(MovieData movieData) {
                        return movieData == null ? null : movieData.getData()[0]; // 正在上映
                    }
                })
                .map(new Func1<MovieReleaseType, List<MovieModel>>() {
                    @Override
                    public List<MovieModel> call(MovieReleaseType movieReleaseType) {
                        return movieReleaseType == null ? null : movieReleaseType.getData();
                    }
                });
    }

    public static Observable<List<MovieModel>> loadGoingToBeingReleasedMovies(String city) {
        return getMovieApi()
                .apiGet(API_KEY, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RequestResult, MovieData>() {
                    @Override
                    public MovieData call(RequestResult requestResult) {
                        if (requestResult.getResult() == null) {
                            /**
                             * 如果返回数据对象是null，则抛出业务异常
                             */
                            throw new ApiException(requestResult.getError_code(), requestResult.getReason());
                        }
                        return requestResult.getResult();
                    }
                })
                .map(new Func1<MovieData, MovieReleaseType>() {
                    @Override
                    public MovieReleaseType call(MovieData movieData) {
                        return movieData == null ? null : movieData.getData()[1]; // 即将上映
                    }
                })
                .map(new Func1<MovieReleaseType, List<MovieModel>>() {
                    @Override
                    public List<MovieModel> call(MovieReleaseType movieReleaseType) {
                        return movieReleaseType == null ? null : movieReleaseType.getData();
                    }
                });
    }

    public static Observable<NetLocResult> loadNetLoc(String latLng) {
        return getNetLocApi().getNetLoc(latLng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void releaseNetLocApi() {
        netLocApi = null;
    }

}

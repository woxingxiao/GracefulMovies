package com.xw.project.gracefulmovies.server;

import com.xw.project.gracefulmovies.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
class ApiClient {

    private Retrofit.Builder mRetrofitBuilder;
    private OkHttpClient.Builder mOkHttpClientBuilder;

    ApiClient() {
        mRetrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://op.juhe.cn/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor);
        }
    }

    <S> S createApi(String baseUrl, Class<S> ApiClass) {
        mRetrofitBuilder.baseUrl(baseUrl);

        return createApi(ApiClass);
    }

    <S> S createApi(Class<S> ApiClass) {
        return mRetrofitBuilder
                .client(mOkHttpClientBuilder.build())
                .build()
                .create(ApiClass);
    }

}

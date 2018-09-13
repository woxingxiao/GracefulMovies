package com.xw.project.gracefulmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.xw.project.gracefulmovies.data.ApiResponse;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.NetworkBoundResource;
import com.xw.project.gracefulmovies.data.api.ApiClient;
import com.xw.project.gracefulmovies.data.api.service.MovieService;
import com.xw.project.gracefulmovies.data.ao.MovieDetail;

/**
 * <p>
 * Created by woxingxiao on 2018-08-15.
 */
public class MovieDetailRepository {

    public LiveData<DataResource<MovieDetail>> getMovieDetails(String locationId, int movieId) {
        return new NetworkBoundResource<MovieDetail>() {
            @Override
            protected boolean fetchFromNetworkDirectly() {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<MovieDetail> loadFromLocal(MutableLiveData<MovieDetail> mutableLiveData) {
                return mutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieDetail>> requestApi() {
                MovieService service = new ApiClient().createApi("https://ticket-api-m.mtime.cn/", MovieService.class);
                ApiResponse<MovieDetail> response = new ApiResponse<>();
                return response.map(service.movieDetailGet(locationId, movieId));
            }

            @Override
            protected void saveRemoteResult(@NonNull MovieDetail data) {
            }
        }.getAsLiveData();
    }
}

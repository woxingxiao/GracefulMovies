package com.xw.project.gracefulmovies.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.data.ApiResponse;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.NetworkBoundResource;
import com.xw.project.gracefulmovies.data.api.ApiClient;
import com.xw.project.gracefulmovies.data.api.service.MovieService;
import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;
import com.xw.project.gracefulmovies.rx.RxSchedulers;
import com.xw.project.gracefulmovies.rx.SimpleConsumer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * <p>
 * Created by woxingxiao on 2018-08-09.
 */
public class MovieRepository {

    private MovieRepository() {
    }

    private static class SingletonHolder {
        private static final MovieRepository sInstance = new MovieRepository();
    }

    public static MovieRepository getInstance() {
        return SingletonHolder.sInstance;
    }

    public LiveData<DataResource<List<MovieEntity>>> getMovieNowList(int cityId) {
        return new NetworkBoundResource<List<MovieEntity>>() {
            @Nullable
            @Override
            protected LiveData<List<MovieEntity>> loadFromLocal() {
                return GMApplication.getInstance().getDatabase().movieDao().loadMovieNowList();
            }

            @Nullable
            @Override
            protected LiveData<ApiResponse<List<MovieEntity>>> requestApi() {
                MovieService service = new ApiClient().createApi(MovieService.class);
                ApiResponse<List<MovieEntity>> response = new ApiResponse<>();
                return response.map(service.movieNowListGet(cityId));
            }

            @Override
            protected void saveRemoteResult(@NonNull List<MovieEntity> data) {
                Observable.just(data)
                        .map(rawList -> {
                            Gson gson = new Gson();
                            String json = gson.toJson(rawList);
                            List<MovieEntity> newList;
                            newList = gson.fromJson(json, new TypeToken<ArrayList<MovieEntity>>() {
                            }.getType());
                            return newList;
                        })
                        .compose(RxSchedulers.applyIO())
                        .subscribe(new SimpleConsumer<List<MovieEntity>>() {
                            @Override
                            public void accept(List<MovieEntity> list) {
                                for (int i = 0, size = list.size(); i < size; i++) {
                                    list.get(i).setRank(i);
                                    list.get(i).setNow(true);
                                }
                                GMApplication.getInstance().getDatabase().movieDao().updateMovieNowList(list);
                            }
                        });
            }
        }.getAsLiveData();
    }

    public LiveData<DataResource<List<MovieEntity>>> getMovieFutureList(int cityId) {
        return new NetworkBoundResource<List<MovieEntity>>() {
            @Nullable
            @Override
            protected LiveData<List<MovieEntity>> loadFromLocal() {
                return GMApplication.getInstance().getDatabase().movieDao().loadMovieFutureList();
            }

            @Nullable
            @Override
            protected LiveData<ApiResponse<List<MovieEntity>>> requestApi() {
                MovieService service = new ApiClient().createApi(MovieService.class);
                ApiResponse<List<MovieEntity>> response = new ApiResponse<>();
                return response.map(service.movieFutureListGet(cityId));
            }

            @Override
            protected void saveRemoteResult(@NonNull List<MovieEntity> data) {
                Observable.just(data)
                        .map(rawList -> {
                            Gson gson = new Gson();
                            String json = gson.toJson(rawList);
                            List<MovieEntity> newList;
                            newList = gson.fromJson(json, new TypeToken<ArrayList<MovieEntity>>() {
                            }.getType());
                            return newList;
                        })
                        .observeOn(Schedulers.io())
                        .subscribe(new SimpleConsumer<List<MovieEntity>>() {
                            @Override
                            public void accept(List<MovieEntity> list) {
                                for (int i = 0, size = list.size(); i < size; i++) {
                                    list.get(i).setRank(i);
                                }
                                GMApplication.getInstance().getDatabase().movieDao().updateMovieFutureList(list);
                            }
                        });
            }
        }.getAsLiveData();
    }
}

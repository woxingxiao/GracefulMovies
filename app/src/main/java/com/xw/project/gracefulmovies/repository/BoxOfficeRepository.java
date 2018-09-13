package com.xw.project.gracefulmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.data.ApiResponse;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.NetworkBoundResource;
import com.xw.project.gracefulmovies.data.api.ApiClient;
import com.xw.project.gracefulmovies.data.api.service.BoxOfficeService;
import com.xw.project.gracefulmovies.data.db.entity.BoxOfficeEntity;
import com.xw.project.gracefulmovies.rx.RxSchedulers;
import com.xw.project.gracefulmovies.rx.SimpleConsumer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * <p>
 * Created by woxingxiao on 2018-08-17.
 */
public class BoxOfficeRepository {

    public LiveData<DataResource<List<BoxOfficeEntity>>> getBoxOffices() {
        return new NetworkBoundResource<List<BoxOfficeEntity>>() {
            @Override
            protected boolean returnLocalDataIfNetworkDataEmpty() {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<BoxOfficeEntity>> loadFromLocal(MutableLiveData<List<BoxOfficeEntity>> mutableLiveData) {
                Observable.just(mutableLiveData)
                        .compose(RxSchedulers.applyIO())
                        .subscribe(new SimpleConsumer<MutableLiveData<List<BoxOfficeEntity>>>() {
                            @Override
                            public void accept(MutableLiveData<List<BoxOfficeEntity>> it) {
                                List<BoxOfficeEntity> list = GMApplication.getInstance().getDatabase()
                                        .boxOfficeDao().loadBoxOfficeList();
                                it.postValue(list);
                            }
                        });
                return mutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<BoxOfficeEntity>>> requestApi() {
                BoxOfficeService service = new ApiClient().createApi("http://api.shenjian.io/", BoxOfficeService.class);
                service.fetchBoxOfficeGet();
                ApiResponse<List<BoxOfficeEntity>> response = new ApiResponse<>();
                return response.map(service.fetchBoxOfficeGet());
            }

            @Override
            protected void saveRemoteResult(@NonNull List<BoxOfficeEntity> data) {
                Observable.just(data)
                        .map(rawList -> {
                            Gson gson = new Gson();
                            String json = gson.toJson(rawList);
                            List<BoxOfficeEntity> newList;
                            newList = gson.fromJson(json, new TypeToken<ArrayList<BoxOfficeEntity>>() {
                            }.getType());
                            return newList;
                        })
                        .compose(RxSchedulers.applyIO())
                        .subscribe(new SimpleConsumer<List<BoxOfficeEntity>>() {
                            @Override
                            public void accept(List<BoxOfficeEntity> list) {
                                GMApplication.getInstance().getDatabase().boxOfficeDao().update(list);
                            }
                        });
            }
        }.getAsLiveData();
    }
}

package com.xw.project.gracefulmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.xw.project.gracefulmovies.rx.RxSchedulers;
import com.xw.project.gracefulmovies.rx.SimpleConsumer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public abstract class NetworkBoundResource<T> {

    private final MediatorLiveData<DataResource<T>> mResult = new MediatorLiveData<>();
    private long mStartMills;

    protected NetworkBoundResource() {
        mStartMills = System.currentTimeMillis();
        mResult.setValue(DataResource.loading());

        MutableLiveData<T> dbData = new MutableLiveData<>();
        if (fetchFromNetworkDirectly()) {
            dbData.setValue(null);
        } else {
            loadFromLocal(dbData);
        }
        mResult.addSource(dbData, data -> {
            mResult.removeSource(dbData);

            if (fetchFromNetworkDirectly() || shouldFetchFromNetwork(data)) {
                fetchFromNetwork(dbData);
            } else {
                mResult.addSource(dbData, newData -> {
                    if (newData == null || (newData instanceof List && ((List) newData).isEmpty())) {
                        setValue(DataResource.empty());
                    } else {
                        setValue(DataResource.success(newData));
                    }
                });
            }
        });
    }

    private void fetchFromNetwork(MutableLiveData<T> dbSource) {
        LiveData<ApiResponse<T>> apiResponse = requestApi();
        mResult.addSource(dbSource, newData ->
                setValue(DataResource.loading())
        );
        mResult.addSource(apiResponse, response -> {
            mResult.removeSource(apiResponse);
            mResult.removeSource(dbSource);

            if (response instanceof ApiResponse.ApiResponseSuccess) {
                saveRemoteResult(response.getData());

                setValue(DataResource.success(response.getData()));
            } else if (response instanceof ApiResponse.ApiResponseError) {
                onFetchFailed();

                setValue(DataResource.error(response.getException()));
            } else {
                if (returnLocalDataIfNetworkDataEmpty()) {
                    mResult.addSource(loadFromLocal(dbSource), newData -> {
                        if (newData == null || (newData instanceof List && ((List) newData).isEmpty())) {
                            setValue(DataResource.empty());
                        } else {
                            setValue(DataResource.success(newData));
                        }
                    });
                } else {
                    setValue(DataResource.empty());
                }
            }
        });
    }

    @MainThread
    private void setValue(DataResource<T> newValue) {
        if (mResult.getValue() != newValue) {
            if (newValue.getStatus() == DataResource.Status.LOADING) {
                mResult.setValue(newValue);
            } else {
                long delay = System.currentTimeMillis() - mStartMills;
                delay = friendlyWaitingTimeInMills() - delay;
                if (delay < 0)
                    delay = 0;
                Observable.just(newValue)
                        .delay(delay, TimeUnit.MILLISECONDS)
                        .compose(RxSchedulers.apply())
                        .subscribe(new SimpleConsumer<DataResource<T>>() {
                            @Override
                            public void accept(DataResource<T> it) {
                                mResult.setValue(it);
                            }
                        });
            }
        }
    }

    protected boolean fetchFromNetworkDirectly() {
        return false;
    }

    @MainThread
    protected boolean shouldFetchFromNetwork(@Nullable T localData) {
        return localData == null || (localData instanceof List && ((List) localData).isEmpty());
    }

    @MainThread
    protected boolean returnLocalDataIfNetworkDataEmpty() {
        return false;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<T> loadFromLocal(MutableLiveData<T> mutableLiveData);

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<T>> requestApi();

    @IntRange(from = 0)
    protected int friendlyWaitingTimeInMills() {
        return 500;
    }

    @WorkerThread
    protected abstract void saveRemoteResult(@NonNull T data);

    @MainThread
    protected void onFetchFailed() {
    }

    public final LiveData<DataResource<T>> getAsLiveData() {
        return mResult;
    }
}
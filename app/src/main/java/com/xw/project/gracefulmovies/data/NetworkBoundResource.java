package com.xw.project.gracefulmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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

        LiveData<T> localSource = loadFromLocal();
        if (localSource != null) {
            mResult.addSource(localSource, newData -> {
                mResult.removeSource(localSource);

                if (newData != null) {
                    if (newData instanceof List) {
                        if (!((List) newData).isEmpty()) {
                            setValue(DataResource.success(newData));
                        }
                    } else {
                        setValue(DataResource.success(newData));
                    }
                }
            });
        }
        if (shouldFetchFromNetwork()) {
            fetchFromNetwork(localSource);
        }
    }

    private void fetchFromNetwork(@Nullable LiveData<T> localSource) {
        setValue(DataResource.loading());

        LiveData<ApiResponse<T>> apiSource = requestApi();
        if (apiSource == null) {
            if (localSource != null) {
                mResult.removeSource(localSource);
                mResult.addSource(localSource, newData -> {
                    if (newData == null || (newData instanceof List && ((List) newData).isEmpty())) {
                        setValue(DataResource.empty());
                    } else {
                        setValue(DataResource.success(newData));
                    }
                });
            }

            return;
        }

        mResult.addSource(apiSource, response -> {
            if (localSource != null)
                mResult.removeSource(localSource);
            mResult.removeSource(apiSource);

            if (response instanceof ApiResponse.ApiResponseSuccess) {
                saveRemoteResult(response.getData());

                setValue(DataResource.success(response.getData()));
            } else if (response instanceof ApiResponse.ApiResponseError) {
                onFetchFailed();

                setValue(DataResource.error(response.getException()));
            } else {
                if (returnLocalDataIfNetworkDataEmpty()) {
                    LiveData<T> localData = loadFromLocal();
                    if (localData != null) {
                        mResult.addSource(localData, newData -> {
                            if (newData == null || (newData instanceof List && ((List) newData).isEmpty())) {
                                setValue(DataResource.empty());
                            } else {
                                setValue(DataResource.success(newData));
                            }
                        });
                    }
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

    protected boolean shouldFetchFromNetwork() {
        return true;
    }

    protected boolean returnLocalDataIfNetworkDataEmpty() {
        return false;
    }

    @Nullable
    @MainThread
    protected abstract LiveData<T> loadFromLocal();

    @Nullable
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
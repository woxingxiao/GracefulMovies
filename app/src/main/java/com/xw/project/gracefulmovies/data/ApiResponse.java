package com.xw.project.gracefulmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.xw.project.gracefulmovies.data.api.ApiException;
import com.xw.project.gracefulmovies.data.api.ApiObserver;
import com.xw.project.gracefulmovies.data.ao.bridge.ModelBridge;
import com.xw.project.gracefulmovies.rx.RxSchedulers;

import java.util.List;

import io.reactivex.Observable;

/**
 * <p>
 * Created by woxingxiao on 2018-08-22.
 */
public class ApiResponse<T> {

    private final MutableLiveData<ApiResponse<T>> mLiveData = new MutableLiveData<>();

    private T data;
    private Exception exception;

    public ApiResponse() {
    }

    ApiResponse(T data, Exception exception) {
        this.data = data;
        this.exception = exception;
    }

    public T getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }

    public LiveData<ApiResponse<T>> map(Observable<ModelBridge<T>> observable) {
        observable
                .map(bridge -> {
                    if (bridge == null) {
                        throw new ApiException(ApiException.CODE_FAILED, "获取数据失败");
                    } else if (bridge.data == null || (bridge.data instanceof List && ((List) bridge.data).isEmpty())) {
                        throw new ApiException(ApiException.CODE_EMPTY, "暂无数据");
                    }

                    return bridge.data;
                })
                .compose(RxSchedulers.applyIO())
                .subscribe(new ApiObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        ApiResponseSuccess<T> success = create(t);
                        mLiveData.postValue(success);
                    }

                    @Override
                    protected void onErrorResolved(Throwable e, String msg) {
                        if (e instanceof ApiException && ((ApiException) e).getCode() == ApiException.CODE_EMPTY) {
                            ApiResponseEmpty<T> empty = create();
                            mLiveData.postValue(empty);
                        } else {
                            ApiResponseError<T> error = create(new Exception(msg, e));
                            mLiveData.postValue(error);
                        }
                    }
                });

        return mLiveData;
    }

    private ApiResponseSuccess<T> create(T data) {
        return new ApiResponseSuccess<>(data, null);
    }

    private ApiResponseError<T> create(Exception e) {
        return new ApiResponseError<>(null, e);
    }

    private ApiResponseEmpty<T> create() {
        return new ApiResponseEmpty<>(null, null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ApiResponseSuccess<T> extends ApiResponse<T> {
        ApiResponseSuccess(T data, Exception e) {
            super(data, e);
        }
    }

    public static class ApiResponseError<T> extends ApiResponse<T> {
        ApiResponseError(T data, Exception e) {
            super(data, e);
        }
    }

    public static class ApiResponseEmpty<T> extends ApiResponse<T> {
        ApiResponseEmpty(T data, Exception e) {
            super(data, e);
        }
    }
}

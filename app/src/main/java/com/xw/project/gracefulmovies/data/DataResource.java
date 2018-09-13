package com.xw.project.gracefulmovies.data;

import android.support.annotation.NonNull;

import static com.xw.project.gracefulmovies.data.DataResource.Status.EMPTY;
import static com.xw.project.gracefulmovies.data.DataResource.Status.ERROR;
import static com.xw.project.gracefulmovies.data.DataResource.Status.LOADING;
import static com.xw.project.gracefulmovies.data.DataResource.Status.SUCCESS;

public class DataResource<T> {

    @NonNull
    private final Status mStatus;
    private final T mData;
    private final Exception mException;

    private DataResource(@NonNull Status status, T data, Exception exception) {
        this.mStatus = status;
        this.mData = data;
        this.mException = exception;
    }

    public static <T> DataResource<T> loading() {
        return new DataResource<>(LOADING, null, null);
    }

    public static <T> DataResource<T> success(T data) {
        return new DataResource<>(SUCCESS, data, null);
    }

    public static <T> DataResource<T> error(Exception e) {
        return new DataResource<>(ERROR, null, e);
    }

    public static <T> DataResource<T> empty() {
        return new DataResource<>(EMPTY, null, null);
    }

    @NonNull
    public Status getStatus() {
        return mStatus;
    }

    public T getData() {
        return mData;
    }

    public Exception getException() {
        return mException;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public enum Status {
        LOADING,
        SUCCESS,
        ERROR,
        EMPTY
    }
}
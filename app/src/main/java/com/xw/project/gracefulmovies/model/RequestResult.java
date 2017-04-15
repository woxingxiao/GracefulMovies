package com.xw.project.gracefulmovies.model;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-11.
 */

public class RequestResult<T> {

    private int error_code;

    private String reason;

    private T result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

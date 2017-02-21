package com.xw.project.gracefulmovies.server;

/**
 * 业务异常
 * <p/>
 * Created by woxingxiao on 2017-02-18.
 */
public class ApiException extends RuntimeException {

    private int code;
    private String msg;

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package com.xw.project.gracefulmovies.data.api;

/**
 * 业务异常
 * <p>
 * Created by woxingxiao on 2018-08-21.
 */
public class ApiException extends RuntimeException {

    public static final int CODE_FAILED = 0;
    public static final int CODE_SUCCEED = 1;
    public static final int CODE_EMPTY = -1;
    public static final int CODE_LIST_EMPTY = -2;

    public static final int BUSINESS_CODE_DATA_FORMAT = 10001;
    public static final int BUSINESS_CODE_SERVER_ERROR = BUSINESS_CODE_DATA_FORMAT + 1;

    public static final int[] BUSINESS_CODES = new int[]{BUSINESS_CODE_DATA_FORMAT, BUSINESS_CODE_SERVER_ERROR};
    public static final String[] BUSINESS_MESSAGES = new String[]{"服务器数据格式错误", "服务器出错了"};

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

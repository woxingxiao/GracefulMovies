package com.xw.project.gracefulmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <><p/>
 * Created by woxingxiao on 2017-03-07.
 */

public class BoxOfficeResult {

    @SerializedName("showapi_res_code")
    private int error_code;

    @SerializedName("showapi_res_error")
    private String reason;

    @SerializedName("showapi_res_body")
    private BoxOfficeData result;

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

    public BoxOfficeData getResult() {
        return result;
    }

    public void setResult(BoxOfficeData result) {
        this.result = result;
    }

    public class BoxOfficeData {

        @SerializedName("ret_code")
        public String code;
        @SerializedName("datalist")
        public List<BoxOfficeModel> modelList;

    }
}

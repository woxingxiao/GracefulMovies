package com.xw.project.gracefulmovies.data.ao;

/**
 * <p>
 * Created by woxingxiao on 2019-01-10.
 */
public class ReGeoResult {

    private String status;
    private String info;
    private String infocode;
    private ReGeo regeocode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public ReGeo getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(ReGeo regeocode) {
        this.regeocode = regeocode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ReGeo {
        public ReGeoInfo addressComponent;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ReGeoInfo {
        public String province;
        public String city;
    }
}

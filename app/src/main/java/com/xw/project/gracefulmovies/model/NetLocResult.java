package com.xw.project.gracefulmovies.model;

import java.util.List;

/**
 * 网络转换定位返回
 * <p/>
 * Created by woxingxiao on 2017-03-01.
 */
public class NetLocResult {

    private List<LocCity> addrList = null;

    private LocCity mLocCity;
    private String mCity;
    private String mUpperCity;

    public String getCity() {
        if (mLocCity == null && addrList != null && !addrList.isEmpty()) {
            for (LocCity locCity : addrList) {
                if ("poi".equals(locCity.type)) {
                    mLocCity = locCity;
                    break;
                }
            }
        }
        if (mLocCity != null && mCity == null) {
            if (mLocCity.admName.endsWith(","))
                mLocCity.admName = mLocCity.admName.substring(0, mLocCity.admName.lastIndexOf(","));

            if (mLocCity.admName.contains(",")) {
                String[] split = mLocCity.admName.split(",");
                mCity = split[split.length - 1];
            }
        }

        return mCity;
    }

    public String getUpperCity() {
        if (mLocCity == null && addrList != null && !addrList.isEmpty()) {
            for (LocCity locCity : addrList) {
                if ("poi".equals(locCity.type)) {
                    mLocCity = locCity;
                    break;
                }
            }
        }
        if (mLocCity != null && mUpperCity == null) {
            if (mLocCity.admName.endsWith(","))
                mLocCity.admName = mLocCity.admName.substring(0, mLocCity.admName.lastIndexOf(","));

            if (mLocCity.admName.contains(",")) {
                String[] split = mLocCity.admName.split(",");
                mUpperCity = split[split.length - 2];
            }
        }

        return mUpperCity;
    }

    private class LocCity {
        String type;
        String admName;
    }
}

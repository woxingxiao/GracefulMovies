package com.xw.project.gracefulmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * MovieSearchModel
 * <p></>
 * Created by woxingxiao on 2017-04-15.
 */

public class MovieSearchModel {

    private String title;
    private String tag;
    private String act;
    private float rating;
    private String area;
    private String dir;
    private String desc;
    private String cover;
    @SerializedName("act_s")
    private List<CastInfo> mCasts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<CastInfo> getCasts() {
        return mCasts;
    }

    public void setCasts(List<CastInfo> casts) {
        mCasts = casts;
    }

    public static class CastInfo {
        public String name;
        public String image;
    }
}

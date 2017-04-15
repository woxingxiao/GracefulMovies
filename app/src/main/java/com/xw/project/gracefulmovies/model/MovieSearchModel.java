package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * MovieSearchModel
 * <p></>
 * Created by woxingxiao on 2017-04-15.
 */

public class MovieSearchModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    protected MovieSearchModel(Parcel in) {
        title = in.readString();
        tag = in.readString();
        act = in.readString();
        rating = in.readFloat();
        area = in.readString();
        dir = in.readString();
        desc = in.readString();
        cover = in.readString();
        mCasts = in.createTypedArrayList(CastInfo.CREATOR);
    }

    public static final Creator<MovieSearchModel> CREATOR = new Creator<MovieSearchModel>() {
        @Override
        public MovieSearchModel createFromParcel(Parcel in) {
            return new MovieSearchModel(in);
        }

        @Override
        public MovieSearchModel[] newArray(int size) {
            return new MovieSearchModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tag);
        dest.writeString(act);
        dest.writeFloat(rating);
        dest.writeString(area);
        dest.writeString(dir);
        dest.writeString(desc);
        dest.writeString(cover);
        dest.writeTypedList(mCasts);
    }

    public static class CastInfo implements Parcelable {
        String name;
        String image;

        protected CastInfo(Parcel in) {
            name = in.readString();
            image = in.readString();
        }

        public static final Creator<CastInfo> CREATOR = new Creator<CastInfo>() {
            @Override
            public CastInfo createFromParcel(Parcel in) {
                return new CastInfo(in);
            }

            @Override
            public CastInfo[] newArray(int size) {
                return new CastInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(image);
        }
    }
}

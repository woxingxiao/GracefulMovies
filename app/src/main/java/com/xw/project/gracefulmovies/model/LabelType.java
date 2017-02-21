package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */

public class LabelType implements Parcelable {

    private String showname;

    private LabelGroup data;

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public LabelGroup getData() {
        return data;
    }

    public void setData(LabelGroup data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.showname);
        dest.writeParcelable(this.data, flags);
    }

    public LabelType() {
    }

    private LabelType(Parcel in) {
        this.showname = in.readString();
        this.data = in.readParcelable(LabelGroup.class.getClassLoader());
    }

    public static final Creator<LabelType> CREATOR = new Creator<LabelType>() {
        @Override
        public LabelType createFromParcel(Parcel source) {
            return new LabelType(source);
        }

        @Override
        public LabelType[] newArray(int size) {
            return new LabelType[size];
        }
    };
}

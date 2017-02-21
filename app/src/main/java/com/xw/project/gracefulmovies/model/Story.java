package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */

public class Story implements Parcelable {

    private String showname;

    private StoryBrief data;

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public StoryBrief getData() {
        return data;
    }

    public void setData(StoryBrief data) {
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

    public Story() {
    }

    private Story(Parcel in) {
        this.showname = in.readString();
        this.data = in.readParcelable(StoryBrief.class.getClassLoader());
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}

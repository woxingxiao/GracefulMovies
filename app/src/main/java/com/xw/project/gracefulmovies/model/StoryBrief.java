package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-14.
 */

public class StoryBrief implements Parcelable {

    private String storyBrief;

    public String getStoryBrief() {
        return storyBrief;
    }

    public void setStoryBrief(String storyBrief) {
        this.storyBrief = storyBrief;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storyBrief);
    }

    public StoryBrief() {
    }

    private StoryBrief(Parcel in) {
        this.storyBrief = in.readString();
    }

    public static final Creator<StoryBrief> CREATOR = new Creator<StoryBrief>() {
        @Override
        public StoryBrief createFromParcel(Parcel source) {
            return new StoryBrief(source);
        }

        @Override
        public StoryBrief[] newArray(int size) {
            return new StoryBrief[size];
        }
    };

}

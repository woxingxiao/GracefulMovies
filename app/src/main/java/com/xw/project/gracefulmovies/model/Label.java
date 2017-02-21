package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */

public class Label implements Parcelable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public Label() {
    }

    private Label(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel source) {
            return new Label(source);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };
}

package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */

public class LabelGroup implements Parcelable {

    @SerializedName("1")
    private Label label1;

    @SerializedName("2")
    private Label label2;

    @SerializedName("3")
    private Label label3;

    @SerializedName("4")
    private Label label4;

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label label1) {
        this.label1 = label1;
    }

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label label2) {
        this.label2 = label2;
    }

    public Label getLabel3() {
        return label3;
    }

    public void setLabel3(Label label3) {
        this.label3 = label3;
    }

    public Label getLabel4() {
        return label4;
    }

    public void setLabel4(Label label4) {
        this.label4 = label4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.label1, flags);
        dest.writeParcelable(this.label2, flags);
        dest.writeParcelable(this.label3, flags);
        dest.writeParcelable(this.label4, flags);
    }

    public LabelGroup() {
    }

    private LabelGroup(Parcel in) {
        this.label1 = in.readParcelable(Label.class.getClassLoader());
        this.label2 = in.readParcelable(Label.class.getClassLoader());
        this.label3 = in.readParcelable(Label.class.getClassLoader());
        this.label4 = in.readParcelable(Label.class.getClassLoader());
    }

    public static final Creator<LabelGroup> CREATOR = new Creator<LabelGroup>() {
        @Override
        public LabelGroup createFromParcel(Parcel source) {
            return new LabelGroup(source);
        }

        @Override
        public LabelGroup[] newArray(int size) {
            return new LabelGroup[size];
        }
    };
}

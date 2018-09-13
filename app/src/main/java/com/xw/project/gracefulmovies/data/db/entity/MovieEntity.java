package com.xw.project.gracefulmovies.data.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Created by woxingxiao on 2018-08-08.
 */
@Entity(tableName = "movies")
public class MovieEntity {

    @PrimaryKey
    private int id;
    private boolean isNow;
    @SerializedName(value = "title", alternate = "t")
    private String title;
    @SerializedName(value = "image", alternate = "img")
    private String image;
    @SerializedName(value = "movieType", alternate = "type")
    private String type;
    @SerializedName("r")
    private double rating;
    private String actors;
    private String actor1;
    private String actor2;
    @SerializedName("rYear")
    private int year;
    @SerializedName("rMonth")
    private int month;
    @SerializedName("rDay")
    private int day;

    @Ignore
    private String imageTiny;
    @Ignore
    private List<String> typeList;

    private int rank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNow() {
        return isNow;
    }

    public void setNow(boolean now) {
        isNow = now;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        if (!TextUtils.isEmpty(image) && image.contains("_1280X720X2")) {
            image = image.replace("_1280X720X2", "_1280X720X3");
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getActor1() {
        return actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getImageTiny() {
        if (imageTiny == null && !TextUtils.isEmpty(image) && image.contains("_1280X720X2")) {
            imageTiny = image.replace("_1280X720X2", "");
        } else if (imageTiny == null && !TextUtils.isEmpty(image) && image.contains("_1280X720X3")) {
            imageTiny = image.replace("_1280X720X3", "");
        }
        return imageTiny;
    }

    public List<String> getTypeList() {
        if (typeList == null && !TextUtils.isEmpty(type)) {
            typeList = new ArrayList<>();
            if (type.contains("/")) {
                String[] split = type.trim().split("/");
                typeList = Arrays.asList(split);
            } else {
                typeList.add(type);
            }
        }
        return typeList;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

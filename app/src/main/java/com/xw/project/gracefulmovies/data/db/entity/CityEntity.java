package com.xw.project.gracefulmovies.data.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Created by woxingxiao on 2018-08-08.
 */
@Entity(tableName = "cities")
public class CityEntity {

    @PrimaryKey
    private int id;
    @SerializedName("n")
    private String name;
    private boolean isUpper;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUpper() {
        return isUpper;
    }

    public void setUpper(boolean upper) {
        isUpper = upper;
    }
}

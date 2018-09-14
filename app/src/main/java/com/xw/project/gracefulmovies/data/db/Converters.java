package com.xw.project.gracefulmovies.data.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {

    @TypeConverter
    public static String stringList2Json(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<String> json2StringList(String json) {
        return new Gson().fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }
}
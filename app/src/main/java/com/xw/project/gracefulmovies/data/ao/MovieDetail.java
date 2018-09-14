package com.xw.project.gracefulmovies.data.ao;

import android.text.TextUtils;

import java.util.List;

public class MovieDetail {

    public MovieDetailInfo basic;
    public BoxOfficeInDetail boxOffice;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class MovieDetailInfo {
        public List<Actor> actors;
        public Director director;
//        public int hotRanking;
//        public String img;
        public String name;
        public String nameEn;
        public String mins;
        public boolean is3D;
        public boolean isIMAX;
        public boolean isIMAX3D;
        public float overallRating;
        public String releaseDate;
        public String story;
        public List<String> type;
        public MovieStills stageImg;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class BoxOfficeInDetail {
        public int ranking;
        public String todayBoxDes;
        public String todayBoxDesUnit;
        public String totalBoxDes;
        public String totalBoxUnit;

        public boolean notReleasedYet() {
            return ranking <= 0 || TextUtils.isEmpty(todayBoxDes) || TextUtils.isEmpty(totalBoxDes);
        }
    }
}
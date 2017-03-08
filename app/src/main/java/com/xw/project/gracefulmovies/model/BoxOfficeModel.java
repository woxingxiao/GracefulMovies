package com.xw.project.gracefulmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * <><p/>
 * Created by woxingxiao on 2017-03-07.
 */

public class BoxOfficeModel {

    @SerializedName(value = "Rank", alternate = {"MovieRank", "rank"})
    private String rank; // 排名
    @SerializedName("MovieName")
    private String name; // 名字
    @SerializedName(value = "BoxOffice", alternate = {"boxoffice", "WeekAmount"})
    private String boxOffice; // 票房
    @SerializedName(value = "SumBoxOffice", alternate = "SumWeekAmount")
    private String sumBoxOffice; // 总票房
    @SerializedName(value = "BoxOffice_Up", alternate = {"Amount_Up", "box_pro"})
    private String boxOfficeRate; // 环比变化、月度占比
    @SerializedName(value = "AvgPrice", alternate = "avgboxoffice")
    private String avgPrice; // 平均票价
    @SerializedName("WomIndex")
    private String womIndex; // 口碑指数
    @SerializedName(value = "MovieDay", alternate = "days")
    private String movieDays; // 上映天数、月内天数
    @SerializedName("releaseTime")
    private String releaseDate; // 上映日期

    private int rankInt = -1;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getSumBoxOffice() {
        return sumBoxOffice;
    }

    public void setSumBoxOffice(String sumBoxOffice) {
        this.sumBoxOffice = sumBoxOffice;
    }

    public String getBoxOfficeRate() {
        return boxOfficeRate;
    }

    public void setBoxOfficeRate(String boxOfficeRate) {
        this.boxOfficeRate = boxOfficeRate;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getWomIndex() {
        return womIndex;
    }

    public void setWomIndex(String womIndex) {
        this.womIndex = womIndex;
    }

    public String getMovieDays() {
        return movieDays;
    }

    public void setMovieDays(String movieDays) {
        this.movieDays = movieDays;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRankInt() {
        if (rankInt < 0 && getRank() != null && !getRank().isEmpty()) {
            try {
                rankInt = Integer.valueOf(getRank());
            } catch (NumberFormatException e) {
                rankInt = 0;
            }
        }
        return rankInt;
    }
}

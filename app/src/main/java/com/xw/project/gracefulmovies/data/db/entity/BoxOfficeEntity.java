package com.xw.project.gracefulmovies.data.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * <p>
 * Created by woxingxiao on 2018-08-17.
 */
@Entity(tableName = "box_offices")
public class BoxOfficeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Irank;
    private String MovieName;
    private String BoxOffice;
    private String sumBoxOffice;
    private String movieDay;
    private String boxPer;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIrank() {
        return Irank;
    }

    public void setIrank(String Irank) {
        this.Irank = Irank;
    }

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String MovieName) {
        this.MovieName = MovieName;
    }

    public String getBoxOffice() {
        return BoxOffice;
    }

    public void setBoxOffice(String BoxOffice) {
        this.BoxOffice = BoxOffice;
    }

    public String getSumBoxOffice() {
        return sumBoxOffice;
    }

    public void setSumBoxOffice(String sumBoxOffice) {
        this.sumBoxOffice = sumBoxOffice;
    }

    public String getMovieDay() {
        return movieDay;
    }

    public void setMovieDay(String movieDay) {
        this.movieDay = movieDay;
    }

    public String getBoxPer() {
        return boxPer;
    }

    public void setBoxPer(String boxPer) {
        this.boxPer = boxPer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BoxOfficeEntity{" +
                "Irank='" + Irank + '\'' +
                ", MovieName='" + MovieName + '\'' +
                ", BoxOffice='" + BoxOffice + '\'' +
                ", sumBoxOffice='" + sumBoxOffice + '\'' +
                ", movieDay='" + movieDay + '\'' +
                ", boxPer='" + boxPer + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

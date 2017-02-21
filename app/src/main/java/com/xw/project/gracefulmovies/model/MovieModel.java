package com.xw.project.gracefulmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */

public class MovieModel implements Parcelable {

    @SerializedName("tvTitle")
    private String name;

    @SerializedName("iconaddress")
    private String poster;

    private String grade;

    private String gradeNum;

    @SerializedName("iconlinkUrl")
    private String webUrl;

    @SerializedName("subHead")
    private String cinemaNum;

    @SerializedName("playDate")
    private PlayDate releaseDate;

    private LabelType director;

    @SerializedName("star")
    private LabelType cast;

    @SerializedName("type")
    private LabelType movieType;

    @SerializedName("story")
    private Story story;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(String gradeNum) {
        this.gradeNum = gradeNum;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getCinemaNum() {
        return cinemaNum;
    }

    public void setCinemaNum(String cinemaNum) {
        this.cinemaNum = cinemaNum;
    }

    public PlayDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(PlayDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LabelType getDirector() {
        return director;
    }

    public void setDirector(LabelType director) {
        this.director = director;
    }

    public LabelType getCast() {
        return cast;
    }

    public void setCast(LabelType cast) {
        this.cast = cast;
    }

    public LabelType getMovieType() {
        return movieType;
    }

    public void setMovieType(LabelType movieType) {
        this.movieType = movieType;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public String getMovieTypeString() {
        return extractLabelTypeString(getMovieType());
    }

    public List<String> getMovieTypeList() {
        List<String> list = null;
        LabelType labelType = getMovieType();
        if (labelType.getData() != null) {
            list = new ArrayList<>();
            if (labelType.getData().getLabel1() != null) {
                list.add(labelType.getData().getLabel1().getName());
            }
            if (labelType.getData().getLabel2() != null) {
                list.add(labelType.getData().getLabel2().getName());
            }
            if (labelType.getData().getLabel3() != null) {
                list.add(labelType.getData().getLabel3().getName());
            }
            if (labelType.getData().getLabel4() != null) {
                list.add(labelType.getData().getLabel4().getName());
            }
        }

        return list;
    }

    public String getCastString() {
        return extractLabelTypeString(getCast());
    }

    public String getReleaseDateString() {
        if (getReleaseDate() == null) {
            return null;
        }

        return getReleaseDate().getData();
    }

    private String extractLabelTypeString(LabelType labelType) {
        if (labelType == null) return "";

        StringBuilder builder = new StringBuilder();
        if (labelType.getData() != null) {
            if (labelType.getData().getLabel1() != null) {
                builder.append(labelType.getData().getLabel1().getName()).append("，");
            }
            if (labelType.getData().getLabel2() != null) {
                builder.append(labelType.getData().getLabel2().getName()).append("，");
            }
            if (labelType.getData().getLabel3() != null) {
                builder.append(labelType.getData().getLabel3().getName()).append("，");
            }
            if (labelType.getData().getLabel4() != null) {
                builder.append(labelType.getData().getLabel4().getName()).append("，");
            }
            if (builder.toString().endsWith("，")) {
                builder.deleteCharAt(builder.lastIndexOf("，"));
            }
        }

        return builder.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.poster);
        dest.writeString(this.grade);
        dest.writeString(this.gradeNum);
        dest.writeString(this.webUrl);
        dest.writeString(this.cinemaNum);
        dest.writeParcelable(this.releaseDate, flags);
        dest.writeParcelable(this.director, flags);
        dest.writeParcelable(this.cast, flags);
        dest.writeParcelable(this.movieType, flags);
        dest.writeParcelable(this.story, flags);
    }

    public MovieModel() {
    }

    private MovieModel(Parcel in) {
        this.name = in.readString();
        this.poster = in.readString();
        this.grade = in.readString();
        this.gradeNum = in.readString();
        this.webUrl = in.readString();
        this.cinemaNum = in.readString();
        this.releaseDate = in.readParcelable(PlayDate.class.getClassLoader());
        this.director = in.readParcelable(LabelType.class.getClassLoader());
        this.cast = in.readParcelable(LabelType.class.getClassLoader());
        this.movieType = in.readParcelable(LabelType.class.getClassLoader());
        this.story = in.readParcelable(Story.class.getClassLoader());
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}

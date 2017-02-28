package com.xw.project.gracefulmovies.view.iview;

import android.content.Context;

import com.xw.project.gracefulmovies.model.MovieModel;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-11.
 */
public interface IMovieListFragment {

    Context getContext();

    void onDataReady(List<MovieModel> movieModels);

    void onDataError(int code, String msg);
}

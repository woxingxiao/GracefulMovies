package com.xw.project.gracefulmovies.presenter.impl;

import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.presenter.IMovieFragmentPresenter;
import com.xw.project.gracefulmovies.server.ApiException;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.server.ApiSubscriber;
import com.xw.project.gracefulmovies.util.PrefUtil;
import com.xw.project.gracefulmovies.view.fragment.MovieListFragment;
import com.xw.project.gracefulmovies.view.iview.IMovieListFragment;

import java.util.List;

import rx.Observable;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-11.
 */

public class MovieFragmentPresenterImpl implements IMovieFragmentPresenter {

    private IMovieListFragment mFragment;
    private ApiSubscriber<List<MovieModel>> mSubscriber;
    private int mErrCode = -1;

    @Override
    public void register(MovieListFragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void loadMovieData(int releaseType) {
        String city = PrefUtil.getCity(mFragment.getContext());

        mSubscriber = new ApiSubscriber<List<MovieModel>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(List<MovieModel> movieModels) {
                if (mFragment != null)
                    mFragment.onDataReady(movieModels);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ApiException)
                    mErrCode = ((ApiException) e).getCode();
                super.onError(e);
            }

            @Override
            protected void onError(String msg) {
                if (mFragment != null)
                    mFragment.onDataError(mErrCode, msg);
            }

            @Override
            public void onFinally() {
                super.onFinally();
            }
        };

        Observable<List<MovieModel>> observable;
        if (releaseType == 0) {
            observable = ApiHelper.loadBeReleasedMovies(city);
        } else {
            observable = ApiHelper.loadGoingToBeingReleasedMovies(city);
        }
        observable.subscribe(mSubscriber);
    }

    @Override
    public void unregister() {
        if (mSubscriber != null && mSubscriber.isUnsubscribed())
            mSubscriber.unsubscribe();
        mFragment = null;
    }
}

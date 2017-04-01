package com.xw.project.gracefulmovies.presenter.impl;

import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.model.MovieReleaseType;
import com.xw.project.gracefulmovies.presenter.IMainActivityPresenter;
import com.xw.project.gracefulmovies.server.ApiException;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.server.ApiSubscriber;
import com.xw.project.gracefulmovies.util.PrefUtil;
import com.xw.project.gracefulmovies.view.iview.IMainActivity;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-04-01.
 */

public class MainActivityPresenter implements IMainActivityPresenter {

    /**
     * 由于接口请求次数限制，加上影讯数据即时性很低，故采取一次启动只加载一次的策略
     */
    private List<MovieModel> mReleasedMovies; // 正在上映电影列表
    private List<MovieModel> mWillReleaseMovies; // 即将上映电影列表

    private IMainActivity mActivity;
    private boolean isFragment0Waiting; // Fragment0初始化已完成但数据还在加载，Fragment0等待数据完毕通知
    private boolean isFragment1Waiting; // Fragment1初始化已完成但数据还在加载，Fragment1等待数据完毕通知
    private boolean isRequestDataFinished; // 网络请求数据完毕
    private int mErrCode; // 错误码

    public MainActivityPresenter(IMainActivity activity) {
        mActivity = activity;
    }

    /**
     * Fragment初始化完成
     */
    @Override
    public void onFragmentInitOK(int fragmentId) {
        if (fragmentId == 0 && !isRequestDataFinished)
            isFragment0Waiting = true;
        if (fragmentId == 1 && !isRequestDataFinished)
            isFragment1Waiting = true;
    }

    /**
     * Fragment下拉刷新请求
     */
    @Override
    public void onFragmentRefreshCheckData(int fragmentId) {
        if (mReleasedMovies != null && mWillReleaseMovies != null && mActivity != null) {
            mActivity.onFragmentRefreshDataReady(fragmentId);
        } else {
            isFragment0Waiting = isFragment1Waiting = true;

            if (isRequestDataFinished)
                loadMovieData();
        }
    }

    /**
     * 网络请求影讯数据
     */
    @Override
    public void loadMovieData() {
        String city = PrefUtil.getCity();
        ApiHelper.loadMovies(city)
                .subscribe(new ApiSubscriber<MovieReleaseType[]>() {

                    @Override
                    public void onStart() {
                        isRequestDataFinished = false;
                    }

                    @Override
                    public void onNext(MovieReleaseType[] movieReleaseTypes) {
                        if (movieReleaseTypes != null && movieReleaseTypes.length == 2) {
                            mReleasedMovies = movieReleaseTypes[0].getData();
                            mWillReleaseMovies = movieReleaseTypes[1].getData();

                            /**
                             * 如果Fragment处于等待状态，则通知其数据已经准备好
                             */
                            if (isFragment0Waiting)
                                mActivity.onFragmentRefreshDataReady(0);
                            if (isFragment1Waiting)
                                mActivity.onFragmentRefreshDataReady(1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            mErrCode = ((ApiException) e).getCode();
                        }
                        super.onError(e);
                    }

                    @Override
                    protected void onError(String msg) {
                        if (mActivity != null) {
                            mActivity.onDataError(mErrCode, msg);
                        }
                    }

                    @Override
                    public void onFinally() {
                        isRequestDataFinished = true;
                    }

                });
    }

    @Override
    public List<MovieModel> getMovieModels(int fragmentId) {
        return fragmentId == 0 ? mReleasedMovies : mWillReleaseMovies;
    }

    @Override
    public void unregister() {
        if (mReleasedMovies != null)
            mReleasedMovies.clear();
        if (mWillReleaseMovies != null)
            mWillReleaseMovies.clear();
        mActivity = null;
    }
}

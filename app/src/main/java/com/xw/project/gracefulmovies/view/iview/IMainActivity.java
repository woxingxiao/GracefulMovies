package com.xw.project.gracefulmovies.view.iview;

import com.xw.project.gracefulmovies.presenter.IMainActivityPresenter;

/**
 * <p/>
 * Created by woxingxiao on 2017-04-01.
 */

public interface IMainActivity {

    IMainActivityPresenter getPresenter();

    void onFragmentRefreshDataReady(int fragmentId);

    void onDataError(int code, String msg);
}

package com.xw.project.gracefulmovies.presenter;

import com.xw.project.gracefulmovies.view.iview.IBoxOfficeActivity;

/**
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public interface IBoxOfficeActivityPresenter {

    void register(IBoxOfficeActivity activity);

    void loadData();

    void unregister();
}

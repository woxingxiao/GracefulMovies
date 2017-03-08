package com.xw.project.gracefulmovies.view.iview;

import android.content.Context;

import com.xw.project.gracefulmovies.model.BoxOfficeModel;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public interface IBoxOfficeListFragment {

    Context getContext();

    void onDataReady(List<BoxOfficeModel> modelList);

    void onDataError(int code, String msg);
}

package com.xw.project.gracefulmovies.view.iview;

import com.xw.project.gracefulmovies.model.MovieSearchModel;

import java.util.ArrayList;

/**
 * <p></>
 * Created by woxingxiao on 2017-04-16.
 */

public interface ISearchActivity {

    void onUpdateSearchViewAdapter(ArrayList<String> histories);

    void onDisplayUI(MovieSearchModel model);

    void onDataFailed(String msg);
}

package com.xw.project.gracefulmovies.presenter;

/**
 * <p/>
 * Created by woxingxiao on 2017-04-16.
 */

public interface ISearchActivityPresenter {

    void readHistoryFormFile();

    void dealWithSearchHistory(String query);

    void doSearch(String query);

    void writeHistoryToFile();

    void unregister();
}

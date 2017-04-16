package com.xw.project.gracefulmovies.presenter.impl;

import android.app.Activity;

import com.xw.project.gracefulmovies.model.MovieSearchModel;
import com.xw.project.gracefulmovies.presenter.ISearchActivityPresenter;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.server.ApiSubscriber;
import com.xw.project.gracefulmovies.view.iview.ISearchActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <p/>
 * Created by woxingxiao on 2017-04-16.
 */

public class SearchActivityPresenterImpl implements ISearchActivityPresenter {

    private static final int MAX_SIZE = 10;
    private static String CACHE_HISTORY_FILE;

    private ISearchActivity mActivity;
    private ArrayList<String> mHistoryList = new ArrayList<>();
    private CompositeSubscription mSubscription = new CompositeSubscription();

    public SearchActivityPresenterImpl(ISearchActivity activity) {
        this.mActivity = activity;

        CACHE_HISTORY_FILE = ((Activity) mActivity).getCacheDir().getAbsolutePath() + "/search_history.json";
    }

    /**
     * 从本地json文件获取搜索历史
     */
    @Override
    public void readHistoryFormFile() {
        mSubscription.add(
                Observable.just("read history")
                        .map(new Func1<String, Void>() {
                            @Override
                            public Void call(String s) {
                                readData();

                                return null;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void s) {
                                mActivity.onUpdateSearchViewAdapter(mHistoryList);
                            }
                        })
        );
    }

    private void readData() {
        mHistoryList.clear();

        File file = new File(CACHE_HISTORY_FILE);
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));

                StringBuilder sb = new StringBuilder();
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mHistoryList.add(jsonArray.optString(i));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void dealWithSearchHistory(String query) {
        mSubscription.add(
                Observable.just(query)
                        .map(new Func1<String, Void>() {
                            @Override
                            public Void call(String query) {
                                dealData(query);

                                return null;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                mActivity.onUpdateSearchViewAdapter(mHistoryList);
                            }
                        })
        );
    }

    private void dealData(String query) {
        mHistoryList.add(0, query.trim());

        // 仅保留MAX_SIZE个数据
        int size = mHistoryList.size() > MAX_SIZE ? MAX_SIZE : mHistoryList.size();
        Iterator<String> iterator = mHistoryList.iterator();
        int index = 0;
        while (iterator.hasNext() && !iterator.next().isEmpty()) {
            if (index > size - 1) {
                iterator.remove();
            }
            index++;
        }

        // 去除重复数据
        ArrayList<String> list = new ArrayList<>();
        size = mHistoryList.size();
        String str;
        boolean isDuplicate;
        for (int i = 0; i < size; i++) {
            isDuplicate = false;
            str = mHistoryList.get(i);
            for (int j = 0; j < i; j++) {
                if (str.equals(mHistoryList.get(j))) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                list.add(str);
            }
        }

        mHistoryList = list;
    }

    @Override
    public void doSearch(String query) {
        mSubscription.add(
                ApiHelper.searchMovie(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ApiSubscriber<MovieSearchModel>() {
                            @Override
                            public void onNext(MovieSearchModel movieSearchModel) {
                                super.onNext(movieSearchModel);

                                mActivity.onDisplayUI(movieSearchModel);
                            }

                            @Override
                            protected void onError(String msg) {
                                mActivity.onDataFailed(msg);
                            }
                        })
        );
    }

    /**
     * 搜索历史保存到本地json文件
     */
    @Override
    public void writeHistoryToFile() {
        Observable.just("write history")
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        writeData();
                    }
                });
    }

    private void writeData() {
        File file = new File(CACHE_HISTORY_FILE);
        boolean ok = true;
        if (!file.exists()) {
            try {
                ok = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!ok) return;

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));

            StringBuilder sb = new StringBuilder();
            int size = mHistoryList.size();
            sb.append("[");
            for (int i = 0; i < size; i++) {
                sb.append("\"").append(mHistoryList.get(i)).append("\"");
                if (i < size - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");

            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void unregister() {
        mActivity = null;
        mSubscription.unsubscribe();
    }
}

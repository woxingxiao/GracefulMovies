package com.xw.project.gracefulmovies;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.xw.project.gracefulmovies.data.db.GMDatabase;
import com.xw.project.gracefulmovies.repository.CityRepository;
import com.xw.project.gracefulmovies.rx.RxSchedulers;
import com.xw.project.gracefulmovies.rx.SimpleConsumer;
import com.xw.project.gracefulmovies.util.Logy;

import io.reactivex.Observable;

/**
 * <p>
 * Created by woxingxiao on 2018-08-14.
 */
public class GMApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static boolean DEBUG = false;
    public static GMApplication sApplication;

    private GMDatabase mDatabase;
    private CityRepository mCityRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new SimpleConsumer<String>() {
                    @Override
                    public void accept(String it) {
                        mDatabase = GMDatabase.createAsync(sApplication);
                    }
                });
        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new SimpleConsumer<String>() {
                    @Override
                    public void accept(String it) {
                        Logy.init(GMApplication.DEBUG);
//                        CrashHandler.getInstance().init(getApplicationContext());
                    }
                });
    }

    public static GMApplication getInstance() {
        return sApplication;
    }

    public GMDatabase getDatabase() {
        return mDatabase;
    }

    public CityRepository getCityRepository() {
        if (mCityRepository == null) {
            mCityRepository = new CityRepository();
        }
        return mCityRepository;
    }
}

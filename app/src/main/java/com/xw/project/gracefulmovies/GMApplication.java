package com.xw.project.gracefulmovies;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.oubowu.slideback.ActivityHelper;
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
    private ActivityHelper mActivityHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        Observable.just("")
                .map(s -> {
                    mDatabase = GMDatabase.createAsync(sApplication);
                    return true;
                })
                .compose(RxSchedulers.applyIO())
                .subscribe(new SimpleConsumer<Boolean>() {
                    @Override
                    public void accept(Boolean it) {
                        GMApplication.getInstance().getCityRepository().init();
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

        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityHelper);
    }

    public static GMApplication getInstance() {
        return sApplication;
    }

    public static ActivityHelper getActivityHelper() {
        return sApplication.mActivityHelper;
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

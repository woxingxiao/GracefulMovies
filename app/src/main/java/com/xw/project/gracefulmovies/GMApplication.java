package com.xw.project.gracefulmovies;

import android.app.Application;

import com.oubowu.slideback.ActivityHelper;

/**
 * <p/>
 * Created by woxingxiao on 2017-2017-01-25.
 */
public class GMApplication extends Application {

    public static boolean DEBUG = false;

    public static GMApplication sGMApplication;

    private ActivityHelper mActivityHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityHelper);

        sGMApplication = this;
    }

    public static ActivityHelper getActivityHelper() {
        return sGMApplication.mActivityHelper;
    }
}

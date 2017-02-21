package com.xw.project.gracefulmovies;

import android.app.Application;

import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.util.Logy;
import com.xw.project.gracefulmovies.util.SharedPrefHelper;

import org.polaric.colorful.Colorful;

/**
 * <p/>
 * Created by woxingxiao on 2017-01-25.
 */
public class GMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Colorful.defaults()
                .primaryColor(Colorful.ThemeColor.DARK)
                .accentColor(Colorful.ThemeColor.DEEP_ORANGE)
                .translucent(false)
                .night(false);
        Colorful.init(this);

        SharedPrefHelper.init(this);
        Logy.init(true);
        ApiHelper.init(getString(R.string.api_key));
//        CrashHandler.getInstance().init(this);
    }
}

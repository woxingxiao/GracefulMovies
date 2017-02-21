package com.xw.project.gracefulmovies.util;

import android.content.res.Resources;

/**
 * 全局助手类
 * <p/>
 * Created by woxingxiao on 2017-02-11.
 */
public final class Util {

    /**
     * @return 状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}

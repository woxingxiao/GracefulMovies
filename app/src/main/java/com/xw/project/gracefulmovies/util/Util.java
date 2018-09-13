package com.xw.project.gracefulmovies.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 全局助手类
 * <p>
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

    public static String trimCity(String city) {
        if (city.endsWith("市")) {
            return city.substring(0, city.lastIndexOf("市"));
        }
        if (city.endsWith("区")) {
            return city.substring(0, city.lastIndexOf("区"));
        }
        if (city.endsWith("县")) {
            return city.substring(0, city.lastIndexOf("县"));
        }
        if (city.endsWith("镇")) {
            return city.substring(0, city.lastIndexOf("镇"));
        }
        if (city.endsWith("乡")) {
            return city.substring(0, city.lastIndexOf("乡"));
        }

        return city;
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}

package com.oubowu.slideback.callbak;

import android.support.annotation.FloatRange;

/**
 * Created by Oubowu on 2016/9/22 0022 18:22.
 */
public interface OnSlideListener {

    void onSlide(@FloatRange(from = 0.0,
            to = 1.0) float percent);

    void onOpen();

    void onClose();

}

package com.oubowu.slideback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;


/**
 * Created by Oubowu on 2016/9/20 0020 11:19.
 */
public class CacheDrawView extends View {

    private View mCacheView;

    public CacheDrawView(Context context) {
        super(context);
    }

    public void drawCacheView(View cacheView) {
        mCacheView = cacheView;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCacheView != null) {
            //            canvas.drawColor(Color.YELLOW);
            mCacheView.draw(canvas);
            // Log.e("TAG", "绘制上个Activity的内容视图...");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Log.e("TAG", "CacheDrawView-37行-onDetachedFromWindow(): ");
        mCacheView = null;
    }
}

package com.xw.project.gracefulmovies.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewTreeObserver;

/**
 * <p/>
 * Created by woxingxiao on 2017-06-26.
 */
public class AutoFitSizeTextView extends AppCompatTextView {

    public AutoFitSizeTextView(Context context) {
        this(context, null, 0);
    }

    public AutoFitSizeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float textWidth = getPaint().measureText(getText().toString());
                int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

                float textSize = getTextSize();
                if (width < textWidth) {
                    textSize = (width / textWidth) * textSize;
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
            }
        });
    }
}
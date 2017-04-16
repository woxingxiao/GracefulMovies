package com.xw.project.gracefulmovies.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;

import com.xw.project.gracefulmovies.R;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-19.
 */

public class QuoteTextView extends AppCompatTextView {

    private boolean isNight;

    public QuoteTextView(Context context) {
        this(context, null);
    }

    public QuoteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_primary_dark));
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_background_day));
    }

    public void setTextWithHtml(String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            setText(Html.fromHtml(text));
        } else {
            setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        }
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
        if (isNight) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_primary_light));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_background_night));
        } else {
            setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_primary_dark));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_background_day));
        }
        postInvalidate();
    }

}

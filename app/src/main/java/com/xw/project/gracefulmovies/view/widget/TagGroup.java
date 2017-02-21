package com.xw.project.gracefulmovies.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.project.gracefulmovies.R;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-14.
 */
public class TagGroup extends LinearLayout {

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);
    }

    public void setTagData(List<String> tags, @ColorRes int tintColorRes) {
        if (tags == null || tags.isEmpty()) {
            return;
        }

        removeAllViews();

        int tintColor = ContextCompat.getColor(getContext(), tintColorRes);
        for (String tag : tags) {
            createTag(tag, tintColor);
        }

        requestLayout();
    }

    private void createTag(String s, @ColorInt int tintColor) {
        TextView textView = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.rightMargin = dp2px(4);
        textView.setLayoutParams(lp);
        textView.setTextSize(9);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.shape_round_stroke_bg_tag);
        DrawableCompat.setTint(drawable, tintColor);
        if (Build.VERSION.SDK_INT > 15)
            textView.setBackground(drawable);
        else
            textView.setBackgroundDrawable(drawable);
        textView.setPadding(dp2px(2), 0, dp2px(2), 0);
        textView.setText(s);

        addView(textView);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}

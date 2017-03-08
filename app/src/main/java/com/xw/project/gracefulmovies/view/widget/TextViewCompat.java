package com.xw.project.gracefulmovies.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.xw.project.gracefulmovies.R;

/**
 * <p/>
 * Created by woxingxiao on 2017-01-29.
 */
public class TextViewCompat extends AppCompatTextView {

    public TextViewCompat(Context context) {
        super(context);
    }

    public TextViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public TextViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewCompat);

            Drawable dl = null;
            Drawable dt = null;
            Drawable dr = null;
            Drawable db = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dl = a.getDrawable(R.styleable.TextViewCompat_drawableLeftCompat);
                dt = a.getDrawable(R.styleable.TextViewCompat_drawableTopCompat);
                dr = a.getDrawable(R.styleable.TextViewCompat_drawableRightCompat);
                db = a.getDrawable(R.styleable.TextViewCompat_drawableBottomCompat);
            } else {
                int dlId = a.getResourceId(R.styleable.TextViewCompat_drawableLeftCompat, -1);
                int dtId = a.getResourceId(R.styleable.TextViewCompat_drawableTopCompat, -1);
                int drId = a.getResourceId(R.styleable.TextViewCompat_drawableRightCompat, -1);
                int dbId = a.getResourceId(R.styleable.TextViewCompat_drawableBottomCompat, -1);

                if (dlId != -1)
                    dl = AppCompatResources.getDrawable(context, dlId);
                if (dtId != -1)
                    dt = AppCompatResources.getDrawable(context, dtId);
                if (drId != -1)
                    dr = AppCompatResources.getDrawable(context, drId);
                if (dbId != -1)
                    db = AppCompatResources.getDrawable(context, dbId);
            }

            boolean isTint = a.getBoolean(R.styleable.TextViewCompat_tintDrawableWithTextColor, false);
            if (isTint && dl != null)
                DrawableCompat.setTint(dl, getCurrentTextColor());
            if (isTint && dt != null)
                DrawableCompat.setTint(dt, getCurrentTextColor());
            if (isTint && dr != null)
                DrawableCompat.setTint(dr, getCurrentTextColor());
            if (isTint && db != null)
                DrawableCompat.setTint(db, getCurrentTextColor());

            setCompoundDrawablesWithIntrinsicBounds(dl, dt, dr, db);
            a.recycle();
        }
    }
}
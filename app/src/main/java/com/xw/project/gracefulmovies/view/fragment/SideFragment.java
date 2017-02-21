package com.xw.project.gracefulmovies.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.util.Util;
import com.xw.project.gracefulmovies.view.activity.ThemeActivity;
import com.xw.project.gracefulmovies.view.widget.QuoteTextView;
import com.yalantis.starwarsdemo.widget.ClipRevealFrame;

import org.polaric.colorful.ColorPickerDialog;
import org.polaric.colorful.Colorful;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xw.project.gracefulmovies.R.id.toolbar;

/**
 * 日间、夜间主题
 * Created by woxignxiao on 2017-02-08.
 */
public abstract class SideFragment extends Fragment {

    public static final String ARG_CX = "cx";
    public static final String ARG_CY = "cy";
    public static final String ARG_SHOULD_EXPAND = "should expand";
    private static final long ANIM_DURATION = 250L;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(toolbar)
    Toolbar mToolbar;
    @BindView(R.id.header_bg_img)
    ImageView mHeaderImg;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.nest_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.day_night_mode_text)
    TextView mDayNightModeText;
    @BindView(R.id.day_night_switch)
    SwitchCompat mSwitch;
    @BindView(R.id.primary_color_text)
    TextView mPrimaryColorText;
    @BindView(R.id.primary_color_img)
    AppCompatImageView mPrimaryColorImg;
    @BindView(R.id.accent_color_text)
    TextView mAccentColorText;
    @BindView(R.id.accent_color_img)
    AppCompatImageView mAccentColorImg;
    @BindView(R.id.quote_text)
    QuoteTextView mQuoteText;

    private ThemeActivity mThemeActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ThemeActivity) {
            mThemeActivity = (ThemeActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.cloneInContext(new ContextThemeWrapper(getContext(), getTheme()))
                .inflate(R.layout.fragment_side, container, false);
        final Bundle args = getArguments();
        if (args != null) {
            rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                           int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    int cx = args.getInt("cx");
                    int cy = args.getInt("cy");
                    // get the hypotheses so the mRadius is from one corner to the other
                    float radius = (float) Math.hypot(right, bottom);

                    // Hardware-supported clipPath()
                    // http://developer.android.com/guide/topics/graphics/hardware-accel.html
                    if (Build.VERSION.SDK_INT >= 18) {
                        Animator reveal = createCheckoutRevealAnimator((ClipRevealFrame) v, cx, cy, 28f, radius);
                        reveal.start();
                    } else {
                        removeOldSideFragment();
                    }
                }
            });
        }

        ButterKnife.bind(this, rootView);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mAppBarLayout.setExpanded(getArguments().getBoolean(ARG_SHOULD_EXPAND), false);
        }

        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = Util.getStatusBarHeight();
        mToolbar.setLayoutParams(lp);

        playFabAnimation();

        displayUI();

        mThemeActivity.setSupportActionBar(mToolbar);
        if (mThemeActivity.getSupportActionBar() != null) {
            mThemeActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                onSideSwitch(mSwitch);

                Colorful.config(getContext()).night(checked).apply();
            }
        });

        mFab.setImageDrawable(tintDrawableWithColor(R.drawable.svg_ic_windmill,
                Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        mPrimaryColorImg.setImageDrawable(tintDrawableWithColor(R.drawable.svg_ic_suit,
                Colorful.getThemeDelegate().getPrimaryColor().getColorRes()));
        mAccentColorImg.setImageDrawable(tintDrawableWithColor(R.drawable.svg_ic_tie,
                Colorful.getThemeDelegate().getAccentColor().getColorRes()));

        String[] quotes = getResources().getStringArray(R.array.quotes);
        int index = new Random().nextInt(quotes.length);
        mQuoteText.setTextWithHtml(quotes[index]);
    }

    private void playFabAnimation() {
        mFab.setScaleX(0f);
        mFab.setScaleY(0f);
        mFab.setAlpha(0f);

        mFab.animate().cancel();
        mFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFab.animate()
                                .rotation(360 * 2)
                                .setDuration(1500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mFab.setRotation(0);
                                        mFab.animate()
                                                .scaleX(0f)
                                                .scaleY(0f)
                                                .alpha(0f)
                                                .setDuration(200)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        mFab.animate().cancel();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    protected Animator createCheckoutRevealAnimator(final ClipRevealFrame view, int x, int y, float startRadius, float endRadius) {
        setMenuVisibility(false);
        Animator retAnimator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            retAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            view.setClipRadius(startRadius);

            retAnimator = ObjectAnimator.ofFloat(view, "clipRadius", startRadius, endRadius);
        }
        retAnimator.setDuration(ANIM_DURATION);
        retAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setClipOutLines(false);
                removeOldSideFragment();
            }
        });

        retAnimator.setInterpolator(new AccelerateInterpolator(2.0f));
        return retAnimator;
    }

    private void removeOldSideFragment() {
        mThemeActivity.removeAllFragmentExcept(getTagString());
    }

    public void onSideSwitch(SwitchCompat v) {
        Rect rect = new Rect();
        v.getGlobalVisibleRect(rect);
        final int cy = rect.centerY();
        final int halfThumbWidth = v.getThumbDrawable().getIntrinsicWidth() / 2;
        final int cx;

        if (this instanceof DayModeFragment && v.isChecked()) {
            cx = rect.right - halfThumbWidth;
            mThemeActivity.switchModeFragment(cx, cy, isAppBarExpanded(), NightModeFragment.TAG);
        } else if (!v.isChecked()) {
            cx = rect.left + halfThumbWidth;
            mThemeActivity.switchModeFragment(cx, cy, isAppBarExpanded(), DayModeFragment.TAG);
        }
    }

    private boolean isAppBarExpanded() {
        return mAppBarLayout != null && mAppBarLayout.getBottom() == mAppBarLayout.getHeight();
    }

    @OnClick({R.id.primary_color_img, R.id.accent_color_img})
    public void onClick(View view) {
        ColorPickerDialog dialog = new ColorPickerDialog(getContext());

        switch (view.getId()) {
            case R.id.primary_color_img:
                dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(Colorful.ThemeColor color) {
                        Colorful.config(getContext()).primaryColor(color).apply();

                        mCollapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getContext(), color.getColorRes()));
                        mFab.setBackgroundTintList(createColorStateList(color.getColorRes()));
                        mPrimaryColorImg.setImageDrawable(tintDrawableWithColor(R.drawable.svg_ic_suit,
                                color.getColorRes()));

                        playFabAnimation();
                    }
                });

                break;
            case R.id.accent_color_img:
                dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(Colorful.ThemeColor color) {
                        Colorful.config(getContext()).accentColor(color).apply();

                        mFab.setImageDrawable(tintDrawableWithColor(R.drawable.svg_ic_windmill,
                                color.getColorRes()));
                        mAccentColorImg.setImageDrawable(tintDrawableWithColor(R.drawable.svg_ic_tie,
                                color.getColorRes()));

                        playFabAnimation();
                    }
                });

                break;
        }

        dialog.show();
    }

    @StyleRes
    abstract int getTheme();

    abstract void displayUI();

    public abstract String getTagString();

    protected Drawable tintDrawableWithColor(@DrawableRes int drawableRes, @ColorRes int colorRes) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getContext(), colorRes));
        return drawable;
    }

    protected ColorStateList createColorStateList(@ColorRes int colorRes) {
        int color = ContextCompat.getColor(getContext(), colorRes);
        int[] colors = new int[]{color, color, color, color, color, color};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

}

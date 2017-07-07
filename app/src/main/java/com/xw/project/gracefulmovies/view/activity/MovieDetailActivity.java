package com.xw.project.gracefulmovies.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.util.Util;

import org.polaric.colorful.Colorful;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 影片详情
 * <p/>
 * Created by woxingxiao on 2017-02-09.
 */
public class MovieDetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.detail_poster_image)
    KenBurnsView mPosterImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.detail_rating_bar)
    SimpleRatingBar mRatingBar;
    @BindView(R.id.detail_rating_info_text)
    TextView mRatingInfoText;
    @BindView(R.id.detail_director_text)
    TextView mDirectorText;
    @BindView(R.id.detail_type_text)
    TextView mTypeText;
    @BindView(R.id.detail_starring_text)
    TextView mStarringText;
    @BindView(R.id.detail_story_brief_text)
    TextView mStoryBriefText;
    @BindView(R.id.detail_rating_text)
    TextView mRatingText;
    @BindView(R.id.detail_rating_layout)
    LinearLayout mRatingLayout;
    @BindView(R.id.detail_release_text)
    TextView mReleaseHintText;
    @BindView(R.id.detail_release_info_text)
    TextView mReleaseInfoText;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private boolean isCollapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();

        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        initializeToolbar();

        mAppBar.addOnOffsetChangedListener(this);
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams)
                mToolbar.getLayoutParams();
        lp.topMargin = Util.getStatusBarHeight();
        mToolbar.setLayoutParams(lp);

        final MovieModel movieModel = getIntent().getParcelableExtra(OBJ_1);
        if (movieModel == null) {
            mAppBar.removeOnOffsetChangedListener(this);
            mPosterImage.pause();
            return;
        }

        mToolbarLayout.setTitle(movieModel.getName());
        Glide.with(this).load(movieModel.getPoster()).into(mPosterImage);
        if (Colorful.getThemeDelegate().isNight()) {
            mPosterImage.setAlpha(0.7f);
        }
        if (movieModel.getGrade() != null) {
            mRatingBar.setRating((Float.valueOf(movieModel.getGrade())) / 2.0f);
            mRatingBar.setFillColor(ContextCompat.getColor(
                    this, Colorful.getThemeDelegate().getAccentColor().getColorRes()));

            mRatingInfoText.setText(movieModel.getGrade());
            mRatingInfoText.append("" + movieModel.getGradeNum());
            mReleaseHintText.setText(getString(R.string.release_info));
            mReleaseInfoText.setTextSize(16);
            mReleaseInfoText.setText(movieModel.getReleaseDateString());
            mReleaseInfoText.append("上映  当地" + movieModel.getCinemaNum());
        } else {
            mRatingText.setVisibility(View.GONE);
            mRatingLayout.setVisibility(View.GONE);
            mReleaseHintText.setText(getString(R.string.release_date));
            mReleaseInfoText.setText(movieModel.getReleaseDateString());
            mReleaseInfoText.append(" 上映");
        }
        mDirectorText.setText(movieModel.getDirector().getData().getLabel1().getName());
        mTypeText.setText(movieModel.getMovieTypeString());
        mStarringText.setText(movieModel.getCastString());

        String story = movieModel.getStory().getData().getStoryBrief();
        story += "更多";
        int index = story.length();
        SpannableStringBuilder ssb = new SpannableStringBuilder(story);
        ssb.setSpan(new ClickableSpan() { // 可点击
            @Override
            public void onClick(View view) {
                goToDetail(movieModel, view);
            }

            @Override
            public void updateDrawState(TextPaint p) {
                p.setColor(ContextCompat.getColor(MovieDetailActivity.this,
                        Colorful.getThemeDelegate().getAccentColor().getColorRes()));
                p.setUnderlineText(true);
            }

        }, index - 2, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStoryBriefText.setMovementMethod(LinkMovementMethod.getInstance());
        mStoryBriefText.setText(ssb);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetail(movieModel, view);
            }
        });
    }

    private void goToDetail(MovieModel movieModel, View view) {
        if (movieModel.getWebUrl() == null || movieModel.getWebUrl().isEmpty()) {
            showToast("无效地址");
        } else {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("url", movieModel.getWebUrl());
            intent.putExtra("title", movieModel.getName() == null ? "" : movieModel.getName());

            navigateWithRippleCompat(
                    this,
                    intent,
                    view,
                    Colorful.getThemeDelegate().getAccentColor().getColorRes()
            );
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { // 完全折叠状态
            if (!isCollapsed) {
                mPosterImage.pause();
            }
            isCollapsed = true;
        } else { // 非折叠状态
            if (isCollapsed) {
                mPosterImage.resume();
            }
            isCollapsed = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCollapsed) {
            mPosterImage.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPosterImage.pause();
    }
}

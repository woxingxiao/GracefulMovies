package com.xw.project.gracefulmovies.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.ao.Actor;
import com.xw.project.gracefulmovies.data.ao.Director;
import com.xw.project.gracefulmovies.data.ao.MovieDetail;
import com.xw.project.gracefulmovies.data.db.entity.CityEntity;
import com.xw.project.gracefulmovies.databinding.ActivityMovieDetailBinding;
import com.xw.project.gracefulmovies.ui.BindingAdapters;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;
import com.xw.project.gracefulmovies.ui.adapter.ActorsAdapter;
import com.xw.project.gracefulmovies.ui.adapter.StillsAdapter;
import com.xw.project.gracefulmovies.ui.fragment.PreviewImageFragment;
import com.xw.project.gracefulmovies.ui.widget.BlurTransformation;
import com.xw.project.gracefulmovies.util.Util;
import com.xw.project.gracefulmovies.viewmodel.MovieDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 影片详情
 * <p>
 * Created by woxingxiao on 2018-08-15.
 */
public class MovieDetailActivity extends BaseActivity<ActivityMovieDetailBinding> {

    private String mImageUrl;
    private PreviewImageFragment mPreviewImageFragment;

    private MovieDetailViewModel mViewModel;

    @Override
    protected int contentLayoutRes() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void afterSetContentView() {
        transparentStatusBar();

        String[] params = getIntent().getStringArrayExtra(OBJ_1);
        Glide.with(mActivity)
                .load(params[0])
                .transform(new BlurTransformation(this, 10))
                .into(mBinding.headerBgIv);
        BindingAdapters.loadImageUrl(mBinding.coverIv, params[1]);
        mBinding.nameTv.setText(params[2]);
        mBinding.titleTv.setText(params[2]);
        mImageUrl = params[1];

        int statusBarHeight = Util.getStatusBarHeight();
        FrameLayout.LayoutParams lp;
        lp = (FrameLayout.LayoutParams) mBinding.titleStatusView.getLayoutParams();
        lp.height = statusBarHeight;
        mBinding.titleStatusView.setLayoutParams(lp);
        mBinding.setStatusBarHeight(statusBarHeight);

        mBinding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final int partHeaderHeight = mBinding.headerBgIv.getMeasuredHeight() * 2 / 3;

                mBinding.scrollView.setOnScrollListener((x, y) -> {
                    if (y > partHeaderHeight) {
                        y = partHeaderHeight;
                    }

                    float alpha = y * 1f / partHeaderHeight;
                    if (alpha > 1) {
                        if (mBinding.titleStatusView.getAlpha() != 1) {
                            mBinding.titleStatusView.setAlpha(1);
                            mBinding.titleLayout.setAlpha(1);
                        }
                    } else if (alpha == 0) {
                        if (mBinding.titleStatusView.getAlpha() != 0) {
                            mBinding.titleStatusView.setAlpha(0);
                            mBinding.titleLayout.setAlpha(0);
                        }
                    } else {
                        mBinding.titleStatusView.setAlpha(alpha);
                        mBinding.titleLayout.setAlpha(alpha);
                    }
                });
            }
        });

        String locationId = "880";
        LiveData<CityEntity> liveData = GMApplication.getInstance().getDatabase().cityDao().loadCity();
        if (liveData != null && liveData.getValue() != null) {
            locationId = liveData.getValue().getId() + "";
        }
        mViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        mViewModel.getMovieDetails(locationId, getIntent().getIntExtra(PARAM_1, 0)).observe(this, resource -> {
            assert resource != null;
            MARGIN_TOP_DP = 0;
            processStatusView(resource);

            if (resource.getStatus() != DataResource.Status.SUCCESS) {
                return;
            }
            MovieDetail detail = resource.getData();
            if (detail == null || detail.basic == null || detail.boxOffice == null)
                return;

            mBinding.setInfo(detail.basic);
            mBinding.setBoxOffice(detail.boxOffice);

            mBinding.typeContainer.setTagData(detail.basic.type, R.color.colorAccent);

            List<String> formats = new ArrayList<>();
            if (detail.basic.is3D) {
                formats.add("3D");
            }
            if (detail.basic.isIMAX) {
                formats.add("IMAX");
            }
            if (detail.basic.isIMAX3D) {
                formats.add("IMAX3D");
            }
            mBinding.formatContainer.setTagData(formats, R.color.colorAccent);

            List<Actor> casts = detail.basic.actors;
            Director director = detail.basic.director;
            if (director != null) {
                Actor d = new Actor();
                d.name = director.name;
                d.img = director.img;
                d.roleName = "导演";
                if (casts == null) {
                    casts = new ArrayList<>();
                }
                casts.add(0, d);
            }
            mBinding.actorsRecyclerView.setAdapter(new ActorsAdapter(casts));
            mBinding.stillsRecyclerView.setAdapter(new StillsAdapter(detail.basic.stageImg.list));
        });

        mViewModel.load();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.go_back_iv) {
            onBackPressed();
        } else if (v.getId() == R.id.cover_iv) {
            ArrayList<String> urls = new ArrayList<>();
            urls.add(mImageUrl);
            gotoPreviewImages(urls, 0);
        } else if (v.getId() == R.id.box_office_ranking_tv) {
            navigate(BoxOfficeActivity.class);
        }
    }

    public void gotoPreviewImages(ArrayList<String> urls, int pos) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mPreviewImageFragment = new PreviewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("urls", urls);
        bundle.putInt("index", pos);
        mPreviewImageFragment.setArguments(bundle);
        ft.add(android.R.id.content, mPreviewImageFragment, PreviewImageFragment.class.getSimpleName());
        ft.commit();
    }

    @Override
    protected void onReload() {
        mViewModel.load();
    }

    @Override
    public void onBackPressed() {
        if (mPreviewImageFragment != null && !mPreviewImageFragment.exitFragment()) {
            return;
        }

        super.onBackPressed();
    }

    public static String convert(String dateStr) {
        String txt = "";
        if (!TextUtils.isEmpty(dateStr) && dateStr.length() == 8) {
            txt = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8) + "上映";
        }
        return txt;
    }
}

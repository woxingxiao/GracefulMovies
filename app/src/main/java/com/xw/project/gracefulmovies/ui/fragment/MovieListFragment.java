package com.xw.project.gracefulmovies.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;
import com.xw.project.gracefulmovies.databinding.FragmentMovieListBinding;
import com.xw.project.gracefulmovies.databinding.ItemMovieBinding;
import com.xw.project.gracefulmovies.ui.activity.MainActivity;
import com.xw.project.gracefulmovies.ui.adapter.MoviesAdapter;
import com.xw.project.gracefulmovies.ui.widget.BlurTransformation;
import com.xw.project.gracefulmovies.viewmodel.CityViewModel;
import com.xw.project.gracefulmovies.viewmodel.MovieViewModel;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

/**
 * 影片列表Fragment
 * <p>
 * Created by woxingxiao on 2017-01-23.
 */
public class MovieListFragment extends BaseFragment<FragmentMovieListBinding> implements
        DiscreteScrollView.ScrollStateChangeListener<MoviesAdapter.DataBindingVH<ItemMovieBinding>> {

    private boolean isNow;
    private boolean isIntentTriggered;
    private int mPreIntentPos;
    private BlurTransformation mBlurTransformation;
    private int mMaxIndex;
    private MoviesAdapter mAdapter;
    private MovieViewModel mMovieViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isNow = bundle.getBoolean(PARAM_1);
        }
        mBlurTransformation = new BlurTransformation(mActivity, 10);
    }

    @Override
    protected int viewLayoutRes() {
        return R.layout.fragment_movie_list;
    }

    @Override
    protected void afterInflateView() {
        mBinding.infiniteViewPager.setItemTransformer(
                new ScaleTransformer.Builder()
                        .setMinScale(0.8f)
                        .build()
        );
        mBinding.infiniteViewPager.addScrollStateChangeListener(this);

        mBinding.bgIv1.setAlpha(0f);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getMovieList(isNow).observe(this, resource -> {
            assert resource != null;

            processStatusView(mBinding.container, resource);

            if (resource.getStatus() == DataResource.Status.LOADING) {
                mBinding.infiniteViewPager.setVisibility(View.INVISIBLE);
            } else if (resource.getStatus() == DataResource.Status.SUCCESS) {
                mBinding.infiniteViewPager.setVisibility(View.VISIBLE);

                if (mAdapter == null) {
                    mAdapter = new MoviesAdapter(isNow, resource.getData());
                    mBinding.infiniteViewPager.setAdapter(mAdapter);
                } else {
                    mAdapter.setData(resource.getData());
                }
                mBinding.bgIv1.animate().alpha(1).setDuration(1000)
                        .withStartAction(() -> displayBgImage(0, mBinding.bgIv1));
                mBinding.bgIv2.animate().alpha(0).setDuration(1000)
                        .withEndAction(() -> displayBgImage(1, mBinding.bgIv2));
            } else if (resource.getStatus() == DataResource.Status.EMPTY) {
                ((MainActivity) mActivity).showLocatedCityDialog(true);
            }
        });
        CityViewModel cityViewModel = ViewModelProviders.of(this).get(CityViewModel.class);
        cityViewModel.getCity().observe(this, mMovieViewModel::setCity);

        mMovieViewModel.load();
    }

    private void displayBgImage(int index, ImageView imageView) {
        if (mActivity == null)
            return;
        List<MovieEntity> data = mAdapter.getData();
        if (data == null || data.isEmpty())
            return;

        Glide.with(mActivity)
                .load(mAdapter.getItem(index).getImageTiny())
                .transform(mBlurTransformation)
                .placeholder(R.drawable.pic_got)
                .error(R.drawable.pic_got)
                .into(imageView);
    }

    @Override
    protected void onReload() {
        mMovieViewModel.load();
    }

    @Override
    public void onScrollStart(@NonNull MoviesAdapter.DataBindingVH<ItemMovieBinding> currentItemHolder, int adapterPosition) {
        isIntentTriggered = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onScrollEnd(@NonNull MoviesAdapter.DataBindingVH<ItemMovieBinding> currentItemHolder, int adapterPosition) {
        List<MovieEntity> data = mAdapter.getData();
        if (data == null || data.isEmpty())
            return;

        final int size = data.size();
        mMaxIndex = adapterPosition > mMaxIndex ? adapterPosition : mMaxIndex;

        boolean isOdd = adapterPosition % 2 != 0;
        if (isOdd) {
            displayBgImage(adapterPosition, mBinding.bgIv2);

            if (mMaxIndex < adapterPosition + 1 && adapterPosition + 1 < size) { // 预加载右边一张
                displayBgImage(adapterPosition + 1, mBinding.bgIv1);
            }
        } else {
            displayBgImage(adapterPosition, mBinding.bgIv1);

            if (mMaxIndex < adapterPosition + 1 && adapterPosition + 1 < size) { // 预加载右边一张
                displayBgImage(adapterPosition + 1, mBinding.bgIv2);
            }
        }
    }

    @Override
    public void onScroll(float scrollPosition, int currentPosition, int newPosition,
                         @Nullable MoviesAdapter.DataBindingVH<ItemMovieBinding> currentHolder,
                         @Nullable MoviesAdapter.DataBindingVH<ItemMovieBinding> newCurrent) {
        List<MovieEntity> data = mAdapter.getData();
        if (data == null || data.isEmpty())
            return;

        final int size = data.size();
        final float position = Math.abs(scrollPosition);
        final boolean isOdd = currentPosition % 2 != 0;

        if (mPreIntentPos != newPosition) {
            isIntentTriggered = false;
        }
        if (isOdd) {
            if (!isIntentTriggered && newPosition >= 0 && newPosition <= size - 1) {
                displayBgImage(newPosition, mBinding.bgIv1);

                isIntentTriggered = true;
            }

            mBinding.bgIv1.setAlpha(position);
            mBinding.bgIv2.setAlpha(1 - position);
        } else {
            if (!isIntentTriggered && newPosition >= 0 && newPosition <= size - 1) {
                displayBgImage(newPosition, mBinding.bgIv2);

                isIntentTriggered = true;
            }

            mBinding.bgIv1.setAlpha(1 - position);
            mBinding.bgIv2.setAlpha(position);
        }
        mPreIntentPos = newPosition;

        float alpha;
        if (newCurrent != null) {
            alpha = position / 2 + 0.5f;
            newCurrent.getBinding().getRoot().setAlpha(alpha);
        }
        if (currentHolder != null) {
            alpha = -position / 2 + 1f;
            currentHolder.getBinding().getRoot().setAlpha(alpha);
        }
    }
}
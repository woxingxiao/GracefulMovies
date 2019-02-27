package com.xw.project.gracefulmovies.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.db.dao.MovieDao;
import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;
import com.xw.project.gracefulmovies.databinding.FragmentMovieListBinding;
import com.xw.project.gracefulmovies.ui.adapter.MoviesAdapter;
import com.xw.project.gracefulmovies.ui.widget.BlurTransformation;
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
        DiscreteScrollView.ScrollStateChangeListener<MoviesAdapter.BaseBindingVH> {

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
        mMovieViewModel.getMovieList(isNow).observe(getViewLifecycleOwner(), resource -> {
            assert resource != null;

            processStatusView(mBinding.container, resource);

            if (resource.getStatus() == DataResource.Status.LOADING) {
                mBinding.infiniteViewPager.setVisibility(View.INVISIBLE);
            } else if (resource.getStatus() == DataResource.Status.SUCCESS) {
                MovieDao dao = GMApplication.getInstance().getDatabase().movieDao();
                Observer<List<MovieEntity>> observer = movies -> {
                    if (mAdapter == null) {
                        mAdapter = new MoviesAdapter(isNow, movies);
                        mBinding.infiniteViewPager.setAdapter(mAdapter);
                    } else {
                        mAdapter.setData(movies);
                        try {
                            mBinding.infiniteViewPager.smoothScrollToPosition(0);
                        } catch (IllegalArgumentException ignored) {
                        }
                    }

                    mBinding.infiniteViewPager.setVisibility(View.VISIBLE);
                    mBinding.bgIv1.animate().alpha(1).setDuration(1000)
                            .withStartAction(() -> displayBgImage(0, mBinding.bgIv1));
                    mBinding.bgIv2.animate().alpha(0).setDuration(1000)
                            .withEndAction(() -> displayBgImage(1, mBinding.bgIv2));
                };
                if (isNow) {
                    dao.loadMovieNowList().observe(getViewLifecycleOwner(), observer);
                } else {
                    dao.loadMovieFutureList().observe(getViewLifecycleOwner(), observer);
                }
            }
        });
        GMApplication.getInstance().getCityRepository().getCity()
                .observe(getViewLifecycleOwner(), mMovieViewModel::setCity);
    }

    @Override
    protected void onLazyLoad() {
        mMovieViewModel.load();
    }

    private void displayBgImage(int index, ImageView imageView) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        List<MovieEntity> data = mAdapter.getData();
        if (data == null || data.isEmpty() || index >= data.size())
            return;

        Glide.with(activity)
                .load(data.get(index).getImageTiny())
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
    public void onScrollStart(@NonNull MoviesAdapter.BaseBindingVH currentItemHolder, int adapterPosition) {
        isIntentTriggered = true;
    }

    @Override
    public void onScrollEnd(@NonNull MoviesAdapter.BaseBindingVH currentItemHolder, int adapterPosition) {
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
                         @Nullable MoviesAdapter.BaseBindingVH currentHolder,
                         @Nullable MoviesAdapter.BaseBindingVH newCurrent) {
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
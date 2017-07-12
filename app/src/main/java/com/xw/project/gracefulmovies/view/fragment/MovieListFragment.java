package com.xw.project.gracefulmovies.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.util.BlurTransformation;
import com.xw.project.gracefulmovies.view.activity.MainActivity;
import com.xw.project.gracefulmovies.view.adapter.MovieListAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 影片列表Fragment
 * <p/>
 * Created by woxingxiao on 2017-01-23.
 */
public class MovieListFragment extends Fragment implements
        DiscreteScrollView.ScrollStateChangeListener<MovieListAdapter.MovieVH> {

    @BindView(R.id.bg_img1)
    ImageView mBgImg1;
    @BindView(R.id.bg_img2)
    ImageView mBgImg2;
    @BindView(R.id.infinite_view_pager)
    DiscreteScrollView mInfiniteViewPager;

    private MainActivity mActivity;
    private List<MovieModel> mMovieModels;
    private int mId;
    private boolean isIntentTriggered;
    private int mPreIntentPos;
    private int mPrePos;
    private BlurTransformation mBlurTransformation;
    private int mMaxIndex;

    public static MovieListFragment newInstance(int id) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        mInfiniteViewPager.setItemTransformer(
                new ScaleTransformer.Builder()
                        .setMinScale(0.8f)
                        .build()
        );
        mInfiniteViewPager.addScrollStateChangeListener(this);

        mBlurTransformation = new BlurTransformation(getActivity(), 10);
        mBgImg1.setAlpha(0f);
        mBgImg2.setImageResource(R.drawable.pic_bg_ocean);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mId = getArguments().getInt("id");
        /**
         * Fragment初始化完成告知MainActivityPresenter，并索要数据，如果：
         * 1. 数据已准备好，直接回调装载数据；
         * 2. 数据还在加载，等待加载完毕再回调装载数据。
         */
        mActivity.getPresenter().onFragmentInitOK(mId);
        mActivity.getPresenter().onFragmentRefreshRequest(mId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    public void onRefresh() {
        if (mMovieModels == null) {
            mActivity.getPresenter().onFragmentRefreshRequest(mId);
        }
    }

    public void onDataReady(List<MovieModel> movieModels) {
        mMovieModels = movieModels;

        mInfiniteViewPager.setVisibility(View.VISIBLE);
        mInfiniteViewPager.setAdapter(new MovieListAdapter(mMovieModels));
        mBgImg1.animate().alpha(1).setDuration(1000)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        displayBgImage(0, mBgImg1);
                    }
                });
        mBgImg2.animate().alpha(0).setDuration(1000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        displayBgImage(1, mBgImg2);
                    }
                });
    }

    public void onDataError() {

    }

    private void displayBgImage(int index, ImageView imageView) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Glide.with(activity)
                    .load(mMovieModels.get(index).getPoster())
                    .transform(mBlurTransformation)
                    .placeholder(R.drawable.pic_bg_ocean)
                    .error(R.drawable.pic_bg_ocean)
                    .crossFade()
                    .into(imageView);
        }
    }

    @Override
    public void onScrollStart(@NonNull MovieListAdapter.MovieVH currentItemHolder, int adapterPosition) {
        isIntentTriggered = true;
    }

    @Override
    public void onScrollEnd(@NonNull MovieListAdapter.MovieVH currentItemHolder, int adapterPosition) {
        mMaxIndex = adapterPosition > mMaxIndex ? adapterPosition : mMaxIndex;

        boolean isOdd = adapterPosition % 2 != 0;
        if (isOdd) {
            displayBgImage(adapterPosition, mBgImg2);

            if (mMaxIndex < adapterPosition + 1 && adapterPosition + 1 < mMovieModels.size()) { // 预加载右边一张
                displayBgImage(adapterPosition + 1, mBgImg1);
            }
        } else {
            displayBgImage(adapterPosition, mBgImg1);

            if (mMaxIndex < adapterPosition + 1 && adapterPosition + 1 < mMovieModels.size()) { // 预加载右边一张
                displayBgImage(adapterPosition + 1, mBgImg2);
            }
        }

        currentItemHolder.mNameText.setAlpha(1f);
        if (currentItemHolder.mInfoLayout.getVisibility() == View.VISIBLE) {
            currentItemHolder.mInfoLayout.setAlpha(1f);
        }
        int size = mMovieModels.size();
        if (mMovieModels != null && size > 0) {
            MovieListAdapter.MovieVH vh;

            if (adapterPosition == 0 && size > 1) {
                vh = (MovieListAdapter.MovieVH) mInfiniteViewPager.getViewHolder(adapterPosition + 1);
                if (vh != null) {
                    vh.mNameText.setAlpha(0);

                    if (mPrePos != adapterPosition) {
                        vh.mOpenInfoImg.setVisibility(View.VISIBLE);
                        vh.mInfoLayout.setVisibility(View.INVISIBLE);
                    }
                }
            } else if (adapterPosition == size - 1 && size > 1) {
                vh = (MovieListAdapter.MovieVH) mInfiniteViewPager.getViewHolder(adapterPosition - 1);
                if (vh != null) {
                    vh.mNameText.setAlpha(0);

                    if (mPrePos != adapterPosition) {
                        vh.mOpenInfoImg.setVisibility(View.VISIBLE);
                        vh.mInfoLayout.setVisibility(View.INVISIBLE);
                    }
                }
            } else if (adapterPosition > 0 && adapterPosition + 1 < size - 1) {
                vh = (MovieListAdapter.MovieVH) mInfiniteViewPager.getViewHolder(adapterPosition + 1);
                if (vh != null) {
                    vh.mNameText.setAlpha(0);

                    if (mPrePos != adapterPosition) {
                        vh.mOpenInfoImg.setVisibility(View.VISIBLE);
                        vh.mInfoLayout.setVisibility(View.INVISIBLE);
                    }
                }
                vh = (MovieListAdapter.MovieVH) mInfiniteViewPager.getViewHolder(adapterPosition - 1);
                if (vh != null) {
                    vh.mNameText.setAlpha(0);

                    if (mPrePos != adapterPosition) {
                        vh.mOpenInfoImg.setVisibility(View.VISIBLE);
                        vh.mInfoLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        mPrePos = adapterPosition;
    }

    @Override
    public void onScroll(float scrollPosition, @NonNull MovieListAdapter.MovieVH currentHolder, @NonNull MovieListAdapter.MovieVH newCurrent) {
        float position = Math.abs(scrollPosition);
        boolean isOdd = currentHolder.getAdapterPosition() % 2 != 0;
        int intentPos = newCurrent.getAdapterPosition();

        if (mPreIntentPos != intentPos) {
            isIntentTriggered = false;
        }
        if (isOdd) {
            if (!isIntentTriggered && intentPos >= 0 && intentPos <= mMovieModels.size() - 1) {
                displayBgImage(intentPos, mBgImg1);

                isIntentTriggered = true;
            }

            mBgImg1.setAlpha(position);
            mBgImg2.setAlpha(1 - position);
        } else {
            if (!isIntentTriggered && intentPos >= 0 && intentPos <= mMovieModels.size() - 1) {
                displayBgImage(intentPos, mBgImg2);

                isIntentTriggered = true;
            }

            mBgImg1.setAlpha(1 - position);
            mBgImg2.setAlpha(position);
        }
        mPreIntentPos = intentPos;

        float fastAlpha = position + 0.4f;
        currentHolder.mNameText.setAlpha(1 - fastAlpha);
        newCurrent.mNameText.setAlpha(fastAlpha);
        if (currentHolder.mInfoLayout.getVisibility() == View.VISIBLE) {
            currentHolder.mInfoLayout.setAlpha(1 - fastAlpha);
        }
    }
}

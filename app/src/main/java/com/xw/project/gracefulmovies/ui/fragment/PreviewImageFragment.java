package com.xw.project.gracefulmovies.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.databinding.FragmentPreviewImageBinding;
import com.xw.project.gracefulmovies.databinding.ItemImagePreviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by woxingxiao on 2018-05-22.
 */

public class PreviewImageFragment extends BaseFragment<FragmentPreviewImageBinding> {

    private int mSize;
    private boolean hasDismissed;

    @Override
    protected int viewLayoutRes() {
        return R.layout.fragment_preview_image;
    }

    @Override
    protected void afterInflateView() {
        MyVPAdapter adapter = new MyVPAdapter(this);
        Bundle bundle = getArguments();
        int index = 0;
        if (bundle != null) {
            ArrayList<String> urls = bundle.getStringArrayList("urls");
            if (urls != null)
                mSize = urls.size();
            index = bundle.getInt("index");

            adapter.setUrls(urls);
        }
        mBinding.previewViewPager.setAdapter(adapter);
        mBinding.previewViewPager.setCurrentItem(index);
        mBinding.previewViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBinding.previewIndicatorTv.setText(mActivity.getString(R.string.format_indicator, position + 1, mSize));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (mSize > 0) {
            mBinding.previewIndicatorTv.setText(mActivity.getString(R.string.format_indicator, index + 1, mSize));
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(mActivity, R.anim.anim_fade_in_short);
        } else {
            return AnimationUtils.loadAnimation(mActivity, R.anim.anim_fade_out_short);
        }
    }

    public boolean exitFragment() {
        if (mActivity != null && !hasDismissed) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.remove(this);
            ft.commitAllowingStateLoss();

            hasDismissed = true;

            return false;
        }

        return hasDismissed;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static class MyVPAdapter extends PagerAdapter {

        private PreviewImageFragment mFragment;
        private SparseArray<View> views = new SparseArray<>();
        private List<String> mUrls;

        MyVPAdapter(PreviewImageFragment fragment) {
            mFragment = fragment;
        }

        void setUrls(List<String> urls) {
            mUrls = urls;
        }

        @Override
        public int getCount() {
            if (mUrls == null)
                return 0;
            return mUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view;
            if (views.get(position) == null) {
                Context context = container.getContext();

                ItemImagePreviewBinding binding = ItemImagePreviewBinding.inflate(LayoutInflater.from(context));
                view = binding.getRoot();
                binding.previewPhotoView.setOnViewTapListener((v, x, y) -> mFragment.exitFragment());

                Glide.with(context)
                        .load(mUrls.get(position))
                        .placeholder(R.drawable.pic_place_holder)
                        .error(R.drawable.pic_error)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                binding.previewProgressBar.setVisibility(View.INVISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                binding.previewProgressBar.setVisibility(View.INVISIBLE);
                                return false;
                            }
                        })
                        .into(binding.previewPhotoView);

                views.put(position, view);
            } else {
                view = views.get(position);
            }

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }
    }
}

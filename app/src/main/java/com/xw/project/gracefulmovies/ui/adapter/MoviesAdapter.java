package com.xw.project.gracefulmovies.ui.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.View;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;
import com.xw.project.gracefulmovies.databinding.ItemMovieBinding;
import com.xw.project.gracefulmovies.ui.activity.MovieDetailActivity;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;
import com.xw.project.gracefulmovies.ui.adapter.base.BaseBindingListAdapter;

import java.util.List;

/**
 * 电影列表适配器
 * <p>
 * Created by woxignxiao on 2018-09-03.
 */
public class MoviesAdapter extends BaseBindingListAdapter<MovieEntity> {

    private boolean isNow;

    public MoviesAdapter(boolean isNow, List<MovieEntity> data) {
        super(data, new DiffUtil.ItemCallback<MovieEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull MovieEntity oldItem, @NonNull MovieEntity newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull MovieEntity oldItem, @NonNull MovieEntity newItem) {
                return oldItem.getId() == newItem.getId() && oldItem.getTitle().equals(newItem.getTitle());
            }
        });

        this.isNow = isNow;
    }

    @Override
    protected int itemLayoutRes() {
        return R.layout.item_movie;
    }

    @Override
    protected void onBind(@NonNull ViewDataBinding binding, MovieEntity item, int position) {
        super.onBind(binding, item, position);

        ItemMovieBinding binding_ = (ItemMovieBinding) binding;
        binding_.getRoot().setAlpha(position == 0 ? 1f : 0.5f);
        binding_.typeContainer.setTagData(item.getTypeList(), R.color.color_text_dark, R.color.color_blue);
        if (isNow) {
            binding_.ratingBar.setVisibility(View.VISIBLE);
            binding_.gradeTv.setVisibility(View.VISIBLE);
            binding_.releaseDateTv.setVisibility(View.GONE);
            if (item.getRating() <= 0) {
                binding_.ratingBar.setVisibility(View.GONE);
                binding_.gradeTv.setVisibility(View.GONE);
            } else {
                binding_.ratingBar.setVisibility(View.VISIBLE);
                binding_.gradeTv.setVisibility(View.VISIBLE);
            }
            binding_.castTv.setText(item.getActors());
        } else {
            binding_.ratingBar.setVisibility(View.GONE);
            binding_.gradeTv.setVisibility(View.GONE);
            binding_.releaseDateTv.setVisibility(View.VISIBLE);
            String txt = item.getYear() + "-";
            txt += item.getMonth() < 10 ? "0" + item.getMonth() : item.getMonth();
            txt += "-";
            txt += item.getDay() < 10 ? "0" + item.getDay() : item.getDay();
            binding_.releaseDateTv.setText(txt);
            binding_.releaseDateTv.append(" 上映");
            txt = item.getActor1() + "，" + item.getActor2();
            binding_.castTv.setText(txt);
        }
        binding.getRoot().setOnClickListener(v ->
                BaseActivity.navigate(getContext(), MovieDetailActivity.class, item.getId(),
                        new String[]{item.getImageTiny(), item.getImage(), item.getTitle()}));
    }
}
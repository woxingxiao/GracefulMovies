package com.xw.project.gracefulmovies.ui.adapter;

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
public class MoviesAdapter extends BaseBindingListAdapter<MovieEntity, ItemMovieBinding> {

    private boolean isNow;

    public MoviesAdapter(boolean isNow, List<MovieEntity> data) {
        super(data, new DiffUtil.ItemCallback<MovieEntity>() {
            @Override
            public boolean areItemsTheSame(MovieEntity oldItem, MovieEntity newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(MovieEntity oldItem, MovieEntity newItem) {
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
    protected void bind(ItemMovieBinding binding, MovieEntity item, int position) {
        super.bind(binding, item, position);

        binding.getRoot().setAlpha(position == 0 ? 1f : 0.5f);
        binding.typeContainer.setTagData(item.getTypeList(), R.color.color_text_dark, R.color.color_blue);
        if (isNow) {
            binding.ratingBar.setVisibility(View.VISIBLE);
            binding.gradeTv.setVisibility(View.VISIBLE);
            binding.releaseDateTv.setVisibility(View.GONE);
            if (item.getRating() <= 0) {
                binding.ratingBar.setVisibility(View.GONE);
                binding.gradeTv.setVisibility(View.GONE);
            } else {
                binding.ratingBar.setVisibility(View.VISIBLE);
                binding.gradeTv.setVisibility(View.VISIBLE);
                binding.ratingBar.setRating((float) (item.getRating() / 2));
            }
            binding.castTv.setText(item.getActors());
        } else {
            binding.ratingBar.setVisibility(View.GONE);
            binding.gradeTv.setVisibility(View.GONE);
            binding.releaseDateTv.setVisibility(View.VISIBLE);
            String txt = item.getYear() + "-";
            txt += item.getMonth() < 10 ? "0" + item.getMonth() : item.getMonth();
            txt += "-";
            txt += item.getDay() < 10 ? "0" + item.getDay() : item.getDay();
            binding.releaseDateTv.setText(txt);
            binding.releaseDateTv.append(" 上映");
            txt = item.getActor1() + "，" + item.getActor2();
            binding.castTv.setText(txt);
        }
        binding.posterIv.setOnClickListener(v ->
                BaseActivity.navigate(getContext(), MovieDetailActivity.class, item.getId(),
                        new String[]{item.getImageTiny(), item.getImage(), item.getTitle()}));
    }
}
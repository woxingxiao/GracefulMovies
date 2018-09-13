package com.xw.project.gracefulmovies.ui.adapter;

import android.support.v7.util.DiffUtil;
import android.view.View;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.db.entity.BoxOfficeEntity;
import com.xw.project.gracefulmovies.databinding.ItemBoxOfficeBinding;
import com.xw.project.gracefulmovies.ui.adapter.base.BaseBindingListAdapter;

/**
 * BoxOfficeAdapter
 * <p>
 * Created by woxignxiao on 2018-09-02.
 */
public class BoxOfficeAdapter extends BaseBindingListAdapter<BoxOfficeEntity, ItemBoxOfficeBinding> {

    public BoxOfficeAdapter() {
        super(new DiffUtil.ItemCallback<BoxOfficeEntity>() {
            @Override
            public boolean areItemsTheSame(BoxOfficeEntity oldItem, BoxOfficeEntity newItem) {
                return oldItem.getIrank().equals(newItem.getIrank());
            }

            @Override
            public boolean areContentsTheSame(BoxOfficeEntity oldItem, BoxOfficeEntity newItem) {
                return oldItem.getIrank().equals(newItem.getIrank()) &&
                        oldItem.getMovieName().equals(newItem.getMovieName());
            }
        });
    }

    @Override
    protected int itemLayoutRes() {
        return R.layout.item_box_office;
    }

    @Override
    protected void bind(ItemBoxOfficeBinding binding, BoxOfficeEntity item, int position) {
        super.bind(binding, item, position);

        if ("1".equals(item.getIrank())) {
            binding.boRankIv.setVisibility(View.VISIBLE);
            binding.boRankIv.setImageResource(R.drawable.svg_ic_gold_medal);
            binding.boRankTv.setText("");
        } else if ("2".equals(item.getIrank())) {
            binding.boRankIv.setVisibility(View.VISIBLE);
            binding.boRankIv.setImageResource(R.drawable.svg_ic_silver_medal);
            binding.boRankTv.setText("");
        } else if ("3".equals(item.getIrank())) {
            binding.boRankIv.setVisibility(View.VISIBLE);
            binding.boRankIv.setImageResource(R.drawable.svg_ic_bronze_medal);
            binding.boRankTv.setText("");
        } else {
            binding.boRankIv.setVisibility(View.INVISIBLE);
            binding.boRankTv.setText(item.getIrank());
        }
    }
}
package com.xw.project.gracefulmovies.ui.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
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
public class BoxOfficeAdapter extends BaseBindingListAdapter<BoxOfficeEntity> {

    public BoxOfficeAdapter() {
        super(new DiffUtil.ItemCallback<BoxOfficeEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull BoxOfficeEntity oldItem, @NonNull BoxOfficeEntity newItem) {
                return oldItem.getIrank().equals(newItem.getIrank());
            }

            @Override
            public boolean areContentsTheSame(@NonNull BoxOfficeEntity oldItem, @NonNull BoxOfficeEntity newItem) {
                return oldItem.toString().equals(newItem.toString());
            }
        });
    }

    @Override
    protected int itemLayoutRes() {
        return R.layout.item_box_office;
    }

    @Override
    protected void onBind(@NonNull ViewDataBinding binding, BoxOfficeEntity item, int position) {
        super.onBind(binding, item, position);

        ItemBoxOfficeBinding binding_ = (ItemBoxOfficeBinding) binding;
        if ("1".equals(item.getIrank())) {
            binding_.boRankIv.setVisibility(View.VISIBLE);
            binding_.boRankIv.setImageResource(R.drawable.svg_ic_gold_medal);
            binding_.boRankTv.setText("");
        } else if ("2".equals(item.getIrank())) {
            binding_.boRankIv.setVisibility(View.VISIBLE);
            binding_.boRankIv.setImageResource(R.drawable.svg_ic_silver_medal);
            binding_.boRankTv.setText("");
        } else if ("3".equals(item.getIrank())) {
            binding_.boRankIv.setVisibility(View.VISIBLE);
            binding_.boRankIv.setImageResource(R.drawable.svg_ic_bronze_medal);
            binding_.boRankTv.setText("");
        } else {
            binding_.boRankIv.setVisibility(View.INVISIBLE);
            binding_.boRankTv.setText(item.getIrank());
        }
    }
}
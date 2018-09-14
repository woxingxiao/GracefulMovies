package com.xw.project.gracefulmovies.ui.adapter;

import android.support.v7.util.DiffUtil;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.ao.MovieStills;
import com.xw.project.gracefulmovies.databinding.ItemStillsBinding;
import com.xw.project.gracefulmovies.ui.activity.MovieDetailActivity;
import com.xw.project.gracefulmovies.ui.adapter.base.BaseBindingListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * StillsAdapter
 * <p>
 * Created by woxignxiao on 2018-09-01.
 */
public class StillsAdapter extends BaseBindingListAdapter<MovieStills.Stills, ItemStillsBinding> {

    public StillsAdapter(List<MovieStills.Stills> data) {
        super(data, new DiffUtil.ItemCallback<MovieStills.Stills>() {
            @Override
            public boolean areItemsTheSame(MovieStills.Stills oldItem, MovieStills.Stills newItem) {
                return oldItem.imgUrl.equals(newItem.imgUrl);
            }

            @Override
            public boolean areContentsTheSame(MovieStills.Stills oldItem, MovieStills.Stills newItem) {
                return oldItem.imgUrl.equals(newItem.imgUrl);
            }
        });
    }

    @Override
    protected int itemLayoutRes() {
        return R.layout.item_stills;
    }

    @Override
    protected void bind(ItemStillsBinding binding, MovieStills.Stills item, int position) {
        super.bind(binding, item, position);

        binding.getRoot().setOnClickListener(v -> {
            ArrayList<String> urls = new ArrayList<>();
            for (MovieStills.Stills stills : getData()) {
                urls.add(stills.imgUrl);
            }

            ((MovieDetailActivity) getContext()).gotoPreviewImages(urls, position);
        });
    }
}
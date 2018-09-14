package com.xw.project.gracefulmovies.ui.adapter;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.ao.Actor;
import com.xw.project.gracefulmovies.databinding.ItemActorBinding;
import com.xw.project.gracefulmovies.ui.activity.MovieDetailActivity;
import com.xw.project.gracefulmovies.ui.adapter.base.BaseBindingListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ActorsAdapter
 * <p>
 * Created by woxignxiao on 2018-09-01.
 */
public class ActorsAdapter extends BaseBindingListAdapter<Actor, ItemActorBinding> {

    public ActorsAdapter(List<Actor> data) {
        super(data, new DiffUtil.ItemCallback<Actor>() {
            @Override
            public boolean areItemsTheSame(Actor oldItem, Actor newItem) {
                return oldItem.name.equals(newItem.name);
            }

            @Override
            public boolean areContentsTheSame(Actor oldItem, Actor newItem) {
                return oldItem.name.equals(newItem.name) && oldItem.img.equals(newItem.img);
            }
        });
    }

    @Override
    protected int itemLayoutRes() {
        return R.layout.item_actor;
    }

    @Override
    protected void bind(ItemActorBinding binding, Actor item, int position) {
        super.bind(binding, item, position);

        binding.getRoot().setOnClickListener(v -> {
            ArrayList<String> urls = new ArrayList<>();
            for (Actor actor : getData()) {
                urls.add(actor.img);
            }

            ((MovieDetailActivity) getContext()).gotoPreviewImages(urls, position);
        });
    }

    public static String displayRoleText(String role) {
        if ("导演".equals(role)) {
            return role;
        } else if (!TextUtils.isEmpty(role)) {
            return "饰 " + role;
        }
        return "";
    }
}
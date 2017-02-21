package com.xw.project.gracefulmovies.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * BaseRecyclerViewHolder
 * <p/>
 * Created by woxignxiao on 2017-01-25.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <V extends View> V findView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            if (view != null) {
                mViews.put(id, view);
            }
        }

        return (V) view;
    }
}
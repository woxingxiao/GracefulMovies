package com.xw.project.gracefulmovies.ui.adapter.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xw.project.gracefulmovies.BR;

import java.util.List;

/**
 * RecyclerView适配器基类
 * <p>
 * Created by woxignxiao on 2018-09-01.
 */
public abstract class BaseBindingListAdapter<T> extends ListAdapter<T, BaseBindingListAdapter.BaseBindingVH> {

    private Context mContext;
    private List<T> mData;

    public BaseBindingListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this(null, diffCallback);
    }

    public BaseBindingListAdapter(List<T> data, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);

        mData = data;
        submitList(data);
    }

    public BaseBindingListAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public BaseBindingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                itemLayoutRes(), parent, false);
        return new BaseBindingVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseBindingVH holder, int position) {
        onBind(holder, holder.getBinding(), getItem(position), position, holder.getItemViewType());
        holder.getBinding().executePendingBindings();
    }

    @LayoutRes
    protected abstract int itemLayoutRes();

    protected void onBind(@NonNull BaseBindingVH holder, @NonNull ViewDataBinding binding, T item, int position, int viewType) {
        onBind(holder, binding, item, position);
        onBind(binding, item, position);

        binding.setVariable(BR.model, item);
    }

    protected void onBind(@NonNull BaseBindingVH holder, @NonNull ViewDataBinding binding, T item, int position) {
    }

    protected void onBind(@NonNull ViewDataBinding binding, T item, int position) {
    }

    @Override
    public T getItem(int position) {
        return super.getItem(position);
    }

    public Context getContext() {
        return mContext;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        submitList(data);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class BaseBindingVH extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        BaseBindingVH(ViewDataBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}

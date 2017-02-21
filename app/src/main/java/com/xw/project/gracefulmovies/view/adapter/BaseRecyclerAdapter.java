package com.xw.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器基类
 * <p/>
 * Created by woxignxiao on 2017-01-25.
 */
public abstract class BaseRecyclerAdapter<T, VH extends BaseRecyclerViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int TYPE_NO_DATA = 0;
    public static final int TYPE_NORMAL = 1;

    protected Context mContext;
    private boolean hasNoData; // 是否没有数据
    protected List<T> mData;
    private boolean isShowAnim = true; // 是否播放item进入动画
    private int mLastPosition = -1;
    private boolean isLoading = true; // 是否显示loading placeHolder

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        return onCreate(LayoutInflater.from(mContext), parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        position = holder.getAdapterPosition();

        onBind(holder, position);

        Animator[] animators = getAnimators(holder.itemView);
        if (isShowAnim && animators != null && animators.length > 0
                && holder.getAdapterPosition() > mLastPosition) {
            if (animators.length > 1) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animators);
                animatorSet.start();
            } else {
                for (Animator animator : animators) {
                    animator.start();
                }
            }

            mLastPosition = holder.getAdapterPosition();
        }
    }

    protected Animator[] getAnimators(View view) {
        return null;
    }

    @Override
    public int getItemCount() {
        hasNoData = (mData == null || mData.isEmpty());
        return hasNoData ? 1 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return hasNoData ? TYPE_NO_DATA : TYPE_NORMAL;
    }

    /**
     * 获得除Header、hasNoDataView（如果有）外的item对应的data
     *
     * @param position item position
     * @return data
     */
    public T getItem(int position) {
        return hasNoData ? null : mData.get(position);
    }

    public void setData(List<T> data) {
        this.mData = data;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        this.hasNoData = mData.isEmpty();
        isLoading = false;

        notifyItemRangeChanged(0, hasNoData ? 1 : mData.size() - 1);
    }

    public void addData(List<T> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        int prePos = mData.size() - 1;
        if (data != null) {
            mData.addAll(data);
        }
        this.hasNoData = mData == null || mData.isEmpty();
        isLoading = false;

        notifyItemInserted(prePos + 1);
    }

    public void clearData() {
        if (mData != null) {
            mData.clear();
            hasNoData = true;
            isLoading = false;

            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    public boolean hasNoData() {
        return hasNoData;
    }

    public boolean checkNoDataItemByType(int type) {
        return type == TYPE_NO_DATA;
    }

    public boolean checkNoDataItemByPosition(int position) {
        return getItemViewType(position) == TYPE_NO_DATA;
    }

    public void setShowAnim(boolean showAnim) {
        isShowAnim = showAnim;
    }

    public boolean isShowAnim() {
        return isShowAnim;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (hasNoData)
            notifyItemChanged(0);
    }

    protected abstract VH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType);

    protected abstract void onBind(VH holder, int position);

}

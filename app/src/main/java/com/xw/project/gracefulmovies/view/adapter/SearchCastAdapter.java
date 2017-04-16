package com.xw.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieSearchModel;
import com.xw.project.gracefulmovies.util.Util;

import java.util.List;

/**
 * 搜索电影演员表适配器
 * <p/>
 * Created by woxingxiao on 2017-04-16.
 */
public class SearchCastAdapter extends BaseRecyclerAdapter<MovieSearchModel.CastInfo, SearchCastAdapter.CastVH> {

    private LinearLayout.LayoutParams mLayoutParams;

    public SearchCastAdapter(List<MovieSearchModel.CastInfo> list) {
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mLayoutParams = new LinearLayout.LayoutParams(screenWidth / 4, screenWidth / 4);

        setData(list);
    }

    @Override
    protected CastVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new CastVH(inflater.inflate(R.layout.item_search_cast, parent, false));
    }

    @Override
    protected void onBind(final CastVH holder, int position) {
        MovieSearchModel.CastInfo cast = mData.get(position);

        if (position == 0) {
            mLayoutParams.setMargins(0, Util.dp2px(2), Util.dp2px(2), Util.dp2px(2));
        } else {
            mLayoutParams.setMargins(Util.dp2px(2), Util.dp2px(2), Util.dp2px(2), Util.dp2px(2));
        }
        holder.castImage.setLayoutParams(mLayoutParams);
        Glide.with(mContext).load(cast.image).crossFade().error(R.drawable.svg_ic_loading).into(holder.castImage);
        holder.castText.setText(cast.name);
    }

    class CastVH extends BaseRecyclerViewHolder {

        AppCompatImageView castImage;
        TextView castText;

        CastVH(View itemView) {
            super(itemView);

            castImage = findView(R.id.cast_img);
            castText = findView(R.id.cast_text);
        }
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return null;
    }
}

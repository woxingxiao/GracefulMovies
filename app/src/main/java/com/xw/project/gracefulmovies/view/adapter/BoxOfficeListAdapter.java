package com.xw.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.BoxOfficeModel;

import org.polaric.colorful.Colorful;

/**
 * 票房列表适配器
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public class BoxOfficeListAdapter extends BaseRecyclerAdapter<BoxOfficeModel, BoxOfficeListAdapter.BoxOfficeVH> {

    @Override
    protected BoxOfficeVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_DATA) {
            return new BoxOfficeVH(inflater.inflate(R.layout.layout_place_holder, parent, false));
        }
        return new BoxOfficeVH(inflater.inflate(R.layout.item_box_office, parent, false));
    }

    @Override
    protected void onBind(final BoxOfficeVH holder, int position) {
        if (checkNoDataItemByPosition(position)) {
            ((TextView) holder.itemView.findViewById(R.id.place_holder_hint_text)).setText(isLoading()
                    ? mContext.getString(R.string.data_loading) : mContext.getString(R.string.has_no_data));
            return;
        }

        BoxOfficeModel model = mData.get(position);

        if (model.getRankInt() == 1) {
            holder.rankImg.setVisibility(View.VISIBLE);
            holder.rankImg.setImageResource(R.drawable.svg_ic_gold_metal);
            holder.rankText.setText("");
        } else if (model.getRankInt() == 2) {
            holder.rankImg.setVisibility(View.VISIBLE);
            holder.rankImg.setImageResource(R.drawable.svg_ic_silver_metal);
            holder.rankText.setText("");
        } else if (model.getRankInt() == 3) {
            holder.rankImg.setVisibility(View.VISIBLE);
            holder.rankImg.setImageResource(R.drawable.svg_ic_bronze_metal);
            holder.rankText.setText("");
        } else {
            holder.rankImg.setVisibility(View.INVISIBLE);
            holder.rankText.setText(model.getRank());
        }
        holder.nameText.setText(model.getName());

        holder.text1.setTextColor(ContextCompat.getColor(mContext,
                Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        holder.text2.setTextColor(ContextCompat.getColor(mContext,
                Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        holder.text1.setText(model.getBoxOffice());
        holder.text2.setText(model.getSumBoxOffice());
        holder.text3.setText(model.getMovieDays());
        holder.text4.setText(model.getBoxOfficeRate());
        holder.text5.setText(model.getAvgPrice());
    }

    class BoxOfficeVH extends BaseRecyclerViewHolder {

        AppCompatImageView rankImg;
        TextView rankText;
        TextView nameText;
        TextView text1, text2, text3, text4, text5;

        BoxOfficeVH(View itemView) {
            super(itemView);

            rankImg = findView(R.id.bo_rank_img);
            rankText = findView(R.id.bo_rank_text);
            nameText = findView(R.id.bo_name_text);
            text1 = findView(R.id.bo_text_1);
            text2 = findView(R.id.bo_text_2);
            text3 = findView(R.id.bo_text_3);
            text4 = findView(R.id.bo_text_4);
            text5 = findView(R.id.bo_text_5);
        }
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1f).setDuration(400),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100, 0).setDuration(400)
        };
    }
}

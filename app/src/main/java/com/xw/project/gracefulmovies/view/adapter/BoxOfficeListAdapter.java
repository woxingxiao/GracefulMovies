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
import com.xw.project.gracefulmovies.view.activity.BoxOfficeActivity;

import org.polaric.colorful.Colorful;

/**
 * 票房列表适配器
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public class BoxOfficeListAdapter extends BaseRecyclerAdapter<BoxOfficeModel, BoxOfficeListAdapter.BoxOfficeVH> {

    private int mDataType;

    public BoxOfficeListAdapter(int type) {
        mDataType = type;
    }

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
        if (mDataType < 3) {
            holder.text1.setText(model.getBoxOffice());
            if (mDataType == BoxOfficeActivity.DAY) {
                holder.hint1.setText(mContext.getString(R.string.hint_box_office_day));
            } else if (mDataType == BoxOfficeActivity.WEEKEND) {
                holder.hint1.setText(mContext.getString(R.string.hint_box_office_weekend));
            } else if (mDataType == BoxOfficeActivity.WEEK) {
                holder.hint1.setText(mContext.getString(R.string.hint_box_office_week));
            }
            holder.text2.setText(model.getSumBoxOffice());
            holder.hint2.setText(mContext.getString(R.string.hint_box_office_sum));
            holder.text3.setText(model.getMovieDays());
            holder.hint3.setText(mContext.getString(R.string.hint_released_days));
            holder.text4.setText(model.getBoxOfficeRate());
            holder.hint4.setText(mContext.getString(R.string.hint_box_office_rate));
            holder.text5.setText(model.getAvgPrice());
            holder.hint5.setText(mContext.getString(R.string.hint_average_price));
            if (mDataType != BoxOfficeActivity.DAY) {
                holder.text6.setText(model.getWomIndex());
                holder.hint6.setText(mContext.getString(R.string.hint_wom_index));
            }
        } else {
            holder.text1.setText(model.getBoxOffice());
            holder.hint1.setText(mContext.getString(R.string.hint_box_office_month));
            holder.text2.setTextSize(18);
            holder.text2.setText(model.getReleaseDate());
            holder.hint2.setText(mContext.getString(R.string.release_date));
            holder.text3.setText(model.getMovieDays());
            holder.hint3.setText(mContext.getString(R.string.hint_playing_days_in_the_month));
            holder.text4.setText(model.getBoxOfficeRate());
            holder.hint4.setText(mContext.getString(R.string.hint_playing_rate_in_the_month));
            holder.text5.setText(model.getAvgPrice());
            holder.hint5.setText(mContext.getString(R.string.hint_average_price));
        }
    }

    class BoxOfficeVH extends BaseRecyclerViewHolder {

        AppCompatImageView rankImg;
        TextView rankText;
        TextView nameText;
        TextView text1, text2, text3, text4, text5, text6;
        TextView hint1, hint2, hint3, hint4, hint5, hint6;

        BoxOfficeVH(View itemView) {
            super(itemView);

            rankImg = findView(R.id.bo_rank_img);
            rankText = findView(R.id.bo_rank_text);
            nameText = findView(R.id.bo_name_text);
            text1 = findView(R.id.bo_text_1);
            hint1 = findView(R.id.bo_hint_1);
            text2 = findView(R.id.bo_text_2);
            hint2 = findView(R.id.bo_hint_2);
            text3 = findView(R.id.bo_text_3);
            hint3 = findView(R.id.bo_hint_3);
            text4 = findView(R.id.bo_text_4);
            hint4 = findView(R.id.bo_hint_4);
            text5 = findView(R.id.bo_text_5);
            hint5 = findView(R.id.bo_hint_5);
            text6 = findView(R.id.bo_text_6);
            hint6 = findView(R.id.bo_hint_6);
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

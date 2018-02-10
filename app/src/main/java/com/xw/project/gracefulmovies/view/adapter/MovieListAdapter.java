package com.xw.project.gracefulmovies.view.adapter;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.view.activity.BaseActivity;
import com.xw.project.gracefulmovies.view.activity.MovieDetailActivity;
import com.xw.project.gracefulmovies.view.widget.AutoFitSizeTextView;
import com.xw.project.gracefulmovies.view.widget.TagGroup;

import org.polaric.colorful.Colorful;

import java.util.List;

/**
 * 电影列表适配器
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */
public class MovieListAdapter extends BaseRecyclerAdapter<MovieModel, MovieListAdapter.MovieVH> {

    public MovieListAdapter(List<MovieModel> movieModels) {
        setData(movieModels);
    }

    @Override
    protected MovieVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new MovieVH(inflater.inflate(R.layout.item_movie, parent, false));
    }

    @Override
    protected void onBind(final MovieVH holder, int position) {
        if (mData == null)
            return;
        final MovieModel model = mData.get(position);
        if (model == null)
            return;

        holder.mNameText.setText(model.getName());
        holder.mNameText.setAlpha(position == 0 ? 1 : 0);
        Glide.with(mContext)
                .load(model.getPoster())
                .placeholder(R.drawable.pic_place_holder)
                .error(R.drawable.pic_place_holder)
                .into(holder.mPosterImg);
        holder.tagContainer.setTagData(model.getMovieTypeList(),
                Colorful.getThemeDelegate().getAccentColor().getColorRes());
        if (model.getGrade() != null) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.gradeText.setVisibility(View.VISIBLE);
            holder.releaseDateText.setVisibility(View.GONE);
            holder.ratingBar.setRating((Float.valueOf(model.getGrade())) / 2.0f);
            holder.ratingBar.setFillColor(ContextCompat.getColor(
                    mContext, Colorful.getThemeDelegate().getAccentColor().getColorRes()));
            holder.gradeText.setText(model.getGrade());
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.gradeText.setVisibility(View.INVISIBLE);
            holder.releaseDateText.setVisibility(View.VISIBLE);
            holder.releaseDateText.setText(model.getReleaseDateString());
            holder.releaseDateText.append(" 上映");
        }
        holder.castText.setText(model.getCastString());

        holder.mOpenInfoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mOpenInfoImg.setVisibility(View.GONE);
                holder.mInfoLayout.setVisibility(View.VISIBLE);
            }
        });
        holder.mPosterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.navigate(mContext, MovieDetailActivity.class, model);
            }
        });
    }

    public static class MovieVH extends BaseRecyclerViewHolder {

        public AutoFitSizeTextView mNameText;
        public ImageView mOpenInfoImg;
        public View mInfoLayout;
        ImageView mPosterImg;
        TagGroup tagContainer;
        SimpleRatingBar ratingBar;
        TextView gradeText;
        TextView releaseDateText;
        TextView castText;

        MovieVH(View itemView) {
            super(itemView);

            mPosterImg = findView(R.id.movie_poster_img);
            mNameText = findView(R.id.movie_name_text);
            mOpenInfoImg = findView(R.id.movie_open_info_img);
            mInfoLayout = findView(R.id.movie_info_layout);
            tagContainer = findView(R.id.movie_type_container);
            ratingBar = findView(R.id.movie_rating_bar);
            gradeText = findView(R.id.movie_grade_text);
            releaseDateText = findView(R.id.movie_release_date_text);
            castText = findView(R.id.movie_cast_text);
        }
    }
}

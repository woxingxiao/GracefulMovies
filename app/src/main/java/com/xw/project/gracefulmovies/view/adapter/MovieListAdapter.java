package com.xw.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.view.activity.MovieDetailActivity;
import com.xw.project.gracefulmovies.view.widget.TagGroup;

import org.polaric.colorful.Colorful;

/**
 * 电影列表适配器
 * <p/>
 * Created by woxingxiao on 2017-02-12.
 */
public class MovieListAdapter extends BaseRecyclerAdapter<MovieModel, MovieListAdapter.MovieVH> {

    @Override
    protected MovieVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_DATA) {
            return new MovieVH(inflater.inflate(R.layout.layout_place_holder, parent, false));
        }
        return new MovieVH(inflater.inflate(R.layout.item_movie, parent, false));
    }

    @Override
    protected void onBind(final MovieVH holder, int position) {
        if (checkNoDataItemByPosition(position)) {
            ((TextView) holder.itemView.findViewById(R.id.place_holder_hint_text)).setText(isLoading()
                    ? mContext.getString(R.string.data_loading) : mContext.getString(R.string.has_no_data));
            return;
        }

        final MovieModel movieModel = mData.get(position);

        holder.posterImg.setAlpha(Colorful.getThemeDelegate().isNight() ? 0.7f : 1.0f);
        Glide.with(mContext).load(movieModel.getPoster()).crossFade().into(holder.posterImg);
        holder.nameText.setText(movieModel.getName());
        holder.tagContainer.setTagData(movieModel.getMovieTypeList(),
                Colorful.getThemeDelegate().getAccentColor().getColorRes());
        if (movieModel.getGrade() != null) {
            holder.gradeLayout.setVisibility(View.VISIBLE);
            holder.releaseDateText.setVisibility(View.GONE);
            holder.ratingBar.setRating((Float.valueOf(movieModel.getGrade())) / 2.0f);
            holder.ratingBar.setFillColor(ContextCompat.getColor(
                    mContext, Colorful.getThemeDelegate().getAccentColor().getColorRes()));

            holder.gradeText.setText(movieModel.getGrade());
        } else {
            holder.gradeLayout.setVisibility(View.GONE);
            holder.releaseDateText.setVisibility(View.VISIBLE);
            holder.releaseDateText.setText(movieModel.getReleaseDateString());
            holder.releaseDateText.append(" 上映");
        }
        holder.castText.setText(movieModel.getCastString());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat option = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0,
                        view.getMeasuredWidth(), view.getMeasuredHeight());
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("movie_model", movieModel);
                ActivityCompat.startActivity(mContext, intent, option.toBundle());
            }
        });
    }

    class MovieVH extends BaseRecyclerViewHolder {

        LinearLayout container;
        AppCompatImageView posterImg;
        TextView nameText;
        TagGroup tagContainer;
        LinearLayout gradeLayout;
        SimpleRatingBar ratingBar;
        TextView gradeText;
        TextView releaseDateText;
        TextView castText;

        MovieVH(View itemView) {
            super(itemView);

            container = findView(R.id.movie_container);
            posterImg = findView(R.id.movie_poster_img);
            nameText = findView(R.id.movie_name_text);
            tagContainer = findView(R.id.movie_type_container);
            gradeLayout = findView(R.id.movie_grade_layout);
            ratingBar = findView(R.id.movie_rating_bar);
            gradeText = findView(R.id.movie_grade_text);
            releaseDateText = findView(R.id.movie_release_date_text);
            castText = findView(R.id.movie_cast_text);
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

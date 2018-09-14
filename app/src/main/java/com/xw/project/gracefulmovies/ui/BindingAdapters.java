package com.xw.project.gracefulmovies.ui;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xw.project.gracefulmovies.R;

/**
 * <p>
 * Created by woxingxiao on 2018-08-31.
 */
public final class BindingAdapters {

    @BindingAdapter("imageUrl")
    public static void loadImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.pic_place_holder)
                .error(R.drawable.pic_error)
                .into(imageView);
    }

    @BindingAdapter("srb_rating")
    public static void setRating(SimpleRatingBar ratingBar, float rating) {
        ratingBar.setVisibility(rating > 0 ? View.VISIBLE : View.GONE);
        ratingBar.setRating(rating / 2);
    }

    @BindingAdapter("srb_rating")
    public static void setRating(SimpleRatingBar ratingBar, double rating) {
        ratingBar.setVisibility(rating > 0 ? View.VISIBLE : View.GONE);
        ratingBar.setRating((float) (rating / 2));
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }
}

package com.xw.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.OpenModel;
import com.xw.project.gracefulmovies.view.widget.QuoteTextView;

import org.polaric.colorful.Colorful;

import java.util.List;

/**
 * 开源许可列表适配器
 * <p/>
 * Created by woxingxiao on 2017-02-19.
 */
public class LicenseListAdapter extends BaseRecyclerAdapter<OpenModel, LicenseListAdapter.LicenseVH> {

    public LicenseListAdapter(List<OpenModel> list) {
        setData(list);
    }

    @Override
    protected LicenseVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new LicenseVH(inflater.inflate(R.layout.item_license, parent, false));
    }

    @Override
    protected void onBind(final LicenseVH holder, int position) {
        OpenModel openModel = mData.get(position);

        holder.sourceText.setText(openModel.getName());
        holder.licenseText.setText(openModel.getLicense());
        holder.licenseText.setNight(Colorful.getThemeDelegate().isNight());
    }

    class LicenseVH extends BaseRecyclerViewHolder {

        TextView sourceText;
        QuoteTextView licenseText;

        LicenseVH(View itemView) {
            super(itemView);

            sourceText = findView(R.id.open_source_text);
            licenseText = findView(R.id.open_license_text);
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

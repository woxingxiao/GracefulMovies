package com.xw.project.gracefulmovies.ui.adapter;

import android.support.v7.util.DiffUtil;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.ao.OpenProject;
import com.xw.project.gracefulmovies.databinding.ItemLicenseBinding;
import com.xw.project.gracefulmovies.ui.adapter.base.BaseBindingListAdapter;

import java.util.List;

/**
 * 开源许可列表适配器
 * <p>
 * Created by woxignxiao on 2018-09-02.
 */
public class LicensesAdapter extends BaseBindingListAdapter<OpenProject, ItemLicenseBinding> {

    public LicensesAdapter(List<OpenProject> data) {
        super(data, new DiffUtil.ItemCallback<OpenProject>() {
            @Override
            public boolean areItemsTheSame(OpenProject oldItem, OpenProject newItem) {
                return oldItem.name.equals(newItem.name);
            }

            @Override
            public boolean areContentsTheSame(OpenProject oldItem, OpenProject newItem) {
                return oldItem.name.equals(newItem.name);
            }
        });
    }

    @Override
    protected int itemLayoutRes() {
        return R.layout.item_license;
    }
}
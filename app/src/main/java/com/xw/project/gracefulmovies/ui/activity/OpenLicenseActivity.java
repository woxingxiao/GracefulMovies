package com.xw.project.gracefulmovies.ui.activity;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.ao.OpenProject;
import com.xw.project.gracefulmovies.databinding.ActivityOpenLicenseBinding;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;
import com.xw.project.gracefulmovies.ui.adapter.LicensesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 开源许可
 * <p>
 * Created by woxingxiao on 2018-08-18.
 */
public class OpenLicenseActivity extends BaseActivity<ActivityOpenLicenseBinding> {

    @Override
    protected int contentLayoutRes() {
        return  R.layout.activity_open_license;
    }

    @Override
    protected void afterSetContentView() {
        initializeToolbar();

        String[] libs = getResources().getStringArray(R.array.libraries);
        String[] licenses = getResources().getStringArray(R.array.licenses);
        List<OpenProject> list = new ArrayList<>();
        OpenProject project;
        for (int i = 0; i < libs.length; i++) {
            project = new OpenProject();
            project.name = libs[i];
            project.license = licenses[i];
            list.add(project);
        }
        mBinding.licenseRecyclerView.setAdapter(new LicensesAdapter(list));
    }
}

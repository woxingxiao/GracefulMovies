package com.xw.project.gracefulmovies.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.databinding.ActivityBoxOfficeBinding;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;
import com.xw.project.gracefulmovies.ui.adapter.BoxOfficeAdapter;
import com.xw.project.gracefulmovies.viewmodel.BoxOfficeViewModel;

/**
 * 票房
 * <p>
 * Created by xoxingxiao on 2018-08-17.
 */
public class BoxOfficeActivity extends BaseActivity<ActivityBoxOfficeBinding> implements SwipeRefreshLayout.OnRefreshListener {

    private BoxOfficeAdapter mAdapter;
    private BoxOfficeViewModel mViewModel;

    @Override
    protected int contentLayoutRes() {
        return R.layout.activity_box_office;
    }

    @Override
    protected void afterSetContentView() {
        initializeToolbar();

        mBinding.fab.setOnClickListener(view -> {
            if (mAdapter.getData() != null && !mAdapter.getData().isEmpty())
                mBinding.recyclerView.smoothScrollToPosition(0);
        });

        mBinding.swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary)
        );
        mBinding.swipeRefreshLayout.setProgressViewEndTarget(false, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, Resources.getSystem().getDisplayMetrics()));
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new BoxOfficeAdapter();
        mBinding.recyclerView.setAdapter(mAdapter);

        GMApplication.getInstance().getDatabase().boxOfficeDao().loadBoxOfficeList()
                .observe(this, list -> mAdapter.setData(list));

        mViewModel = ViewModelProviders.of(this).get(BoxOfficeViewModel.class);
        mViewModel.getBoxOffices().observe(this, resource -> {
            assert resource != null;
            processStatusView(resource);

            if (resource.getStatus() != DataResource.Status.LOADING) {
                mBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        mBinding.swipeRefreshLayout.setRefreshing(true);
        mViewModel.load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.box_office, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_attention) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setView(R.layout.layout_box_office_attention_dialog);
            builder.setPositiveButton("好的", null);
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onReload() {
        mViewModel.load();
    }

    @Override
    public void onRefresh() {
        mViewModel.load();
    }
}

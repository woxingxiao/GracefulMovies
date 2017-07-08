package com.xw.project.gracefulmovies.view.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.BoxOfficeModel;
import com.xw.project.gracefulmovies.presenter.IBoxOfficeActivityPresenter;
import com.xw.project.gracefulmovies.presenter.impl.BoxOfficeActivityPresenterImpl;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.view.adapter.BoxOfficeListAdapter;
import com.xw.project.gracefulmovies.view.iview.IBoxOfficeActivity;

import org.polaric.colorful.Colorful;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 票房
 * <p/>
 * Created by xoxingxiao on 2017-03-07.
 */
public class BoxOfficeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        IBoxOfficeActivity {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private BoxOfficeListAdapter mAdapter;
    private IBoxOfficeActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_office);
        ButterKnife.bind(this);

        initializeToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getData() != null && !mAdapter.getData().isEmpty())
                    mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, Colorful.getThemeDelegate().getAccentColor().getColorRes()),
                ContextCompat.getColor(this, Colorful.getThemeDelegate().getPrimaryColor().getColorRes())
        );
        mSwipeRefreshLayout.setProgressViewEndTarget(false, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, Resources.getSystem().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BoxOfficeListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);

        mPresenter = new BoxOfficeActivityPresenterImpl();
        mPresenter.register(this);
        mPresenter.loadData();
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
    public void onRefresh() {
        mPresenter.loadData();
        mAdapter.setLoading(true);
    }

    @Override
    public void onDataReady(List<BoxOfficeModel> modelList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setData(modelList);
    }

    @Override
    public void onDataError(int code, String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clearData();
        mAdapter.setLoading(false);
        showToast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.unregister();
        mPresenter = null;
        ApiHelper.releaseBoxOfficeApi();
    }
}

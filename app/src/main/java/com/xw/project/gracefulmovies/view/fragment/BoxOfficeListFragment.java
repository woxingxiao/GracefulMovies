package com.xw.project.gracefulmovies.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.BoxOfficeModel;
import com.xw.project.gracefulmovies.presenter.IBoxOfficeFragmentPresenter;
import com.xw.project.gracefulmovies.presenter.impl.BoxOfficeFragmentPresenterImpl;
import com.xw.project.gracefulmovies.view.adapter.BoxOfficeListAdapter;
import com.xw.project.gracefulmovies.view.iview.IBoxOfficeListFragment;

import org.polaric.colorful.Colorful;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 影片列表Fragment
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public class BoxOfficeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        IBoxOfficeListFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BoxOfficeListAdapter mAdapter;
    private IBoxOfficeFragmentPresenter mPresenter;
    private int mDataType;

    public static BoxOfficeListFragment newInstance(int type) {
        BoxOfficeListFragment fragment = new BoxOfficeListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new BoxOfficeFragmentPresenterImpl();
        mPresenter.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), Colorful.getThemeDelegate().getAccentColor().getColorRes()),
                ContextCompat.getColor(getContext(), Colorful.getThemeDelegate().getPrimaryColor().getColorRes())
        );
        mSwipeRefreshLayout.setProgressViewEndTarget(false, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, Resources.getSystem().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataType = getArguments().getInt("type");
        mAdapter = new BoxOfficeListAdapter(mDataType);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadData(mDataType);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(mDataType);
        mAdapter.setLoading(true);
    }

    @Override
    public void onDataReady(List<BoxOfficeModel> movieModels) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setData(movieModels);
    }

    @Override
    public void onDataError(int code, String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clearData();
        mAdapter.setLoading(false);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.unregister();
        mPresenter = null;
    }

}

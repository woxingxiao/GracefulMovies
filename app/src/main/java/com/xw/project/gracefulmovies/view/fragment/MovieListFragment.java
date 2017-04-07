package com.xw.project.gracefulmovies.view.fragment;

import android.content.Context;
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

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieModel;
import com.xw.project.gracefulmovies.view.activity.MainActivity;
import com.xw.project.gracefulmovies.view.adapter.MovieListAdapter;

import org.polaric.colorful.Colorful;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 影片列表Fragment
 * <p/>
 * Created by woxingxiao on 2017-01-23.
 */
public class MovieListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private MovieListAdapter mAdapter;
    private int mId;

    public static MovieListFragment newInstance(int id) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mSwipeRefreshLayout.setRefreshing(savedInstanceState == null);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MovieListAdapter();
        mAdapter.setShowAnim(savedInstanceState == null);
        mAdapter.setLoading(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mId = getArguments().getInt("id");
        /**
         * Fragment初始化完成告知MainActivityPresenter，并索要数据，如果：
         * 1. 数据已准备好，直接回调装载数据；
         * 2. 数据还在加载，等待加载完毕再回调装载数据。
         */
        mActivity.getPresenter().onFragmentInitOK(mId);
        mActivity.getPresenter().onFragmentRefreshRequest(mId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onRefresh() {
        if (mAdapter.getData() != null) {
            // 假刷新
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 500);
        } else {
            mActivity.getPresenter().onFragmentRefreshRequest(mId);
            mAdapter.setLoading(true);
        }
    }

    public void onDataReady(List<MovieModel> movieModels) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setData(movieModels);
    }

    public void onDataError() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clearData();
        mAdapter.setLoading(false);
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

}

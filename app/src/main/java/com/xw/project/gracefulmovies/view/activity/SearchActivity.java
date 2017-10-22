package com.xw.project.gracefulmovies.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieSearchModel;
import com.xw.project.gracefulmovies.presenter.ISearchActivityPresenter;
import com.xw.project.gracefulmovies.presenter.impl.SearchActivityPresenterImpl;
import com.xw.project.gracefulmovies.view.adapter.SearchCastAdapter;
import com.xw.project.gracefulmovies.view.iview.ISearchActivity;
import com.xw.project.gracefulmovies.view.widget.TagGroup;
import com.xw.repo.VectorCompatTextView;

import org.polaric.colorful.Colorful;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索
 * <p></>
 * Created by woxingxiao on 2017-04-15.
 */
public class SearchActivity extends BaseActivity implements ISearchActivity {

    @BindView(R.id.search_base_info_layout)
    View mBaseInfoLayout;
    @BindView(R.id.search_root_layout)
    LinearLayout mRootLayout;
    @BindView(R.id.place_holder_hint_text)
    VectorCompatTextView mPlaceHolderHintText;
    @BindView(R.id.place_holder_container)
    View mPlaceHolderContainer;
    @BindView(R.id.search_poster_img)
    AppCompatImageView mPosterImg;
    @BindView(R.id.search_name_text)
    TextView mNameText;
    @BindView(R.id.search_type_container)
    TagGroup mTypeContainer;
    @BindView(R.id.search_rating_bar)
    SimpleRatingBar mRatingBar;
    @BindView(R.id.search_grade_text)
    TextView mGradeText;
    @BindView(R.id.search_director_text)
    TextView mDirectorText;
    @BindView(R.id.search_scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.search_story_brief_text)
    TextView mStoryBriefText;
    @BindView(R.id.search_cast_recycler_view)
    RecyclerView mCastRecyclerView;

    private SearchView mSearchView;

    private ArrayAdapter<String> mAdapter;
    private ISearchActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new SearchActivityPresenterImpl(this);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initializeToolbar();
        mPlaceHolderHintText.setText(getString(R.string.hint_movie_search));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mPlaceHolderHintText.getVisibility() == View.VISIBLE) {
                    mPlaceHolderHintText.setText(getString(R.string.data_loading));
                }
                hideInputMethod();

                mPresenter.doSearch(query);
                mPresenter.dealWithSearchHistory(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        int id = mSearchView.getResources().getIdentifier("search_src_text", "id", getPackageName());
        AutoCompleteTextView autoCompleteText = (AutoCompleteTextView) mSearchView.findViewById(id);
        autoCompleteText.setThreshold(0);
        if (Colorful.getThemeDelegate().isNight()) {
            mAdapter = new ArrayAdapter<>(this, R.layout.layout_search_hint_night, new ArrayList<String>());
        } else {
            mAdapter = new ArrayAdapter<>(this, R.layout.layout_search_hint_day, new ArrayList<String>());
        }
        autoCompleteText.setAdapter(mAdapter);
        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchView.setQuery(mAdapter.getItem(position), true);
            }
        });

        mPresenter.readHistoryFormFile();

        return true;
    }

    @Override
    protected void onSlideBackIntention() {
        super.onSlideBackIntention();

        clearFocusOfSearchView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            clearFocusOfSearchView();

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 清除焦点，隐藏输入法软键盘，以防止右滑返回时（由于SlideBack的机制）首页FAB会闪一下
     */
    private void clearFocusOfSearchView() {
        mSearchView.clearFocus();
    }

    @OnClick({R.id.search_root_layout, R.id.search_base_info_layout, R.id.search_more_info_layout})
    public void onClick() {
        hideInputMethod();
    }

    @Override
    public void onUpdateSearchViewAdapter(ArrayList<String> histories) {
        mAdapter.clear();
        mAdapter.addAll(histories);
    }

    @Override
    public void onDisplayUI(MovieSearchModel model) {
        mPlaceHolderContainer.setVisibility(View.GONE);
        mBaseInfoLayout.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        if (Colorful.getThemeDelegate().isNight()) {
            mScrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_night));
        } else {
            mScrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_day));
        }

        mNameText.setText(model.getTitle());
        Glide.with(this).load(model.getCover()).error(R.drawable.svg_ic_loading).into(mPosterImg);
        String tags = model.getTag();
        if (!TextUtils.isEmpty(tags) && tags.contains("/")) {
            mTypeContainer.setTagData(tags.replaceAll(" ", "").split("/"),
                    Colorful.getThemeDelegate().getAccentColor().getColorRes());

            int childCount = mTypeContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                TextView textView = (TextView) mTypeContainer.getChildAt(i);
                textView.setTextColor(ContextCompat.getColor(this,
                        Colorful.getThemeDelegate().getAccentColor().getColorRes()));
            }
        }
        mDirectorText.setText(model.getDir());
        float rating = model.getRating();
        if (rating > 0) {
            mRatingBar.setRating(rating);
            mRatingBar.setFillColor(ContextCompat.getColor(
                    this, Colorful.getThemeDelegate().getAccentColor().getColorRes()));
            mGradeText.setText(String.valueOf(rating));
        } else {
            mRatingBar.setVisibility(View.INVISIBLE);
            mGradeText.setVisibility(View.INVISIBLE);
        }
        mStoryBriefText.setText(model.getDesc());
        mCastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if (model.getCasts() != null && !model.getCasts().isEmpty()) {
            mCastRecyclerView.setAdapter(new SearchCastAdapter(model.getCasts()));
        }
    }

    @Override
    public void onDataFailed(String msg) {
        mPlaceHolderContainer.setVisibility(View.VISIBLE);
        mBaseInfoLayout.setVisibility(View.GONE);
        mScrollView.setVisibility(View.GONE);
        mPlaceHolderHintText.setText(getString(R.string.has_no_data));

        showToast(msg);
    }

    private void hideInputMethod() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.writeHistoryToFile();
        mPresenter.unregister();
    }

}

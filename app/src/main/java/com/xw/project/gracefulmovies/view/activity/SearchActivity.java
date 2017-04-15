package com.xw.project.gracefulmovies.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.model.MovieSearchModel;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.view.widget.TagGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.polaric.colorful.Colorful;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {

    private static final int MAX_SIZE = 10;
    private static String CACHE_HISTORY_FILE;

    @BindView(R.id.search_result_layout)
    View mResultLayout;
    @BindView(R.id.search_root_layout)
    LinearLayout mRootLayout;
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
    @BindView(R.id.search_story_brief_text)
    TextView mStoryBriefText;

    private SearchView mSearchView;

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CACHE_HISTORY_FILE = getCacheDir().getAbsolutePath() + "/search_history.json";

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initializeToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bridge, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideInputMethod();

                doSearchTask(query);

                Observable.just(query)
                        .map(new Func1<String, Void>() {
                            @Override
                            public Void call(String s) {
                                mHistoryList.add(0, s.trim());

                                // 仅保留MAX_SIZE个数据
                                int size = mHistoryList.size() > MAX_SIZE ? MAX_SIZE : mHistoryList.size();
                                Iterator<String> iterator = mHistoryList.iterator();
                                int index = 0;
                                while (iterator.hasNext() && !iterator.next().isEmpty()) {
                                    if (index > size - 1) {
                                        iterator.remove();
                                    }
                                    index++;
                                }

                                // 去除重复数据
                                ArrayList<String> list = new ArrayList<>();
                                size = mHistoryList.size();
                                String str;
                                boolean isDuplicate;
                                for (int i = 0; i < size; i++) {
                                    isDuplicate = false;
                                    str = mHistoryList.get(i);
                                    for (int j = i + 1; j < size; j++) {
                                        if (str.equals(mHistoryList.get(j))) {
                                            isDuplicate = true;
                                            break;
                                        }
                                    }
                                    if (!isDuplicate) {
                                        list.add(str);
                                    }
                                }

                                mHistoryList = list;

                                return null;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                mAdapter.clear();
                                mAdapter.addAll(mHistoryList);
                            }
                        });

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
                mSearchView.setQuery(mHistoryList.get(position), true);
            }
        });

        Observable.just("read history")
                .map(new Func1<String, Void>() {
                    @Override
                    public Void call(String s) {
                        readHistoryFormFile();

                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void s) {
                        mAdapter.clear();
                        mAdapter.addAll(mHistoryList);
                    }
                });

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

    @OnClick(R.id.search_root_layout)
    public void onClick() {
        hideInputMethod();
    }

    private void hideInputMethod() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
    }

    private void doSearchTask(String name) {
        ApiHelper.searchMovie(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MovieSearchModel>() {
                    @Override
                    public void call(MovieSearchModel movieSearchModel) {
                        displayUI(movieSearchModel);
                    }
                });
    }

    private void displayUI(MovieSearchModel model) {
        mResultLayout.setVisibility(View.VISIBLE);

        mNameText.setText(model.getTitle());
        Glide.with(this).load(model.getCover()).into(mPosterImg);
        String tags = model.getTag();
        if (!TextUtils.isEmpty(tags) && tags.contains("/")) {
            mTypeContainer.setTagData(tags.trim().split("/"),
                    Colorful.getThemeDelegate().getAccentColor().getColorRes());
        }
        mDirectorText.setText(model.getDir());
        float rating = model.getRating();
        if (rating > 0) {
            mRatingBar.setRating(rating);
            mRatingBar.setFillColor(ContextCompat.getColor(
                    this, Colorful.getThemeDelegate().getAccentColor().getColorRes()));
            mGradeText.setText(String.valueOf(rating));
        } else {
            mRatingBar.setVisibility(View.GONE);
            mGradeText.setVisibility(View.GONE);
        }
        mStoryBriefText.setText(model.getDesc());
    }

    /**
     * 从本地json文件获取搜索历史
     */
    private void readHistoryFormFile() {
        mHistoryList.clear();

        File file = new File(CACHE_HISTORY_FILE);
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));

                StringBuilder sb = new StringBuilder();
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mHistoryList.add(jsonArray.optString(i));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索历史保存到本地json文件
     */
    private void writeHistoryToFile() {
        File file = new File(CACHE_HISTORY_FILE);
        boolean ok = true;
        if (!file.exists()) {
            try {
                ok = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!ok) return;

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));

            StringBuilder sb = new StringBuilder();
            int size = mHistoryList.size();
            sb.append("[");
            for (int i = 0; i < size; i++) {
                sb.append("\"").append(mHistoryList.get(i)).append("\"");
                if (i < size - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");

            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Observable.just("write history")
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        writeHistoryToFile();
                    }
                });
    }

}

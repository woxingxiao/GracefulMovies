package com.xw.project.gracefulmovies.view.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.xw.project.gracefulmovies.R;

public class SearchActivity extends BaseActivity {

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bridge, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
}

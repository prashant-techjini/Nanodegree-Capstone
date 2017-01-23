package com.nanodegree.topnews.newslist;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.BookmarksHelper;
import com.nanodegree.topnews.data.BookmarksProvider;
import com.nanodegree.topnews.databinding.ActivityBookmarksListBinding;
import com.nanodegree.topnews.drawermenu.DrawerActivity;
import com.nanodegree.topnews.model.Article;
import com.nanodegree.topnews.newsdetail.NewsDetailActivity;
import com.nanodegree.topnews.newsdetail.NewsDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class BookmarksListActivity extends DrawerActivity implements
        NewsListAdapter.NewsItemSelectionListener, LoaderManager.LoaderCallbacks<Cursor>,
        NewsListFragment.OnFragmentInteractionListener {

    private static final String[] PROJECTION = {BookmarksHelper.BOOKMARK_COLUMN_ID,
            BookmarksHelper.BOOKMARK_COLUMN_TITLE, BookmarksHelper.BOOKMARK_COLUMN_PUBLISHED_AT,
            BookmarksHelper.BOOKMARK_COLUMN_URL, BookmarksHelper.BOOKMARK_COLUMN_IMAGE_URL};
    private ActivityBookmarksListBinding binding;
    private NewsListFragment newsListFragment;
    private boolean isTablet = false;
    private static int LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_bookmarks_list,
                drawerBinding.flContentMain, true);

        Toolbar toolbar = binding.toolbar.toolbarBookmarksList;
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            ViewCompat.setElevation(toolbar, 8);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle(R.string.bookmarks);
            }
        }

        newsListFragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_news_list);
        FrameLayout layoutNewsDetail = (FrameLayout) findViewById(R.id.fl_container_news_detail);
        if (layoutNewsDetail != null) {
            isTablet = true;

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container_news_detail, new NewsDetailFragment());
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            isTablet = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BookmarksProvider.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() == 0) {
            return;
        }
        cursor.moveToFirst();

        List<Article> articleList = new ArrayList<Article>();
        do {
            Article article = new Article();
            article.setTitle(cursor.getString(cursor.getColumnIndex(BookmarksHelper.BOOKMARK_COLUMN_TITLE)));
            article.setPublishedAt(cursor.getString(cursor.getColumnIndex(BookmarksHelper.BOOKMARK_COLUMN_PUBLISHED_AT)));
            article.setUrl(cursor.getString(cursor.getColumnIndex(BookmarksHelper.BOOKMARK_COLUMN_URL)));
            article.setUrlToImage(cursor.getString(cursor.getColumnIndex(BookmarksHelper.BOOKMARK_COLUMN_IMAGE_URL)));
            articleList.add(article);
        } while (cursor.moveToNext());

        newsListFragment.setData(articleList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onArticleSelected(int position) {

        if (isTablet) {
            NewsListAdapter adapter = newsListFragment.getAdapter();
            NewsListAdapter.ArticleViewHolder viewHolderLastSelected =
                    (NewsListAdapter.ArticleViewHolder) newsListFragment
                            .getRecyclerView().findViewHolderForLayoutPosition(adapter.selectedIndex);

            if (viewHolderLastSelected != null) {
                viewHolderLastSelected.binding.rlNewsListItem.setBackgroundColor(Color.TRANSPARENT);
            }

            NewsListAdapter.ArticleViewHolder viewHolderSelected =
                    (NewsListAdapter.ArticleViewHolder) newsListFragment
                            .getRecyclerView().findViewHolderForLayoutPosition(adapter.selectedIndex);

            if (viewHolderSelected != null) {
                viewHolderSelected.binding.rlNewsListItem.setBackgroundColor(ContextCompat.getColor(this,
                        R.color.selected_item_bg));
            }

            adapter.selectedIndex = position;

            NewsDetailFragment movieDetailFragment = new NewsDetailFragment();

            Gson gson = new Gson();
            String jsonString = gson.toJson(newsListFragment.getAdapter().getArticleList().get(position));
            getIntent().putExtra(Constants.NEWS_DETAIL, jsonString);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container_news_detail, movieDetailFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            Intent intent = new Intent(this, NewsDetailActivity.class);
            Gson gson = new Gson();
            String jsonString = gson.toJson(newsListFragment.getAdapter().getArticleList().get(position));
            intent.putExtra(Constants.NEWS_DETAIL, jsonString);
            startActivity(intent);
        }
    }

    @Override
    public void onListLoaded(List<Article> articles) {

    }
}

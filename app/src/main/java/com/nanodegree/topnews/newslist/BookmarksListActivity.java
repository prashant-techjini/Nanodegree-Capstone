package com.nanodegree.topnews.newslist;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.BookmarksHelper;
import com.nanodegree.topnews.data.BookmarksProvider;
import com.nanodegree.topnews.databinding.ActivityBookmarksListBinding;
import com.nanodegree.topnews.drawermenu.DrawerActivity;
import com.nanodegree.topnews.model.Article;
import com.nanodegree.topnews.newsdetail.NewsDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class BookmarksListActivity extends DrawerActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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
                actionBar.setTitle(R.string.app_name);
            }
        }

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerBinding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerBinding.drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerBinding.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
}

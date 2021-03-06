package com.nanodegree.topnews.newslist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.databinding.ActivityNewsListBinding;
import com.nanodegree.topnews.drawermenu.DrawerActivity;
import com.nanodegree.topnews.model.Article;
import com.nanodegree.topnews.model.NewsSource;
import com.nanodegree.topnews.newsdetail.NewsDetailActivity;
import com.nanodegree.topnews.newsdetail.NewsDetailFragment;
import com.nanodegree.topnews.newssource.NewsSourceActivity;
import com.nanodegree.topnews.util.Utils;
import com.nanodegree.topnews.widget.TopNewsWidget;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsListActivity extends DrawerActivity implements View.OnClickListener,
        NewsListAdapter.NewsItemSelectionListener, NewsDetailFragment.ImageLoadingCallback,
        NewsListFragment.OnFragmentInteractionListener {

    private ActivityNewsListBinding binding;
    private NewsListFragment newsListFragment;
    private boolean isTablet = false;
    private FirebaseRemoteConfig remoteConfig;
    private NewsSource newsSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_news_list,
                drawerBinding.flContentMain, true);

        binding.toolbar.setClickHandler(this);

        Toolbar toolbar = binding.toolbar.toolbarNewsList;
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

        updateNewsSourceDisplay();

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

    private void updateNewsSourceDisplay() {
        NewsSource newsSource = Utils.getCurrentNewsSource(this);

        if (!TextUtils.isEmpty(newsSource.getUrlsToLogos().getSmall())) {
            Picasso.with(this).load(newsSource.getUrlsToLogos().getSmall()).
                    into(binding.toolbar.ivSourceLogo);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNewsSourceDisplay();
        newsListFragment.refreshNewsList();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_source_logo:
                Intent intent = new Intent(this, NewsSourceActivity.class);
                startActivityForResult(intent, 100);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        String jsonString = data.getStringExtra(Constants.NEWS_SOURCE);
        Gson gson = new Gson();
        newsSource = gson.fromJson(jsonString, NewsSource.class);

        if (!TextUtils.isEmpty(newsSource.getUrlsToLogos().getSmall())) {
            Picasso.with(this).load(newsSource.getUrlsToLogos().getSmall())
                    .into(binding.toolbar.ivSourceLogo);
        }
        newsListFragment.updateNewsSource(newsSource.getId());

        Preferences.putString(this, Constants.NEWS_SOURCE_ID, newsSource.getId());
        Preferences.putString(this, Constants.NEWS_SOURCE_NAME, newsSource.getName());
        Preferences.putString(this, Constants.NEWS_SOURCE_LOGO_URL, newsSource.getUrlsToLogos().getSmall());

        Intent intent = new Intent(this, TopNewsWidget.class);
        intent.setAction(TopNewsWidget.ACTION_NEWS_WIDGET_UPDATE);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        sendBroadcast(intent);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {

    }

    @Override
    public void onListLoaded(List<Article> articles) {
        if (articles.size() > 0 && isTablet) {
            onArticleSelected(0);
        }
    }
}

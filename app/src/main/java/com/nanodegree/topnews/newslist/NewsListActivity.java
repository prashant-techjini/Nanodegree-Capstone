package com.nanodegree.topnews.newslist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.databinding.ActivityNewsListBinding;
import com.nanodegree.topnews.drawermenu.DrawerActivity;
import com.nanodegree.topnews.model.NewsSource;
import com.nanodegree.topnews.newsdetail.NewsDetailActivity;
import com.nanodegree.topnews.newsdetail.NewsDetailFragment;
import com.nanodegree.topnews.newssource.NewsSourceActivity;
import com.squareup.picasso.Picasso;

public class NewsListActivity extends DrawerActivity implements View.OnClickListener,
        NewsListAdapter.NewsItemSelectionListener {

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
        initRemoteConfig();

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

        String newsSourceId = Preferences.getString(this, Constants.NEWS_SOURCE_ID);
        String newsSourceName = Preferences.getString(this, Constants.NEWS_SOURCE_NAME);
        String newsSourceLogoUrl = Preferences.getString(this, Constants.NEWS_SOURCE_LOGO_URL);

        if ("".equals(newsSourceName)) {
            newsSourceName = FirebaseRemoteConfig.getInstance().getString("default_source_name");
            newsSourceLogoUrl = FirebaseRemoteConfig.getInstance().getString("default_source_logo_url");
        }

        binding.toolbar.tvSourceName.setText(newsSourceName);
        if (!TextUtils.isEmpty(newsSourceLogoUrl)) {
            Picasso.with(this).load(newsSourceLogoUrl).into(binding.toolbar.ivSourceLogo);
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

    private void initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance();

        remoteConfig.setDefaults(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        remoteConfig.setConfigSettings(remoteConfigSettings);
        fetchRemoteConfigValues();
    }

    private void fetchRemoteConfigValues() {
        long cacheExpiration = 3600;

        //expire the cache immediately for development mode.
        if (remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        remoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            remoteConfig.activateFetched();
                        } else {
                            //task failed
                        }
                    }
                });
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

        binding.toolbar.tvSourceName.setText(newsSource.getName());
        if (!TextUtils.isEmpty(newsSource.getUrlsToLogos().getSmall())) {
            Picasso.with(this).load(newsSource.getUrlsToLogos().getSmall())
                    .into(binding.toolbar.ivSourceLogo);
        }
        newsListFragment.updateNewsSource(newsSource.getId());
    }

}

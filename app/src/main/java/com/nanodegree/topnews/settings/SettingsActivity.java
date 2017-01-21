package com.nanodegree.topnews.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.databinding.ActivitySettingsBinding;
import com.nanodegree.topnews.drawermenu.DrawerActivity;
import com.nanodegree.topnews.model.NewsSource;
import com.nanodegree.topnews.newssource.NewsSourceActivity;

public class SettingsActivity extends DrawerActivity implements View.OnClickListener {
    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_settings,
                drawerBinding.flContentMain, true);
        binding.setClickHandler(this);

        Toolbar toolbar = binding.toolbar.toolbarSettings;
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            ViewCompat.setElevation(toolbar, 8);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle(R.string.settings);
            }
        }

        binding.tvSourceName.setText(Preferences.getString(this, Constants.NEWS_SOURCE_NAME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_settings:
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
        NewsSource newsSource = gson.fromJson(jsonString, NewsSource.class);

        binding.tvSourceName.setText(newsSource.getName());
    }
}

package com.nanodegree.topnews.newssource;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.databinding.ActivityNewsSourceBinding;

public class NewsSourceActivity extends AppCompatActivity {
    ActivityNewsSourceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_source);

        Toolbar toolbar = binding.toolbar.toolbarNewsSource;
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            ViewCompat.setElevation(toolbar, 4 * getResources().getDisplayMetrics().density);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle(R.string.selecte_news_source);
            }
        }
    }
}

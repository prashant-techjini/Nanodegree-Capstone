package com.nanodegree.topnews.newssource;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
            //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= 21) {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_arrow_back);
            }
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.white));
            toolbar.setNavigationIcon(drawable);

            ViewCompat.setElevation(toolbar, 4 * getResources().getDisplayMetrics().density);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle(R.string.select_news_source);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

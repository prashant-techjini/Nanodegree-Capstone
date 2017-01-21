package com.nanodegree.topnews.drawermenu;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.nanodegree.topnews.BaseActivity;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.databinding.ActivityDrawerBinding;
import com.nanodegree.topnews.newslist.BookmarksListActivity;
import com.nanodegree.topnews.newslist.NewsListActivity;

public class DrawerActivity extends BaseActivity {
    protected ActivityDrawerBinding drawerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerBinding = DataBindingUtil.setContentView(this, R.layout.activity_drawer);

        drawerBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;

                switch (item.getItemId()) {
                    case R.id.nav_item_home:
                        if (!(DrawerActivity.this instanceof NewsListActivity)) {
                            intent = new Intent(DrawerActivity.this, NewsListActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_item_bookmarks:
                        if (!(DrawerActivity.this instanceof BookmarksListActivity)) {
                            intent = new Intent(DrawerActivity.this, BookmarksListActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_item_settings:
//                        if (!(DrawerActivity.this instanceof SettingsActivity)) {
//                            intent = new Intent(DrawerActivity.this, SettingsActivity.class);
//                            startActivity(intent);
//                        }
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DrawerActivity.this instanceof NewsListActivity) {
            drawerBinding.navView.getMenu().getItem(0).setChecked(true);
        }
        else if (DrawerActivity.this instanceof BookmarksListActivity) {
            drawerBinding.navView.getMenu().getItem(1).setChecked(true);
        }
//        else if (DrawerActivity.this instanceof SettingsActivity) {
//            drawerBinding.navView.getMenu().getItem(2).setChecked(true);
//        }
    }
}

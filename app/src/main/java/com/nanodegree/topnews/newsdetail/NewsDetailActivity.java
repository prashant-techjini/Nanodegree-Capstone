package com.nanodegree.topnews.newsdetail;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nanodegree.topnews.BaseActivity;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.databinding.ActivityNewsDetailBinding;

import java.util.List;

public class NewsDetailActivity extends BaseActivity implements NewsDetailFragment.ImageLoadingCallback {

    private ActivityNewsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        Toolbar toolbar = binding.toolbar.toolbarNewsDetail;
        if (toolbar != null) {
            //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

            Drawable drawable;
            if (Build.VERSION.SDK_INT >= 21) {
                drawable = ContextCompat.getDrawable(NewsDetailActivity.this,
                        R.drawable.ic_arrow_back);
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_arrow_back);
            }
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.white));
            binding.toolbar.toolbarNewsDetail.setNavigationIcon(drawable);

            ViewCompat.setElevation(toolbar, 4 * getResources().getDisplayMetrics().density);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle("");
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

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        new DynamicColorPickerTask().execute(bitmap);
    }

    private Palette.Swatch getPaletteSwatchBackIcon(Bitmap bitmap) {
        Palette.Swatch swatch = null;
        if (bitmap != null) {
            Palette palette = Palette.from(bitmap).generate();

            //Pick the appropriate color based on requirements
            if (swatch == null) {
                swatch = palette.getLightVibrantSwatch();
            }
            if (swatch == null) {
                swatch = palette.getLightMutedSwatch();
            }
            if (swatch == null) {
                swatch = palette.getDarkVibrantSwatch();
            }
            if (swatch == null) {
                swatch = palette.getDarkMutedSwatch();
            }
        }
        return swatch;
    }

    private void updateBackIconFromPaletteSwatch(Palette.Swatch swatch) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = ContextCompat.getDrawable(NewsDetailActivity.this,
                    R.drawable.ic_arrow_back);
        } else {
            drawable = getResources().getDrawable(R.drawable.ic_arrow_back);
        }
        DrawableCompat.setTint(drawable, swatch.getRgb());
        binding.toolbar.toolbarNewsDetail.setNavigationIcon(drawable);
    }

    private class DynamicColorPickerTask extends AsyncTask<Bitmap, String, Palette.Swatch> {
        @Override
        protected Palette.Swatch doInBackground(Bitmap... params) {
            return getPaletteSwatchBackIcon(params[0]);
        }

        @Override
        protected void onPostExecute(Palette.Swatch swatch) {
            updateBackIconFromPaletteSwatch(swatch);
        }
    }

}

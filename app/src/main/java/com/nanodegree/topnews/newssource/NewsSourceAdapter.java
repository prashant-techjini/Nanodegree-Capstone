package com.nanodegree.topnews.newssource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.databinding.ListItemNewsSourceBinding;
import com.nanodegree.topnews.model.NewsSource;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsSourceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private NewsSourceActivity activity;
    private Context context;
    private List<NewsSource> newsSourceList;
    public int selectedIndex = 0;

    public NewsSourceAdapter(Activity activity, Context context, List<NewsSource> newsSourceList) {
        if (activity instanceof NewsSourceActivity) {
            this.activity = (NewsSourceActivity) activity;
        }
        this.context = context;
        this.newsSourceList = newsSourceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemNewsSourceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.list_item_news_source, parent, false);
        RecyclerView.ViewHolder viewHolder = new NewsSourceViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsSourceViewHolder viewHolder = (NewsSourceViewHolder) holder;

        final NewsSource newsSource = newsSourceList.get(position);

        if (newsSource != null && !TextUtils.isEmpty(newsSource.getUrlsToLogos().getSmall())) {
            Picasso.with(context).load(newsSource.getUrlsToLogos().getSmall())
                    .into(viewHolder.binding.ivSourceItemLogo);
        } else {
            viewHolder.binding.ivSourceItemLogo.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.binding.tvSourceItemTitle.setText(newsSource.getName());
    }

    @Override
    public int getItemCount() {
        return newsSourceList != null ? newsSourceList.size() : 0;
    }

    public class NewsSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ListItemNewsSourceBinding binding;


        public NewsSourceViewHolder(ListItemNewsSourceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setClickHandler(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_news_source_item:
                    int position = getLayoutPosition();
                    //activity.onArticleSelected(position);
                    selectedIndex = position;
                    NewsSource newsSource = newsSourceList.get(position);

                    Preferences.putString(context, Constants.NEWS_SOURCE_ID, newsSource.getId());
                    Preferences.putString(context, Constants.NEWS_SOURCE_NAME, newsSource.getName());
                    Preferences.putString(context, Constants.NEWS_SOURCE_LOGO_URL,
                            newsSource.getUrlsToLogos().getSmall());

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(newsSource);

                    Intent intent = new Intent();
                    intent.putExtra(Constants.NEWS_SOURCE, jsonString);

                    activity.setResult(100, intent);
                    activity.finish();

                    break;

                default:
                    break;
            }
        }
    }

    public void setData(List<NewsSource> newsSourceList) {
        this.newsSourceList = newsSourceList;
        notifyDataSetChanged();
    }

    public List<NewsSource> getNewsSourceList() {
        return newsSourceList;
    }

}

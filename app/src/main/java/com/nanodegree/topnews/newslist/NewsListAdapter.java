package com.nanodegree.topnews.newslist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.BookmarksHelper;
import com.nanodegree.topnews.data.BookmarksProvider;
import com.nanodegree.topnews.databinding.ListItemArticleBinding;
import com.nanodegree.topnews.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private NewsListActivity activity;
    private Context context;
    private List<Article> articleList;
    public int selectedIndex = 0;
    private static final String[] PROJECTION = {BookmarksHelper.BOOKMARK_COLUMN_ID,
            BookmarksHelper.BOOKMARK_COLUMN_TITLE, BookmarksHelper.BOOKMARK_COLUMN_PUBLISHED_AT,
            BookmarksHelper.BOOKMARK_COLUMN_URL, BookmarksHelper.BOOKMARK_COLUMN_IMAGE_URL};

    interface NewsItemSelectionListener {
        void onArticleSelected();
    }

    public NewsListAdapter(Activity activity, Context context, List<Article> articleList) {
        if (activity instanceof NewsListActivity) {
            this.activity = (NewsListActivity) activity;
        }
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemArticleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item_article, parent, false);
        RecyclerView.ViewHolder viewHolder = new ArticleViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder viewHolder = (ArticleViewHolder) holder;

        final Article article = articleList.get(position);

        if (isBookmarked(article)) {
            viewHolder.binding.ivArticleBookmark.setSelected(true);
        } else {
            viewHolder.binding.ivArticleBookmark.setSelected(false);
        }

        if (article != null && article.getUrlToImage() != null) {
            Picasso.with(context).load(article.getUrlToImage())
                    .into(viewHolder.binding.ivArticleImage);
        } else {
            viewHolder.binding.ivArticleImage.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.binding.tvArticleTitle.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ListItemArticleBinding binding;

        public ArticleViewHolder(ListItemArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setClickHandler(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_news_list_item:
                    int position = getLayoutPosition();
                    activity.onArticleSelected(position);
                    selectedIndex = position;
                    break;

                case R.id.iv_article_bookmark:
                    Article article = articleList.get(getLayoutPosition());
                    if (binding.ivArticleBookmark.isSelected()) {
                        binding.ivArticleBookmark.setSelected(false);
                        //notifyItemRemoved(getLayoutPosition());
                        deleteBookmark(article);
                    } else {
                        binding.ivArticleBookmark.setSelected(true);
                        addBookmark(article);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void setData(List<Article> articleList) {
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    private void addBookmark(Article article) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_TITLE, article.getTitle());
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_URL, article.getUrl());
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_IMAGE_URL, article.getUrlToImage());
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_PUBLISHED_AT, article.getPublishedAt());
        context.getContentResolver().insert(BookmarksProvider.CONTENT_URI, contentValues);
    }

    private void deleteBookmark(Article article) {
        context.getContentResolver().delete(BookmarksProvider.CONTENT_URI,
                BookmarksHelper.BOOKMARK_COLUMN_URL + "=?",
                new String[]{article.getUrl()});
    }

    private boolean isBookmarked(Article article) {
        Cursor cursor = context.getContentResolver().query(BookmarksProvider.CONTENT_URI,
                PROJECTION, BookmarksHelper.BOOKMARK_COLUMN_URL + "=?",
                new String[]{article.getUrl()}, null);

        return cursor.getCount() > 0;
    }

}

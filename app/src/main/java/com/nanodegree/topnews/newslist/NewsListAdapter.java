package com.nanodegree.topnews.newslist;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private NewsListActivity activity;
    private Context context;
    private List<Article> articleList;
    public int selectedIndex = 0;

    public NewsListAdapter(Activity activity, Context context, List<Article> articleList) {
        if (activity instanceof NewsListActivity) {
            this.activity = (NewsListActivity) activity;
        }
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        RecyclerView.ViewHolder viewHolder = new ArticleViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder viewHolder = (ArticleViewHolder) holder;

        final Article article = articleList.get(position);

        if (article != null && article.getUrlToImage() != null) {
            Picasso.with(context).load(article.getUrlToImage()).into(viewHolder.imageArticle);
        } else {
            viewHolder.imageArticle.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.textArticleTitle.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatImageView imageArticle;
        public AppCompatImageView imageBookmark;
        public TextView textArticleTitle;
        public RelativeLayout baseLayout;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            imageArticle = (AppCompatImageView) itemView.findViewById(R.id.iv_article_image);
            imageBookmark = (AppCompatImageView) itemView.findViewById(R.id.iv_article_bookmark);
            textArticleTitle = (TextView) itemView.findViewById(R.id.tv_article_title);
            baseLayout = (RelativeLayout) itemView.findViewById(R.id.rl_news_list_item);

            itemView.setOnClickListener(this);
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

}

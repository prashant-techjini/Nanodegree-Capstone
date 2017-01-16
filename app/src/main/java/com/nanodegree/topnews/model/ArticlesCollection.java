package com.nanodegree.topnews.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Prashant Nayak
 */

public class ArticlesCollection {

    private String status = null;
    private String source = null;
    private String sortBy = null;
    private List<Article> articles = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}

package com.nanodegree.topnews.model;

import java.util.List;

/**
 * @author Prashant Nayak
 */

public class NewsSourcesCollection {
    private String status = null;
    private List<NewsSource> sources = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NewsSource> getSources() {
        return sources;
    }

    public void setSources(List<NewsSource> sources) {
        this.sources = sources;
    }
}

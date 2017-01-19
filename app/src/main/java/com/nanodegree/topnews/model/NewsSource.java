package com.nanodegree.topnews.model;

/**
 * @author Prashant Nayak
 */

public class NewsSource {
    private String id = null;
    private String name = null;
    private UrlsToLogos urlsToLogos = null;

    public static class UrlsToLogos {
        private String small = null;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UrlsToLogos getUrlsToLogos() {
        return urlsToLogos;
    }

    public void setUrlsToLogos(UrlsToLogos urlsToLogos) {
        this.urlsToLogos = urlsToLogos;
    }
}

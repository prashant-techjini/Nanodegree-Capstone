package com.nanodegree.topnews.retrofit;

import com.nanodegree.topnews.model.ArticlesCollection;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Prashant Nayak
 */

public interface ApiInterface {
    @GET("articles")
    Observable<ArticlesCollection> getArticles(
            @Query("source") String source,
            @Query("apiKey") String apiKey);
}

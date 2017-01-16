package com.nanodegree.topnews.interactor;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.model.ArticlesCollection;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Prashant Nayak
 */

public class GetNewsListUseCase extends UseCase<ArticlesCollection> {
    public GetNewsListUseCase(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Observable<ArticlesCollection> buildObservable() {
        String newsSource = FirebaseRemoteConfig.getInstance().getString("default_source");
        return api.getArticles(newsSource, context.getString(R.string.newsapi_api_key));
    }

    public void getNewsList(Subscriber useCaseSubscriber) {
        execute(useCaseSubscriber);
    }
}

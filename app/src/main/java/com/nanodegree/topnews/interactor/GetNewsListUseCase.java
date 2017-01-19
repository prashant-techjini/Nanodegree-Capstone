package com.nanodegree.topnews.interactor;

import android.content.Context;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.model.ArticlesCollection;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Prashant Nayak
 */

public class GetNewsListUseCase extends UseCase<ArticlesCollection> {
    private String sourceId;

    public GetNewsListUseCase(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Observable<ArticlesCollection> buildObservable() {
        return api.getArticles(sourceId, context.getString(R.string.newsapi_api_key));
    }

    public void getNewsList(Subscriber useCaseSubscriber, String sourceId) {
        this.sourceId = sourceId;
        execute(useCaseSubscriber);
    }
}

package com.nanodegree.topnews.interactor;

import android.content.Context;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.model.NewsSourcesCollection;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Prashant Nayak
 */

public class GetNewsSourceUseCase extends UseCase<NewsSourcesCollection> {
    public GetNewsSourceUseCase(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Observable<NewsSourcesCollection> buildObservable() {
        return api.getSources("en", context.getString(R.string.newsapi_api_key));
    }

    public void getSources(Subscriber useCaseSubscriber) {
        execute(useCaseSubscriber);
    }
}

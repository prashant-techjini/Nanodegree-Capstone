package com.nanodegree.topnews.interactor;

import android.content.Context;

import com.nanodegree.topnews.retrofit.ApiInterface;
import com.nanodegree.topnews.retrofit.ApiProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author Prashant Nayak
 *         Reference: https://github.com/android10/Android-CleanArchitecture
 */

public abstract class UseCase<T> {
    protected final ApiInterface api;
    protected Context context;

    private Subscription subscription = Subscriptions.empty();

    public abstract Observable<T> buildObservable();

    public UseCase(final Context context) {
        this.context = context;
        api = ApiProvider.getApiService(context);
    }

    /**
     * Unsubscribes from current {@link rx.Subscription}.
     */
    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber will listen to the observable build
     *                          with {@link #buildObservable()}.
     */
    @SuppressWarnings("unchecked")
    protected void execute(final Subscriber useCaseSubscriber) {
        this.subscription = this.buildObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(useCaseSubscriber);
    }
}

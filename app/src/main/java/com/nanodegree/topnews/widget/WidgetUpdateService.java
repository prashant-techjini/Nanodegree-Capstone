package com.nanodegree.topnews.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;

import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.interactor.GetNewsListUseCase;
import com.nanodegree.topnews.model.ArticlesCollection;

import rx.Subscriber;

public class WidgetUpdateService extends Service {
    private GetNewsListUseCase getNewsListUseCase;
    private int[] appWidgetIds;

    public WidgetUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            appWidgetIds = intent.getIntArrayExtra("WIDGET_ID_LIST");
            getNewsListUseCase = new GetNewsListUseCase(this);
            String newsSourceId = Preferences.getString(this, Constants.NEWS_SOURCE_ID);
            doApiCallGetNewsList(newsSourceId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void doApiCallGetNewsList(String newsSourceId) {
        getNewsListUseCase.getNewsList(new GetNewsListSubscriber(), newsSourceId);
    }

    private void updateWidget(ArticlesCollection articlesCollection) {
        String title = articlesCollection.getArticles().size() > 0
                ? articlesCollection.getArticles().get(0).getTitle() : "";
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            TopNewsWidget.updateAppWidget(this, AppWidgetManager.getInstance(this), appWidgetId, title);
        }
    }

    public class GetNewsListSubscriber extends Subscriber<ArticlesCollection> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ArticlesCollection articlesCollection) {
            updateWidget(articlesCollection);
        }
    }
}

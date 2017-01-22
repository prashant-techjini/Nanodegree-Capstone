package com.nanodegree.topnews.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nanodegree.topnews.R;

/**
 * Implementation of App Widget functionality.
 */
public class TopNewsWidget extends AppWidgetProvider {
    public static final String ACTION_NEWS_WIDGET_UPDATE = "com.nanodegree.topnews.action.APPWIDGET_UPDATE";
    private static int[] widgetIds;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String title) {

        CharSequence widgetText = title;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.top_news_widget);
        views.setTextViewText(R.id.tv_widget_title, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        widgetIds = appWidgetIds;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!intent.getAction().equals(ACTION_NEWS_WIDGET_UPDATE)) {
            super.onReceive(context, intent);
        }
        Intent intent1 = new Intent(context, WidgetUpdateService.class);
        intent1.putExtra("WIDGET_ID_LIST", widgetIds);
        context.startService(intent1);
    }
}
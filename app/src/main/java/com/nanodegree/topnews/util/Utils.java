package com.nanodegree.topnews.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.model.NewsSource;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Prashant Nayak
 */

public class Utils {

    public static String getDisplayTextTime(String timeString) {
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String time = "";
        try {
            Date date = format.parse(timeString);
            SimpleDateFormat printFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault());
            printFormat.setTimeZone(TimeZone.getDefault());
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            printFormat.setDateFormatSymbols(symbols);
            time = printFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static boolean isInternetConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static NewsSource getCurrentNewsSource(Context context) {
        String newsSourceId = Preferences.getString(context, Constants.NEWS_SOURCE_ID);
        String newsSourceName = Preferences.getString(context, Constants.NEWS_SOURCE_NAME);
        String newsSourceLogoUrl = Preferences.getString(context, Constants.NEWS_SOURCE_LOGO_URL);

        //user has not selected any news source, select the default one from Firebase remote config
        if ("".equals(newsSourceId) || "".equals(newsSourceName) || "".equals(newsSourceLogoUrl)) {
            newsSourceId = FirebaseRemoteConfig.getInstance().getString("default_source_id");
            newsSourceName = FirebaseRemoteConfig.getInstance().getString("default_source_name");
            newsSourceLogoUrl = FirebaseRemoteConfig.getInstance().getString("default_source_logo_url");
        }

        NewsSource newsSource = new NewsSource();
        newsSource.setId(newsSourceId);
        newsSource.setName(newsSourceName);
        newsSource.getUrlsToLogos().setSmall(newsSourceLogoUrl);
        return newsSource;
    }
}

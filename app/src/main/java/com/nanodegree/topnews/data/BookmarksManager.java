package com.nanodegree.topnews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nanodegree.topnews.model.Article;

/**
 * @author Prashant Nayak
 */

public class BookmarksManager {

    private static final String[] PROJECTION = {BookmarksHelper.BOOKMARK_COLUMN_ID,
            BookmarksHelper.BOOKMARK_COLUMN_TITLE, BookmarksHelper.BOOKMARK_COLUMN_PUBLISHED_AT,
            BookmarksHelper.BOOKMARK_COLUMN_URL, BookmarksHelper.BOOKMARK_COLUMN_IMAGE_URL};

    public static void addBookmark(Context context, Article article) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_TITLE, article.getTitle());
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_URL, article.getUrl());
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_IMAGE_URL, article.getUrlToImage());
        contentValues.put(BookmarksHelper.BOOKMARK_COLUMN_PUBLISHED_AT, article.getPublishedAt());
        context.getContentResolver().insert(BookmarksProvider.CONTENT_URI, contentValues);
    }

    public static void deleteBookmark(Context context, Article article) {
        context.getContentResolver().delete(BookmarksProvider.CONTENT_URI,
                BookmarksHelper.BOOKMARK_COLUMN_URL + "=?",
                new String[]{article.getUrl()});
    }

    public static boolean isBookmarked(Context context, Article article) {
        Cursor cursor = context.getContentResolver().query(BookmarksProvider.CONTENT_URI,
                PROJECTION, BookmarksHelper.BOOKMARK_COLUMN_URL + "=?",
                new String[]{article.getUrl()}, null);

        return cursor.getCount() > 0;
    }
}

package com.nanodegree.topnews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class BookmarksProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.nanodegree.topnews.BookmarksProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/bookmarks";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private SQLiteDatabase db;
    private static final int BOOKMARKS = 0;
    private static final int BOOKMARK_ID = 1;
    private static HashMap<String, String> projectionMap;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "bookmarks", BOOKMARKS);
        uriMatcher.addURI(PROVIDER_NAME, "bookmarks/#", BOOKMARK_ID);
    }

    public BookmarksProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case BOOKMARKS:
                count = db.delete(BookmarksHelper.TABLE_BOOKMARK, selection, selectionArgs);
                break;

            case BOOKMARK_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(BookmarksHelper.TABLE_BOOKMARK, BookmarksHelper.BOOKMARK_COLUMN_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new SQLiteException("unknown uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(BookmarksHelper.TABLE_BOOKMARK, "", values);
        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("insert failed");
    }

    @Override
    public boolean onCreate() {
        BookmarksHelper bookmarksHelper = new BookmarksHelper(getContext());
        db = bookmarksHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BookmarksHelper.TABLE_BOOKMARK);

        switch (uriMatcher.match(uri)) {
            case BOOKMARKS:
                queryBuilder.setProjectionMap(projectionMap);
                break;

            case BOOKMARK_ID:
                queryBuilder.appendWhere(BookmarksHelper.BOOKMARK_COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

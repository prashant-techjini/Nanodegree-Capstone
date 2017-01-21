package com.nanodegree.topnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Prashant Nayak
 */

public class BookmarksHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Bookmarks";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_BOOKMARK = "bookmarks";
    public static final String BOOKMARK_COLUMN_ID = "_id";
    public static final String BOOKMARK_COLUMN_TITLE = "title";
    public static final String BOOKMARK_COLUMN_URL = "url";
    public static final String BOOKMARK_COLUMN_IMAGE_URL = "image_url";
    public static final String BOOKMARK_COLUMN_PUBLISHED_AT = "published_at";

    public BookmarksHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "";
        createTableSql = "CREATE TABLE "
                + TABLE_BOOKMARK;
        createTableSql += " ( ";
        createTableSql += BOOKMARK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , ";
        createTableSql += BOOKMARK_COLUMN_TITLE + " TEXT, ";
        createTableSql += BOOKMARK_COLUMN_URL + " TEXT, ";
        createTableSql += BOOKMARK_COLUMN_IMAGE_URL + " TEXT, ";
        createTableSql += BOOKMARK_COLUMN_PUBLISHED_AT + " TEXT ";
        createTableSql += " ) ";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        onCreate(db);
    }
}

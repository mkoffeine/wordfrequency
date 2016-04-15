package com.koffeine.wordfrequency2.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.koffeine.wordfrequency2.Logger;

/**
 * Created by mKoffeine on 15.04.2016.
 */
public class WordFreqProvider extends ContentProvider {
    private static Logger logger = Logger.getLogger(WordFreqProvider.class.getSimpleName());
    private static final String AUTHORITY = "com.koffeine.wordfrequency2.WordFreqProvider";
    private static final String WORD_FREQ_PATH = "WordFreqProvider";
    public static final Uri WORD_FREQ_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + WORD_FREQ_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, WORD_FREQ_PATH, 1);
        uriMatcher.addURI(AUTHORITY, WORD_FREQ_PATH + "/#", 2);
    }

    static final String WORD_FREQ_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + WORD_FREQ_PATH;

    // одна строка
    static final String WORD_FREQ_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + WORD_FREQ_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_WORD_FREQS = 1;

    // Uri с указанным ID
    static final int URI_WORD_FREQ_ID = 2;

//    static final String DB_NAME = "mydb";
//    static final int DB_VERSION = 1;


    static final String ID = "_id";
    static final String WORD_NAME = "name";

    private WordSQLHelper dbHelper;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        dbHelper = new WordSQLHelper(getContext());
        logger.debug("WordFreqProvider oncreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        logger.debug("query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WORD_FREQS:
                logger.debug("URI_WORD_FREQS");
                break;
            case URI_WORD_FREQ_ID:
                String id = uri.getLastPathSegment();
                logger.debug("URI_WORD_FREQ_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(WordSQLHelper.WORDS_TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                WORD_FREQ_CONTENT_URI);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        logger.debug("insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_WORD_FREQS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(WordSQLHelper.WORDS_TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(WORD_FREQ_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        logger.debug("delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WORD_FREQS:
                logger.debug("URI_WORD_FREQS");
                break;
            case URI_WORD_FREQ_ID:
                String id = uri.getLastPathSegment();
                logger.debug("URI_WORD_FREQ_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(WordSQLHelper.WORDS_TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        logger.debug("update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WORD_FREQS:
                logger.debug("URI_WORD_FREQS");

                break;
            case URI_WORD_FREQ_ID:
                String id = uri.getLastPathSegment();
                logger.debug("URI_WORD_FREQ_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(WordSQLHelper.WORDS_TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        logger.debug("getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WORD_FREQS:
                return WORD_FREQ_CONTENT_TYPE;
            case URI_WORD_FREQ_ID:
                return WORD_FREQ_CONTENT_ITEM_TYPE;
        }
        return null;
    }
}

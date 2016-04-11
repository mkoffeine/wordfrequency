package com.koffeine.wordfrequency2.model.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.koffeine.wordfrequency2.Logger;

/**
 * Created by mKoffeine on 07.04.2016.
 */
public class WordSQLHelper extends SQLiteOpenHelper {
    private Logger logger = Logger.getLogger(WordSQLHelper.class.getSimpleName());
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_WORD = "words";
    public static final String WORD_DB_NAME = "com.koffeine.word_db";
    public static final String WORDS_TABLE_NAME = "words";
    private static final String WORD_TABLE_CREATE =
            "CREATE TABLE " + WORDS_TABLE_NAME + " (" +
                    KEY_WORD + " TEXT PRIMARY KEY ASC); ";

    public WordSQLHelper(Context context) {
        super(context, WORD_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORD_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.koffeine.wordfrequency2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.koffeine.wordfrequency2.Logger;


public class WordSQLHelper extends SQLiteOpenHelper {
    private Logger logger = Logger.getLogger(WordSQLHelper.class.getSimpleName());
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_NAME = "words";
    public static final String COLUMN_ID = "_id";
    public static final String WORD_DB_NAME = "com.koffeine.word_db";
    public static final String WORDS_TABLE_NAME = "words";
    private static final String WORD_TABLE_CREATE =
            "CREATE TABLE " + WORDS_TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER primary key autoincrement, " +
                    COLUMN_NAME + " TEXT unique); ";

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

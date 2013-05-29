package com.koffeine.wordfrequency.model.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.koffeine.wordfrequency.Logger;

/**
 * Created by Michael on 28.05.13.
 */
public class WordsSQLHelper extends SQLiteOpenHelper {
    private Logger logger = Logger.getLogger(WordsSQLHelper.class.getSimpleName());
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_POSITION = "position";
    public static final String KEY_WORD = "word";
    public static final String KEY_FREQ = "freq";
    public static final String WORDS_DB_NAME = "com.koffeine.words";
    public static final String WORDS_TABLE_NAME = "words";
    private static final String WORDS_TABLE_CREATE =
//            "CREATE TABLE IF NOT EXISTS " + WORDS_TABLE_NAME + " (" +
            "CREATE TABLE IF NOT EXISTS " + WORDS_TABLE_NAME + " (" +
                    KEY_WORD + " TEXT PRIMARY KEY ASC, " +
//                    KEY_WORD + " TEXT, " +
                    KEY_POSITION + " INTEGER, " +
                    KEY_FREQ + " INTEGER);";
    final String WORDS_TABLE_DROP = "DROP TABLE IF EXISTS " + WORDS_TABLE_NAME;

    public WordsSQLHelper(Context context) {
        super(context, WORDS_DB_NAME, null, DATABASE_VERSION);
        logger.debug("WordsSQLHelper constructor");
    }


//    public WordsSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        logger.debug("WordsSQLHelper onCreate");
        sqLiteDatabase.execSQL(WORDS_TABLE_CREATE);
    }
    public void dropAndCreateTable(SQLiteDatabase db) {
        logger.debug("WordsSQLHelper dropAndCreateTable");
        db.execSQL(WORDS_TABLE_DROP);
        db.execSQL(WORDS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        logger.debug("WordsSQLHelper onUpgrade");
    }


}

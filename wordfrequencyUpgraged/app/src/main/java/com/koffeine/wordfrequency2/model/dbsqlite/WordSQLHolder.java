package com.koffeine.wordfrequency2.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.koffeine.wordfrequency2.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mKoffeine on 07.04.2016.
 */
public class WordSQLHolder {
    private Logger logger = Logger.getLogger(WordSQLHolder.class.getSimpleName());
    private WordSQLHelper helper;
    private Context context;
    private SQLiteDatabase db;
    private static final String WORD_WHERE_CLAUSE = WordSQLHelper.KEY_WORD + "=?";

    public WordSQLHolder(Context context) {
        this.context = context;
        helper = new WordSQLHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertInDB(String word) {

        ContentValues values = new ContentValues(1);
        values.put(WordSQLHelper.KEY_WORD, word);
        db.insert(WordSQLHelper.WORDS_TABLE_NAME, null, values);
        logger.debug("Word <" + word + "> was inserted ");
    }

    public void deleteWord(String word) {
        db.delete(WordSQLHelper.WORDS_TABLE_NAME, WORD_WHERE_CLAUSE, new String[]{word});
        logger.debug("Word <" + word + "> was removed ");
    }

    public List<String> getAllWords() {
        Cursor c = db.query(WordSQLHelper.WORDS_TABLE_NAME, null, null, null, null, null, null);
        logger.debug("cursor w: " + "   count: " + c.getCount());
        List<String> list = new ArrayList<>();
        if (c.getCount() > 0) {
            int columnWord = c.getColumnIndex(WordSQLHelper.KEY_WORD);
            while (c.moveToNext()) {
                list.add(c.getString(columnWord));
            }
        }
        c.close();
        return list;
    }

    public Cursor getAllWordCursor() {
        Cursor c = db.query(WordSQLHelper.WORDS_TABLE_NAME, null, null, null, null, null, null);
        logger.debug("cursor w: " + "   count: " + c.getCount());
        return c;
    }
}

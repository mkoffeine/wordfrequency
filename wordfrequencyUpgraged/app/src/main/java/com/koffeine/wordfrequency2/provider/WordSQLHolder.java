package com.koffeine.wordfrequency2.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.koffeine.wordfrequency2.Logger;


public class WordSQLHolder {
    private Logger logger = Logger.getLogger(WordSQLHolder.class.getSimpleName());
    private Context context;
    private static final String WORD_WHERE_CLAUSE = WordSQLHelper.COLUMN_NAME + "=?";

    public WordSQLHolder(Context context) {
        this.context = context;
    }

    public void insertInDB(String word) {

        ContentResolver resolver = getContentResolver();
        ContentValues value = new ContentValues(1);
        value.put(WordSQLHelper.COLUMN_NAME, word);
        resolver.insert(WordFreqProvider.WORD_FREQ_CONTENT_URI, value);
        logger.debug("Word <" + word + "> was inserted ");
    }

    private ContentResolver getContentResolver() {
        return context.getApplicationContext().getContentResolver();
    }

    public void deleteWord(String word) {
        getContentResolver().delete(
                WordFreqProvider.WORD_FREQ_CONTENT_URI,
                WORD_WHERE_CLAUSE, new String[]{word});
        logger.debug("Word <" + word + "> was removed ");
    }

    public Cursor getAllWordCursor() {
        Cursor c = getContentResolver().query(WordFreqProvider.WORD_FREQ_CONTENT_URI, null, null, null, null);
        logger.debug("cursor w: " + "   count: " + c.getCount());
        return c;
    }
}

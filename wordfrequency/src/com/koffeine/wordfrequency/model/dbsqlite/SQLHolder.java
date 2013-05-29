package com.koffeine.wordfrequency.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.koffeine.wordfrequency.Logger;
import com.koffeine.wordfrequency.model.WordInfo;

import java.util.List;

/**
 * Created by Michael on 28.05.13.
 */
public class SQLHolder {
    private Logger logger = Logger.getLogger(SQLHolder.class.getSimpleName());
    private WordsSQLHelper helper;
    private Context context;
    private SQLiteDatabase db;
    private static String WHERE_SELECT = WordsSQLHelper.KEY_WORD + "=?";

    public SQLHolder(Context context) {
        this.context = context;
        helper = new WordsSQLHelper(context);
        db = helper.getWritableDatabase();
    }
    public void insertInDB(List<WordInfo> words) {
        helper.dropAndCreateTable(db);
        db.beginTransaction();
        for (int i = 0; i < words.size(); i++) {
            WordInfo wi = words.get(i);
            ContentValues values = new ContentValues(2);
            values.put(WordsSQLHelper.KEY_FREQ, wi.getFreq());
            values.put(WordsSQLHelper.KEY_WORD, wi.getWord());
            values.put(WordsSQLHelper.KEY_POSITION, wi.getPosition());
            db.insert(WordsSQLHelper.WORDS_TABLE_NAME, null, values);
            if (i% 7000 == 1) {
                logger.debug(i+ " inserted " + wi.getWord() + "  n: " + wi.getPosition());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public WordInfo getWordInfo(String w) {
        WordInfo wi = null;
        Cursor c = db.query(WordsSQLHelper.WORDS_TABLE_NAME, null, WHERE_SELECT, new String[]{w}, null,null,null);
        logger.debug("cursor w: " + w + "   count: " + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            int columnWord = c.getColumnIndex(WordsSQLHelper.KEY_WORD);
            int columnFreq = c.getColumnIndex(WordsSQLHelper.KEY_FREQ);
            int columnPosition = c.getColumnIndex(WordsSQLHelper.KEY_POSITION);
            wi = new WordInfo(c.getString(columnWord), c.getInt(columnPosition), c.getInt(columnFreq));
        }
        c.close();
        return wi;
    }
}

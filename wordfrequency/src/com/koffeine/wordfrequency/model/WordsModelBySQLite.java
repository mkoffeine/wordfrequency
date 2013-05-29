package com.koffeine.wordfrequency.model;

import android.content.Context;
import android.os.Build;
import com.koffeine.wordfrequency.Logger;
import com.koffeine.wordfrequency.WordsFreqApplication;
import com.koffeine.wordfrequency.model.dbsqlite.SQLHolder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Michael on 28.05.13.
 */
public class WordsModelBySQLite extends AbstractWordsModel {
    private Logger logger = Logger.getLogger(WordsModelBySQLite.class.getSimpleName());
    private SQLHolder sqlHolder;

    @Override
    public void initLogic(Context context) {
        ArrayList<WordInfo> words;
        try {
            words = readDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("WordsModelBySQLite caught IOException");
        }
        sqlHolder = ((WordsFreqApplication) context).getSqlHolder();
        logger.debug("before inserting db");
        long time = System.currentTimeMillis();
        sqlHolder.insertInDB(words);
        long delta = time - System.currentTimeMillis();
        logger.debug("time insertInDB  " + delta + "   size: " + words.size());
    }
    String getDictionaryFileName() {
        return Build.VERSION.SDK_INT < 11 ? "en_full_raw_50.dic" : "en_full_raw_50.dic";
    }

    @Override
    public String getStatus(String typedWord) {
        WordInfo wi = sqlHolder.getWordInfo(typedWord);
        String status = "not found for: " + typedWord;
        if (wi != null) {
            status = "w:  " + wi.toString() + "\n\n";
        }
        return status;
    }
}

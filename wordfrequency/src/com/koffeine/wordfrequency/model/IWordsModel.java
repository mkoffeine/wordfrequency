package com.koffeine.wordfrequency.model;

import android.content.Context;

/**
 * Created by Michael on 28.05.13.
 */
public interface IWordsModel {
    String[] SUFFIXES = new String[]{"s", "ing", "es", "ly", "er", "ar", "ir", "y", "able", "e",
            "t", "ed", "en", "ist", "ful", "fy", "ian", "ize", "ible",
            "ness", "less", "ism", "hood", "ment"
            , "al", "ent", "nt", "sm", "m", "st", "an"
            , "ss", "ng", "n", "ble", "le", "r", "l"
            , "t", "ul", "sm", "n", "ood", "od", "d"};

    void initLogic(Context context);
    String getStatus(String typedWord);
}

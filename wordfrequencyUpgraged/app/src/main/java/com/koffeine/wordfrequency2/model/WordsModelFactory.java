package com.koffeine.wordfrequency2.model;

import android.os.Build;

import com.koffeine.wordfrequency2.buildproperties.BuildProperties;

/**
 * Created by Michael on 28.05.13.
 */
public class WordsModelFactory {
    public IWordsModel createWordsModel() {
        //analyze if we have a lot of memory
        IWordsModel model;
        if (Build.VERSION.SDK_INT < 11) {
            model = new WordsModelByArray();
        } else {
            if (BuildProperties.STABLE_BUILD) {
                model = new WordsModelByArray();
            } else {
                model = new WordsModelBySQLite();
            }
        }
        return model;
    }
}

package com.koffeine.wordfrequency2;

import android.app.Application;

import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.dbsqlite.SQLHolder;


public class WordsFreqApplication extends Application {
    private IWordsModel wordsModel;
    private SQLHolder sqlHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        sqlHolder = new SQLHolder(getApplicationContext());
    }

    public SQLHolder getSqlHolder() {
        return sqlHolder;
    }

    public IWordsModel getWordsModel() {
        return wordsModel;
    }

    public void setWordsModel(IWordsModel wordsModel) {
        this.wordsModel = wordsModel;
    }
}

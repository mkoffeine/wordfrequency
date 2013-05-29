package com.koffeine.wordfrequency;

import android.app.Application;
import com.koffeine.wordfrequency.model.IWordsModel;
import com.koffeine.wordfrequency.model.dbsqlite.SQLHolder;


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

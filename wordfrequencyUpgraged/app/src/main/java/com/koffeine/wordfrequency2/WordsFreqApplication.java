package com.koffeine.wordfrequency2;

import android.app.Application;

import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.dbsqlite.WordSQLHolder;


public class WordsFreqApplication extends Application {
    private IWordsModel wordsModel;
    private WordSQLHolder sqlHolder;
    private Logger logger = Logger.getLogger(WordsFreqApplication.class.getSimpleName());

    @Override
    public void onCreate() {
        super.onCreate();
        sqlHolder = new WordSQLHolder(getApplicationContext());
        logger.debug("123123 WordsFreqApplication.onCreate");
    }

    public WordSQLHolder getSqlHolder() {
        return sqlHolder;
    }

    public IWordsModel getWordsModel() {
        return wordsModel;
    }

    public void setWordsModel(IWordsModel wordsModel) {
        this.wordsModel = wordsModel;
    }


}

package com.koffeine.wordfrequency2;

import android.app.Application;

import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.provider.WordFreqProviderHolder;


public class WordsFreqApplication extends Application {
    private IWordsModel wordsModel;
    private WordFreqProviderHolder sqlHolder;
    private Logger logger = Logger.getLogger(WordsFreqApplication.class.getSimpleName());

    @Override
    public void onCreate() {
        super.onCreate();
        sqlHolder = new WordFreqProviderHolder(getApplicationContext());
    }

    public WordFreqProviderHolder getSqlHolder() {
        return sqlHolder;
    }

    public IWordsModel getWordsModel() {
        return wordsModel;
    }

    public void setWordsModel(IWordsModel wordsModel) {
        this.wordsModel = wordsModel;
    }


}

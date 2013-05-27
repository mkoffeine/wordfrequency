package com.koffeine.wordfrequency;

import android.app.Application;


public class WordsFreqApplication extends Application {
    private WordsModel wordsModel;

    public WordsModel getWordsModel() {
        return wordsModel;
    }

    public void setWordsModel(WordsModel wordsModel) {
        this.wordsModel = wordsModel;
    }
}

package com.koffeine.wordfrequency2.model.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.WordsModelByArray;


public class WordsLoader extends AsyncTaskLoader<IWordsModel> {

    public WordsLoader(Context context) {
        super(context);
    }

    @Override
    public IWordsModel loadInBackground() {
        IWordsModel wordsModel = new WordsModelByArray();
        wordsModel.initLogic(getContext().getApplicationContext());
        return wordsModel;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}

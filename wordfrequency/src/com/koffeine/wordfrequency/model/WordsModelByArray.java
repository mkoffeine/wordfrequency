package com.koffeine.wordfrequency.model;

import android.content.Context;
import android.os.Build;
import com.koffeine.wordfrequency.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


class WordsModelByArray extends AbstractWordsModel{
    private Logger logger = Logger.getLogger(WordsModelByArray.class.getSimpleName());
    private WordInfo[] wordsArray;


    public void initLogic(Context context) {
        try {
            readData();
        }
        catch (IOException e) {
            throw new IllegalStateException("WordsModelByArray caught IOException");
        }
    }
    private void readData() throws IOException {

        ArrayList<WordInfo> words = readDataFromFile();
        wordsArray = words.toArray(new WordInfo[]{});

    }

    String getDictionaryFileName() {
        return Build.VERSION.SDK_INT < 11 ? "en_full_raw_10.dic" : "en_full_raw_2.dic";
    }


    public String getStatus(String typedWord) {
        String status = "";
        if (typedWord.length() > 1 && wordsArray != null && wordsArray.length > 0) {
            int index = Arrays.binarySearch(wordsArray, new WordInfo(typedWord, 0, 0));
            index = index > -1 ? index : -index - 1;
            if (index > -1 && index < wordsArray.length) {
                WordInfo info = wordsArray[index];
                status = "w:  " + info.toString() + "\n\n";
                for (String suffix : SUFFIXES) {
                    String word = typedWord + suffix;
                    index = Arrays.binarySearch(wordsArray, new WordInfo(word, 0, 0));
                    if (index > 0 && index < wordsArray.length) {
                        status += "  " + wordsArray[index].toString() + "\n";
                    } else {
                        //status += "   --no-- " + word + "\n";
                    }
                }
            }
        }
        return status;
    }

}

package com.koffeine.wordfrequency2.model;

import android.content.Context;

import com.koffeine.wordfrequency2.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;


public class WordsModelByArray extends AbstractWordsModel {
    private Logger logger = Logger.getLogger(WordsModelByArray.class.getSimpleName());
    private WordInfo[] wordsArray;


    public void initLogic(Context context) {
        try {
            readData();
        } catch (IOException e) {
            throw new IllegalStateException("WordsModelByArray caught IOException");
        }
    }

    private void readData() throws IOException {

        ArrayList<WordInfo> words = readDataFromFile();
        wordsArray = words.toArray(new WordInfo[]{});

    }

    String getDictionaryFileName() {
        return "en_full_raw_50.dic";
    }


    public String getStatus(String typedWord) {
        String status = "";
        if (typedWord.length() > 1 && wordsArray != null && wordsArray.length > 0) {
            int index = Arrays.binarySearch(wordsArray, new WordInfo(typedWord, 0, 0));
            index = index > -1 ? index : -index - 1;
            if (index > -1 && index < wordsArray.length) {
                WordInfo info = wordsArray[index];
                status = "w:  " + info.toString() + "\n\n";
                LinkedList<WordInfo> listInfo = new LinkedList<WordInfo>();
                for (String suffix : SUFFIXES) {
                    String word = typedWord + suffix;
                    index = Arrays.binarySearch(wordsArray, new WordInfo(word, 0, 0));
                    if (index > 0 && index < wordsArray.length) {
                        listInfo.add(wordsArray[index]);
                    } else {
                        //status += "   --no-- " + word + "\n";
                    }
                }
                Collections.sort(listInfo, new WordInfo.FreqDescComparator());
                for (WordInfo wi : listInfo) {
                    status += "  " + wi.toString() + "\n";
                }
            }
        }
        return status;
    }

}

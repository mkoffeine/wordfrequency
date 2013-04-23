package com.koffeine.wordfreq;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class WordsModel {
    private Logger logger = Logger.getLogger(WordsModel.class.getSimpleName());
    private WordInfo[] wordsArray;


    public void initLogic() {
        try {
            readData();
        }
        catch (IOException e) {
            throw new IllegalStateException("WordsModel caught IOException");
        }
    }
    private void readData() throws IOException {

        File sdCard = Environment.getExternalStorageDirectory();
        String dictionaryFile = "en_full_raw_2.dic";
        File file = new File(sdCard, "Android/data/com.koffeine.wordfreq/" + dictionaryFile);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        int i = 0;
        ArrayList<WordInfo> words = new ArrayList<WordInfo>();
        while ((line = br.readLine()) != null) {
            i++;
            int separatorIndex1 = line.indexOf(" ");
            int separatorIndex2 = line.indexOf(" ", separatorIndex1 + 1);
            String word = line.substring(0, separatorIndex1);
            int freq = Integer.parseInt(line.substring(separatorIndex1 + 1, separatorIndex2));
            int lineNumber = Integer.parseInt(line.substring(separatorIndex2 + 1));
            words.add(new WordInfo(word, lineNumber, freq));
            if (i % 11000 == 0) {
                logger.debug("" + word + " " + i + "  # " + lineNumber + "  " + freq + "  ==== " + line);
            }
        }
        wordsArray = words.toArray(new WordInfo[]{});

        for (WordInfo wordInfo : wordsArray) {
            if (i % 11000 == 0)
                logger.debug(wordInfo.toString());
        }
    }
    public String getStatus(String typedWord) {
        String status = "";
        if (typedWord.length() > 1 && wordsArray != null && wordsArray.length > 0) {
            int index = Arrays.binarySearch(wordsArray, new WordInfo(typedWord, 0, 0));
            index = index > -1 ? index : -index - 1;
            if (index > -1 && index < wordsArray.length) {
                WordInfo info = wordsArray[index];
                status = "words:   " + info.toString() + "\n\n";
                String[] suffixes = {"s", "ing", "es", "ly", "er", "ar", "ir", "y", "able", "e",
                        "t", "ed", "en", "ist", "ful", "fy", "ian", "ize", "ible",
                        "ness", "less", "ism", "hood", "ment"
                        , "al", "ent", "nt", "sm", "m", "st", "an"
                        , "ss", "ng", "n", "ble", "le", "r", "l"
                        , "t", "ul", "sm", "n", "ood", "od", "d"};
                for (String suffix : suffixes) {
                    String word = typedWord + suffix;
                    index = Arrays.binarySearch(wordsArray, new WordInfo(word, 0, 0));
                    if (index > 0 && index < wordsArray.length) {
                        status += "   " + wordsArray[index].toString() + "\n";
                    } else {
                        //status += "   --no-- " + word + "\n";
                    }
                }
            }
        }
        return status;
    }

}

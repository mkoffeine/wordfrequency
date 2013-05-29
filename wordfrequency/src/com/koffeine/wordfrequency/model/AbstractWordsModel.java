package com.koffeine.wordfrequency.model;

import android.os.Environment;
import com.koffeine.wordfrequency.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Michael on 28.05.13.
 */
abstract class AbstractWordsModel implements IWordsModel {
    private Logger logger = Logger.getLogger(AbstractWordsModel.class.getSimpleName());

    ArrayList<WordInfo> readDataFromFile() throws IOException {
        File sdCard = Environment.getExternalStorageDirectory();
        String dictionaryFile = getDictionaryFileName();
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
        return words;
    }

    abstract String getDictionaryFileName();

}

package com.koffeine.wordfreq;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileSortAndAdjust {
    public static void main(String[] args) throws Exception{
        //income file:  X:\_Myhaylo\idea12\en_full-rawEd.dic    (213272)
        //outcome file:  assets\en_full-raw.dic
        //Structure: String word;  int position;  int freq;

        ArrayList<WordInfo> words = new ArrayList<WordInfo>(213272);
        readFileSortedByFreq(words);
        System.out.println("start sorting");
        Collections.sort(words);
        System.out.println("end sorting");
        writeInAssetsFile(words);
        System.out.println("written to tile");
    }

    private static void writeInAssetsFile(ArrayList<WordInfo> words) throws Exception {
        String file = "assets\\en_full_raw.dic";
//        FileOutputStream os = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (WordInfo wi : words) {
            bw.write(wi.getWord() + " " + wi.getFreq() + " " + wi.getPosition());
            bw.newLine();
        }
        bw.flush();
    }

    private static void readFileSortedByFreq(ArrayList<WordInfo> words) throws IOException {
        InputStream is = null;
        String dictionaryFile = "X:\\_Myhaylo\\idea12\\en_full-rawEd.dic";
        is = new FileInputStream(dictionaryFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {

            int separatorIndex = line.indexOf(" ");
            String word = line.substring(0, separatorIndex);
            int freq = Integer.parseInt(line.substring(separatorIndex + 1));
            if (isCorrectWord(word) && freq > 3) {
                i++;
                words.add(new WordInfo(word, i, freq));
                if (i % 18000 == 0) {
                    System.out.println("" + word + " " + i + "  " + freq + "  ==== " + line);
                }
            }
        }
    }
    public static boolean isCorrectWord(String s) {
        boolean correct = false;
        if (s.length() > 2/* &&
                s.charAt(0) >= 'a' && s.charAt(0) <= 'z' &&
                s.charAt(1) >= 'a' && s.charAt(1) <= 'z'*/) {
            correct = true;
        }
        if (correct) {
            Pattern pattern = Pattern.compile("[a-z]*");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                correct = matcher.group().equals(s);
            }
            else {
                correct = false;
            }
        }
        return correct;
    }
}

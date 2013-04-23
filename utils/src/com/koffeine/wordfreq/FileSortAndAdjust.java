package com.koffeine.wordfreq;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileSortAndAdjust {
    static int MIN_FREQ = 0;
    static int MAX_FREQ_WHEN_SPEC_SYMBOLS_IS_ALLOWED = 3;
    static int[] LIST_OF_FREQS = new int[]{700, 200, 120, 100, 80, 50, 30, 20, 10, 5, 4, 3, 2, 1, 0};

    public static void main(String[] args) throws Exception {
        //income file:  X:\_Myhaylo\idea12\en_full-rawEd.dic    (213272)
        //outcome file:  output\en_full-raw??.dic
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
        for (int minFreq : LIST_OF_FREQS) {
            String file = "output\\en_full_raw_" + minFreq + ".dic";
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (WordInfo wi : words) {
                if (wi.getFreq() > minFreq) {
                    boolean isCorrectWord = true;
                    if (minFreq > MAX_FREQ_WHEN_SPEC_SYMBOLS_IS_ALLOWED) {
                        isCorrectWord = isCorrectWord(wi.getWord(), "[a-z]*");
                    }
                    if (isCorrectWord) {
                        bw.write(wi.getWord() + " " + wi.getFreq() + " " + wi.getPosition());
                        bw.newLine();
                    }
                }
            }
            bw.flush();
        }
    }

    private static void readFileSortedByFreq(ArrayList<WordInfo> words) throws IOException {
        InputStream is;
        String dictionaryFile = "X:\\_Myhaylo\\idea12\\en_full-rawEd.dic";
        is = new FileInputStream(dictionaryFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {

            int separatorIndex = line.indexOf(" ");
            String word = line.substring(0, separatorIndex);
            int freq = Integer.parseInt(line.substring(separatorIndex + 1));
            if (isCorrectWord(word, "[a-z,\\'\\-]*") && freq > MIN_FREQ) {
                i++;
                words.add(new WordInfo(word, i, freq));
                if (i % 18000 == 0) {
                    System.out.println("" + word + " " + i + "  " + freq + "  ==== " + line);
                }
            }
        }
    }

    public static boolean isCorrectWord(String s, String filter) {
        boolean correct = false;
        if (s.length() > 1 &&
                s.charAt(0) >= 'a' && s.charAt(0) <= 'z' &&
                s.charAt(1) >= 'a' && s.charAt(1) <= 'z') {
            correct = true;
        }
        if (correct) {
            Pattern pattern = Pattern.compile(filter);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                correct = matcher.group().equals(s);
//                if (!correct)
//                    System.err.println(ii++ +"   " +s);
            } else {
                correct = false;
            }
        }
        return correct;
    }
//    static int ii=0;
}

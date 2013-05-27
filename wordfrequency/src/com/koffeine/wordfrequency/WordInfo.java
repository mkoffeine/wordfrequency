package com.koffeine.wordfrequency;

public class WordInfo implements Comparable<WordInfo> {
    private String word;
    private int position;
    private int freq;

    public WordInfo(String word, int position, int freq) {
        if (word == null) {
            throw new IllegalArgumentException("work can't be null in WordInfo.");
        }
        this.word = word;
        this.position = position;
        this.freq = freq;
    }

    public String getWord() {
        return word;
    }

    public int getPosition() {
        return position;
    }

    public int getFreq() {
        return freq;
    }

    public int compareTo(WordInfo wordInfo) {
        return word.compareTo(wordInfo.getWord());
    }

    @Override
    public String toString() {
        return word + "     pos: " + position + "    freq: " + freq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordInfo wordInfo = (WordInfo) o;

        if (!word.equals(wordInfo.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }
}
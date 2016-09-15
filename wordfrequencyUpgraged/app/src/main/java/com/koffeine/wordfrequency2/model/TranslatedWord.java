package com.koffeine.wordfrequency2.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class TranslatedWord extends RealmObject {
    @PrimaryKey
    private String word;
    private String translation;
    private Date addingDate;

    public TranslatedWord() {}

    public TranslatedWord(String word, String translation, Date addingDate) {
        this.word = word;
        this.translation = translation;
        this.addingDate = addingDate;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Date getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
    }
}

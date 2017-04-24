package com.example.schalbyshev.yandex.core;

/**
 * Created by schalbyshev on 12.04.2017.
 */

public class HistoryItem {

    private int id;
    private String text;
    private String translate;
    private String sourceLang;
    private String targetLang;
    private int favorites;

    public HistoryItem(){
    }

    public HistoryItem(String text, String translate, String sourceLang, String targetLang, int favorites) {
        this.text = text;
        this.translate = translate;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.favorites = favorites;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public int getFavorites() {
        return favorites;
    }
}

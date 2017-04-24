package com.example.schalbyshev.yandex.core;

/**
 * Created by schalbyshev on 16.04.2017.
 */

public class Lang implements Comparable<Lang>{

    private String key;
    private String value;

    public Lang(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int compareTo(Lang otherLang) {
        return this.getValue().compareTo(otherLang.getValue());
    }
}

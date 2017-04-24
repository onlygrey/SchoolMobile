package com.example.schalbyshev.yandex.core;

/**
 * Created by schalbyshev on 21.04.2017.
 */

public class Validate {
    public static boolean validateAsyncTaskLoaderText(String string){
        CharSequence cs="Error";
        if (string.contains("Error")) return false;
        else return true;
    }
}

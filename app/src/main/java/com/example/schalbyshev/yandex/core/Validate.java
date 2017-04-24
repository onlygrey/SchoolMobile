package com.example.schalbyshev.yandex.core;

/**
 * Created by schalbyshev on 21.04.2017.
 */

public class Validate {     //проверяем есть ли в строке сообщение об ошибке
    public static boolean validateAsyncTaskLoaderText(String string){
        if (string.contains("Error ")) return false;
        else return true;
    }
}

package com.example.schalbyshev.yandex.core;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

//import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by schalbyshev on 03.04.2017.
 * trnsl.1.1.20170406T182029Z.b5198dbe36626fed.355cf8fbc66bd157e808942cf160746f55f9ad0e
 */

public class AsyncTaskLoaderText extends AsyncTaskLoader<String> {

    public final String TAG = this.getClass().getSimpleName()+" !TAG! ";
    public final String KEY_API = "key=trnsl.1.1.20170406T182029Z.b5198dbe36626fed.355cf8fbc66bd157e808942cf160746f55f9ad0e";
    public final String URL_TRANSLATE="https://translate.yandex.net/api/v1.5/tr/translate?";
    public final String URL_LANGS="https://translate.yandex.net/api/v1.5/tr/getLangs?";
    private String translateURL;
    private String simpleText;
    private String fullText;
    private String lang;
    private int n=0;

    public AsyncTaskLoaderText(Context context, Bundle bundle){
        super(context);
        simpleText=bundle.getString("text");
        lang=bundle.getString("lang");
        if (simpleText==null){
            translateURL=URL_LANGS+bundle.getString("ui")+KEY_API;
            fullText="";
        }else {
            translateURL=URL_TRANSLATE+KEY_API;
            try {
                fullText="text="+ URLEncoder.encode(simpleText, "UTF-8")+"&lang="+lang;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Constructor "+fullText);
    }
    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground");
        //n++;
        String textTranslate;
        try {
            textTranslate=getXML(translateURL,fullText);
            if (simpleText!=null){
                textTranslate=textTranslate+"<SourceText text=\""+simpleText+"\"></SourceText>";
            }
            //textTranslate=translateXMLParser.getTextTranslate();
        }catch (Exception e){
            textTranslate="Error "+e.getMessage();
        }

        return textTranslate;
    }

    private String getXML(String path, String stringTranslate) throws IOException {
        BufferedReader reader=null;
        //String lang="en-ru";
        //String input="home";
        try {
            URL url=new URL(path);
            //HttpsURLConnection httpsURLConnection=(HttpsURLConnection)url.openConnection();

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //if (httpURLConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                httpsURLConnection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                httpsURLConnection.setHostnameVerifier(new AllowAllHostnameVerifier());
            //}

            httpsURLConnection.setRequestMethod("POST");
            //httpsURLConnection.setReadTimeout(10000);
            httpsURLConnection.setDoOutput(true);

            Log.d(TAG, "getXML "+stringTranslate);

            DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            dataOutputStream.writeBytes(stringTranslate);

            //InputStream response = httpsURLConnection.getInputStream();
            httpsURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            StringBuilder buf=new StringBuilder();
            String line=null;
            while ((line=reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}

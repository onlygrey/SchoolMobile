package com.example.schalbyshev.yandex.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.schalbyshev.yandex.R;
import com.example.schalbyshev.yandex.core.Lang;
import com.example.schalbyshev.yandex.core.LangXMLParser;
import com.example.schalbyshev.yandex.ui.LangRecyclerViewAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by schalbyshev on 15.04.2017.
 */

public class SelectLangActivity extends AppCompatActivity {

    private Lang localLang;
    private Lang currentLang;
    public final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";

    private RecyclerView rvLang;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_lang_activity);
        Log.d(LOG_TAG, "onCreate");

        Intent intent = getIntent();
        localLang=new Lang(intent.getStringExtra("localLangKey"),intent.getStringExtra("localLangValue"));
        currentLang=new Lang(intent.getStringExtra("currentLangKey"),intent.getStringExtra("currentLangValue"));
        Log.d(LOG_TAG, "localLang key = "+localLang.getKey()+" value = "+localLang.getValue());
        Log.d(LOG_TAG, "currentLang key = "+currentLang.getKey()+" value = "+currentLang.getValue());

        ArrayList<Lang> langArrayList=getLang();
        Collections.sort(langArrayList);

        if (langArrayList!=null){
            Log.d(LOG_TAG, "langArrayList != null");
            initRecyclerViewAdapter(langArrayList);
            /*for (Lang l: langArrayList ) {
                Log.d(LOG_TAG, "lang key = "+l.getKey()+" value = "+l.getValue());

            }*/
        }
        else {
            //textView = new TextView(this);
            //textView.setText(R.string.voidHistory);
            //llTab1Favorites.addView(textView);
        }
    }

    private ArrayList<Lang> getLang(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    openFileInput("lang_"+localLang.getKey()+".xml")));
            String bufferXML="" ;
            String stringXML="" ;
            // читаем содержимое
            while ((bufferXML = bufferedReader.readLine()) != null) {
                stringXML+=bufferXML;
                //Log.d(LOG_TAG, stringXML);
            }
            LangXMLParser langXMLParser=new LangXMLParser(stringXML);
            return langXMLParser.getAllLangs();
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "bufferedReader exception "+e.getMessage());
            //bundle.putString("ui","ui="+localLang.getKey()+"&");
            //loadLang();
            //e.printStackTrace();
            return null;
        }catch (IOException ioException){
            Log.d(LOG_TAG, "bufferedReader exception "+ioException.getMessage());
            return null;
        }
    }

    private void initRecyclerViewAdapter(ArrayList<Lang> langArrayList) {
        rvLang=(RecyclerView) findViewById(R.id.rvLang);
        LangRecyclerViewAdapter langRecyclerViewAdapter= new LangRecyclerViewAdapter(this,langArrayList,currentLang);
        rvLang.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvLang.setAdapter(langRecyclerViewAdapter);
    }

}

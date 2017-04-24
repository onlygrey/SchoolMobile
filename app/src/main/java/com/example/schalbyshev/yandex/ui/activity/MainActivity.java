package com.example.schalbyshev.yandex.ui.activity;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.schalbyshev.yandex.R;
import com.example.schalbyshev.yandex.core.AsyncTaskLoaderHistory;
import com.example.schalbyshev.yandex.core.AsyncTaskLoaderText;
import com.example.schalbyshev.yandex.core.HistoryItem;
import com.example.schalbyshev.yandex.core.Lang;
import com.example.schalbyshev.yandex.core.TranslateXMLParser;
import com.example.schalbyshev.yandex.core.Validate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements
        LoaderCallbacks<String>{
                // Инициализация переменных
    private TextView tvTranslate;
    private TextView tvSourceLang;
    private TextView tvTargetLang;
    private EditText editText;
    private ImageButton ibClear,ibMakeFavorite;
    private Lang localLang;
    private Lang sourceLang;
    private Lang targetLang;
    private boolean isFavorites;
    private TranslateXMLParser translateXMLParser;

    private final int LOADER_TRANSLATE_ID = 1;
    private final int LOADER_HISTORY_ID = 2;
    private final int LOADER_LANG_ID = 3;
    private final int REQUEST_CODE_SL = 1;
    private final int REQUEST_CODE_TL = 2;
    public final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";

    //private
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        isFavorites = false;
        //getActionBar().hide();
        editText = (EditText) this.findViewById(R.id.editText);
        tvTranslate = (TextView) this.findViewById(R.id.textView);
        tvSourceLang = (TextView) this.findViewById(R.id.tvSourceLang);
        tvTargetLang = (TextView) this.findViewById(R.id.tvTargetLang);
        ibClear = (ImageButton) this.findViewById(R.id.ibClear);
        ibMakeFavorite = (ImageButton) this.findViewById(R.id.ibMakeFavorite);
        bundle = new Bundle();
        getLoaderManager().initLoader(LOADER_TRANSLATE_ID, bundle, this);
        getLoaderManager().initLoader(LOADER_HISTORY_ID, bundle, this);
        getLoaderManager().initLoader(LOADER_LANG_ID, bundle, this);

        initLang(); //инициация языков

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(LOG_TAG, "addTextChangedListener s = "+s+" start = "+start+" count = "+count+ " after = "+after);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(LOG_TAG, "onTextChanged s = "+s+" start = "+start+" count = "+count+ " before = "+before);
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d(LOG_TAG, "addTextChangedListener s = "+s);
                switch ( s.length()){
                    case 0:
                        ibClear.setVisibility(View.INVISIBLE);
                        ibMakeFavorite.setVisibility(View.INVISIBLE);
                        tvTranslate.setText("");
                        break;
                    case 1:
                        ibClear.setVisibility(View.VISIBLE);
                        if (ibMakeFavorite.getVisibility()==View.VISIBLE) ibMakeFavorite.setVisibility(View.INVISIBLE);
                        tvTranslate.setText("");
                        break;
                    default:
                        if (ibMakeFavorite.getVisibility()==View.INVISIBLE) ibMakeFavorite.setVisibility(View.VISIBLE);
                        loadTranslate(s);
                }
            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", editText.getText().toString());
        outState.putString("translate", tvTranslate.getText().toString());
        outState.putBoolean("favorites", isFavorites);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString("text"));
        tvTranslate.setText(savedInstanceState.getString("translate"));
        isFavorites=savedInstanceState.getBoolean("favorites");
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    private void initLang() {
        Log.d(LOG_TAG, "initLang ");

        switch (getResources().getConfiguration().locale.getLanguage()){
            case "ru":
                localLang=new Lang("ru","Русский");
                sourceLang=new Lang("ru","Русский");
                targetLang=new Lang("en","Английский");
                break;
            default:
                localLang=new Lang("en","English");
                sourceLang=new Lang("en","English");
                targetLang=new Lang("ru","Russian");
                break;

        }
        tvSourceLang = (TextView) this.findViewById(R.id.tvSourceLang);     //
        tvTargetLang = (TextView) this.findViewById(R.id.tvTargetLang);
        tvSourceLang.setText(sourceLang.getValue());
        tvTargetLang.setText(targetLang.getValue());
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    openFileInput("lang_"+localLang.getKey()+".xml")));
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "bufferedReader exception "+e);
            //bundle.putString("ui","ui="+localLang.getKey()+"&");
            loadLang();
            //e.printStackTrace();
        }
        //File langXML=new File("lang_"+localLang.getKey()+".xml");

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        // Создаем новый Loader с нужными параметрами
        Loader<String> loader = null;
        switch (id){
            case LOADER_TRANSLATE_ID:
                loader = new AsyncTaskLoaderText(this, args);
                break;
            case LOADER_HISTORY_ID:
                loader = new AsyncTaskLoaderHistory(this,args);
                break;
            case LOADER_LANG_ID:
                loader = new AsyncTaskLoaderText(this,args);
                break;
        }
        //String ask=args.getString("text");
        Log.d(LOG_TAG, "onCreateLoader "+id);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {    //выполнение
        Log.d(LOG_TAG, "onLoadFinished "+data);
        //ibMakeFavorite.setImageResource(R.drawable.btn_star_big_off);
        switch (loader.getId()){
            case LOADER_TRANSLATE_ID:
                if (Validate.validateAsyncTaskLoaderText(data)) //проверяем что нет ошибок сети в ответе от Loader
                {
                    translateXMLParser=new TranslateXMLParser(data); //Содаём парсер XML для перевода используя полученный xml документ от лоадера
                    String textTranslate;
                    textTranslate=translateXMLParser.getTextTranslate();    //получаем текст перевода из XML

                    if (translateXMLParser.getTextSource().equals(editText.getText().toString())){
                        tvTranslate.setText(textTranslate);
                        HistoryItem historyItem=new HistoryItem(editText.getText().toString().trim(),textTranslate.trim(),sourceLang.getKey(),
                                targetLang.getKey(),isFavorites?1:0);
                        loadInsertDB(historyItem);
                    }

                }
                else {  // если ошибки есть - сообщаем об этом пользователь
                    tvTranslate.setText(R.string.errorNet);         //выводим сообщение об ошибке
                    ibMakeFavorite.setVisibility(View.INVISIBLE);   //скрываем кнопку добавления перевода в избранное
                }


                break;
            case LOADER_HISTORY_ID:
                //ibMakeFavorite.setImageResource(R.drawable.star_gold);
                break;
            case LOADER_LANG_ID:
                //File langXML=new File("lang_"+localLang.getKey()+".xml");
                Log.d(LOG_TAG, "onLoadFinished LOADER_LANG_ID");

                try {
                    // отрываем поток для записи
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput("lang_"+localLang.getKey()+".xml", MODE_PRIVATE)));
                    bufferedWriter.write(data);         // пишем данные
                    bufferedWriter.close();             // закрываем поток
                    Log.d(LOG_TAG, "Write to "+"lang_"+localLang.getKey()+".xml");
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Error create file "+"lang_"+localLang.getKey()+".xml");
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader)     {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.ibClear:  //нажали на кнопку очистки поля ввода
                ibClear.setVisibility(View.INVISIBLE);  //скрываем кнопку очистки поля ввода
                ibMakeFavorite.setVisibility(View.INVISIBLE);   //скрываем кнопку добавления перевода в избранное
                tvTranslate.setText("");       //очищаем поле ввода
                editText.setText("");       //
                break;
            case R.id.ibFavorites:  //нажали на кнопку перехода к списку История/Избранное
                //isFavorites=!isFavorites;
                intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);      //запускаем активность История/Избранное
                break;
            case R.id.ibChange:     //нажали на кнопку смена языка
                Lang tempLang=sourceLang;   //меняем языки местами
                sourceLang=targetLang;      //
                targetLang=tempLang;        //
                tvSourceLang.setText(sourceLang.getValue());    //меняем текст языка на активности
                tvTargetLang.setText(targetLang.getValue());    //
                break;
            case R.id.ibMakeFavorite:

                break;
            case R.id.tvSourceLang:
                intent = new Intent(this, SelectLangActivity.class);
                intent.putExtra("localLangKey", localLang.getKey());
                intent.putExtra("localLangValue", localLang.getValue());
                intent.putExtra("currentLangKey", sourceLang.getKey());
                intent.putExtra("currentLangValue", sourceLang.getValue());
                startActivityForResult(intent, REQUEST_CODE_SL);
                break;
            case R.id.tvTargetLang:
                intent = new Intent(this, SelectLangActivity.class);
                intent.putExtra("localLangKey", localLang.getKey());
                intent.putExtra("localLangValue", localLang.getValue());
                intent.putExtra("currentLangKey", targetLang.getKey());
                intent.putExtra("currentLangValue", targetLang.getValue());
                startActivityForResult(intent, REQUEST_CODE_TL);
                break;
            case R.id.ibSettings:
                break;
        }
    }

    public void loadTranslate(CharSequence charSequence) {
        Log.d(LOG_TAG, "loadTranslate "+charSequence);
        Bundle bundleTmp= new Bundle();
        try {
            bundleTmp.putString("text",charSequence.toString());
            bundleTmp.putString("lang",sourceLang.getKey()+"-"+targetLang.getKey());
        }catch (Exception exec){
            Log.d(LOG_TAG, "loadTranslate "+exec);
        }
        //ibMakeFavorite.setImageResource(R.drawable.load);
        Loader<String> loader =getLoaderManager().restartLoader(LOADER_TRANSLATE_ID, bundleTmp, this);
        loader.forceLoad();
    }
    public void loadLang() {
        Log.d(LOG_TAG, "loadLang");
        Bundle bundleTmp= new Bundle();
        bundleTmp.putString("ui","ui="+localLang.getKey()+"&");
        Loader<String> loader =getLoaderManager().restartLoader(LOADER_LANG_ID, bundleTmp, this);
        loader.forceLoad();
    }

    public void loadInsertDB(HistoryItem historyItem) {
        Log.d(LOG_TAG, "loadInsertDB historyItem.getFavorites() = "+historyItem.getFavorites());
        Bundle bundleTmp= new Bundle();
        bundleTmp.putString("text",historyItem.getText());
        bundleTmp.putString("translate",historyItem.getTranslate());
        bundleTmp.putString("source_lang",historyItem.getSourceLang());
        bundleTmp.putString("target_lang",historyItem.getTargetLang());
        bundleTmp.putInt("favorites",historyItem.getFavorites());

        Loader<String> loader =getLoaderManager().restartLoader(LOADER_HISTORY_ID, bundleTmp, this);
        loader.forceLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode){
            case REQUEST_CODE_SL:
                sourceLang.setKey(data.getStringExtra("key"));
                sourceLang.setValue(data.getStringExtra("value"));
                tvSourceLang.setText(sourceLang.getValue());    //меняем текст языка на активности

                break;
            case REQUEST_CODE_TL:
                targetLang.setKey(data.getStringExtra("key"));
                targetLang.setValue(data.getStringExtra("value"));
                tvTargetLang.setText(targetLang.getValue());    //меняем текст языка на активности
                break;
        }
    }

}

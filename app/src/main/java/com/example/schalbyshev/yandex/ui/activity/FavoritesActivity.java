package com.example.schalbyshev.yandex.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.schalbyshev.yandex.R;
import com.example.schalbyshev.yandex.core.HistoryItem;
import com.example.schalbyshev.yandex.db.DatabaseHelper;
import com.example.schalbyshev.yandex.ui.HistoryRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by schalbyshev on 10.04.2017.
 */

public class FavoritesActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";

    private DatabaseHelper db;
    private RecyclerView rvListFiles;
    private TextView textView;
    private ImageButton ibDelete;
    private LinearLayout llTabHistory;
    private LinearLayout llTabFavorites;
    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);
        llTabHistory=(LinearLayout) findViewById(R.id.tab1);
        llTabFavorites=(LinearLayout) findViewById(R.id.tab2);
        ibDelete=(ImageButton) findViewById(R.id.ibDelete);
        db=new DatabaseHelper(this);

        initTabRecyclerView();
        tabHostInitiate();

    }

    private void initRecyclerViewAdapter(ArrayList<HistoryItem> historyItemArrayList,int rvID) {
        rvListFiles=(RecyclerView) findViewById(rvID);
        historyRecyclerViewAdapter= new HistoryRecyclerViewAdapter(this,historyItemArrayList);
        rvListFiles.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvListFiles.setAdapter(historyRecyclerViewAdapter);
    }

    public void onClick(View view) {
        //Intent intent=null;
        switch (view.getId()) {
            case R.id.ibDelete:  //нажали на кнопку очистки поля ввода
                ibDelete.setVisibility(View.INVISIBLE);  //скрываем кнопку очистки поля ввода
                db.deleteHistory();
                if (historyRecyclerViewAdapter!=null) historyRecyclerViewAdapter.clear();
                initTabRecyclerView();
                break;
            case R.id.ibTranslate:
                onBackPressed();
                break;
        }
    }

    private void tabHostClear(){

    }
    private void initTabRecyclerView(){
        ArrayList<HistoryItem> historyItemArrayList=db.getAllHistory();
        if ((historyItemArrayList!=null)&&(historyItemArrayList.size()>0)){
            Log.d(LOG_TAG, "historyItemArrayList size "+historyItemArrayList.size());
            initRecyclerViewAdapter(historyItemArrayList,R.id.rvHistory);
        }
        else {
            Log.d(LOG_TAG, "llTabHistory addView");
            ibDelete.setVisibility(View.INVISIBLE);
            llTabHistory.addView(createTVVoid(R.string.voidHistory));
        }
        ArrayList<HistoryItem> favoriteItemArrayList=db.getAllFavorites();
        if ((favoriteItemArrayList!=null)&&(favoriteItemArrayList.size()>0)){
            Log.d(LOG_TAG, "favoriteItemArrayList size "+favoriteItemArrayList.size());
            initRecyclerViewAdapter(favoriteItemArrayList,R.id.rvFavorites);
        }
        else {
            Log.d(LOG_TAG, "llTabFavorites addView");
            ibDelete.setVisibility(View.INVISIBLE);
            llTabFavorites.addView(createTVVoid(R.string.voidFavorites));
        }

    }
    private void tabHostInitiate(){


        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tabSpec = tabHost.newTabSpec("tab1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator(getString(R.string.tabHistory));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator(getString(R.string.tabFavorites));
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tabSpec.equals(tabId)) {
                    //destroy earth
                }
                if(tabSpec.equals(tabId)) {
                    //destroy mars
                }
            }});
    }

    private TextView createTVVoid(int sringID){
        textView = new TextView(this);
        textView.setPadding(0,64,0,0);
        textView.setGravity(Gravity.CENTER);
        textView.setText(sringID);
        //llTabFavorites.removeAllViews();
        //llTabFavorites.addView(textView);
        return textView;
    }
}

package com.example.schalbyshev.yandex.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FavoritesActivity extends AppCompatActivity{

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
    private HistoryRecyclerViewAdapter favoriteRecyclerViewAdapter;

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

    public void reDraw(){   //перерисовываем вкладки
        historyRecyclerViewAdapter.clear();
        favoriteRecyclerViewAdapter.clear();
        initTabRecyclerView();
    }

    private HistoryRecyclerViewAdapter initRecyclerViewAdapter(ArrayList<HistoryItem> historyItemArrayList,int rvID) {
        rvListFiles=(RecyclerView) findViewById(rvID);
        HistoryRecyclerViewAdapter historyRecyclerViewAdapter= new HistoryRecyclerViewAdapter(this,historyItemArrayList);
        rvListFiles.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvListFiles.setAdapter(historyRecyclerViewAdapter);
        return historyRecyclerViewAdapter;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibDelete:  //нажали на кнопку очистки поля ввода
                ibDelete.setVisibility(View.INVISIBLE);  //скрываем кнопку очистки поля ввода
                db.deleteHistory();
                if ((historyRecyclerViewAdapter!=null )&&(favoriteRecyclerViewAdapter!=null))
                    reDraw();
                break;
            case R.id.ibTranslate:
                onBackPressed();
                break;
        }
    }

    private void initTabRecyclerView(){
        ArrayList<HistoryItem> historyItemArrayList=db.getAllHistory();
        if ((historyItemArrayList!=null)&&(historyItemArrayList.size()>0)){ //если нет записей истории то пусто
            //Log.d(LOG_TAG, "historyItemArrayList size "+historyItemArrayList.size());
            ibDelete.setVisibility(View.VISIBLE);
            historyRecyclerViewAdapter=initRecyclerViewAdapter(historyItemArrayList,R.id.rvHistory);
        }
        else {
            //Log.d(LOG_TAG, "llTabHistory addView");
            ibDelete.setVisibility(View.INVISIBLE);
        }
        ArrayList<HistoryItem> favoriteItemArrayList=db.getAllFavorites();
        if ((favoriteItemArrayList!=null)&&(favoriteItemArrayList.size()>0)){   //если нет записей любимого то пусто
            //Log.d(LOG_TAG, "favoriteItemArrayList size "+favoriteItemArrayList.size());
            ibDelete.setVisibility(View.VISIBLE);
            favoriteRecyclerViewAdapter=initRecyclerViewAdapter(favoriteItemArrayList,R.id.rvFavorites);
        }
        else {
            //Log.d(LOG_TAG, "llTabFavorites addView");
            ibDelete.setVisibility(View.INVISIBLE);
        }

    }
    private void tabHostInitiate(){     //инициация вкладок

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
    }
}

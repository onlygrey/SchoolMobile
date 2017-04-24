package com.example.schalbyshev.yandex.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schalbyshev.yandex.R;
import com.example.schalbyshev.yandex.core.AsyncTaskLoaderHistory;
import com.example.schalbyshev.yandex.core.HistoryItem;
import com.example.schalbyshev.yandex.ui.activity.FavoritesActivity;

import java.util.ArrayList;

/**
 * Created by schalbyshev on 13.04.2017.
 */

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ItemHolder>  implements
        LoaderManager.LoaderCallbacks<String>  {
    private final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";
    private Context context;
    private ArrayList<HistoryItem> historyItemArrayList= new ArrayList<>();
    private final int LOADER_HISTORY_ID = 2;

    public HistoryRecyclerViewAdapter(Context context,ArrayList<HistoryItem> historyItemArrayList){
        this.context=context;
        this.historyItemArrayList=historyItemArrayList;
        //Log.d(LOG_TAG, "Constructor");
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_item_list,parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        //Log.d(LOG_TAG, "onBindViewHolder "+historyItemArrayList.get(position).getText());
        //стандартно привязываем ресурсы к view элементам
        holder.tvText.setText(historyItemArrayList.get(position).getText());
        holder.tvTranslate.setText(historyItemArrayList.get(position).getTranslate());
        holder.tvSourceLang.setText(historyItemArrayList.get(position).getSourceLang()+" - ");
        holder.tvTargetLang.setText(historyItemArrayList.get(position).getTargetLang());
        if(historyItemArrayList.get(position).getFavorites()==1){
            holder.ivFavorites.setImageResource(R.drawable.btn_star_big_on);
        }
        else {
            holder.ivFavorites.setImageResource(R.drawable.btn_star_big_off);
        }
        holder.ivFavorites.setTag(position); //вешаем тэг с номером позиции в архиве для обработчика нажатий
    }
    @Override
    public int getItemCount() {
        return historyItemArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvText;
        TextView tvTranslate;
        TextView tvSourceLang;
        TextView tvTargetLang;
        ImageView ivFavorites;

        public ItemHolder(View itemView) {
            super(itemView);
            //инициализация view элементов
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            tvTranslate = (TextView) itemView.findViewById(R.id.tvTranslate);
            tvSourceLang = (TextView) itemView.findViewById(R.id.tvSourceLang);
            tvTargetLang = (TextView) itemView.findViewById(R.id.tvTargetLang);
            ivFavorites = (ImageView) itemView.findViewById(R.id.ivFavorites);
            ivFavorites.setOnClickListener(this);   //любимые - обрабатывают нажатия
        }

        @Override
        public void onClick(View v) {
            int position=Integer.parseInt(v.getTag().toString());   //берем из тэга позицию
            HistoryItem historyItem=historyItemArrayList.get(position);//бер    м нужную запись истории из массива
            int favorites =(historyItem.getFavorites()==1)?0:1; //меняем признак влюблённости на противоположный
            historyItem.setFavorites(favorites);    //задаём новый признак записи
            ((ImageView) v).setImageResource(favorites==0?R.drawable.btn_star_big_off:R.drawable.btn_star_big_on);  //присваиваем новую картинку для view
            loadUpdateDB(historyItem);  //обновляем запись в БД
        }
    }

    public void clear() {      //очищаем историю
        int size = this.historyItemArrayList.size();
        this.historyItemArrayList.clear();
        notifyItemRangeRemoved(0, size);
    }
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = null;
        loader = new AsyncTaskLoaderHistory(context,args);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        ((FavoritesActivity) context).reDraw();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    public void loadUpdateDB(HistoryItem historyItem) {
        //Log.d(LOG_TAG, "loadUpdateDB historyItem.getFavorites() = "+historyItem.getFavorites());
        Bundle bundleTmp= new Bundle();
        bundleTmp.putString("text",historyItem.getText());
        bundleTmp.putString("translate",historyItem.getTranslate());
        bundleTmp.putString("source_lang",historyItem.getSourceLang());
        bundleTmp.putString("target_lang",historyItem.getTargetLang());
        bundleTmp.putInt("favorites",historyItem.getFavorites());
        bundleTmp.putString("update","update");

        Loader<String> loader =((Activity)context).getLoaderManager().restartLoader(LOADER_HISTORY_ID, bundleTmp, this);
        loader.forceLoad();
    }
}

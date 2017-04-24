package com.example.schalbyshev.yandex.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schalbyshev.yandex.R;
import com.example.schalbyshev.yandex.core.HistoryItem;

import java.util.ArrayList;

/**
 * Created by schalbyshev on 13.04.2017.
 */

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ItemHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";
    Context context;
    int countItemList=0;
    ArrayList<HistoryItem> historyItemArrayList= new ArrayList<>();
    private final int REQUEST_EDIT_TODO=2;

    public HistoryRecyclerViewAdapter(Context context,ArrayList<HistoryItem> historyItemArrayList){
        this.context=context;
        this.historyItemArrayList=historyItemArrayList;
        Log.d(LOG_TAG, "Constructor");
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_item_list,parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder "+historyItemArrayList.get(position).getText());

        holder.tvText.setText(historyItemArrayList.get(position).getText());
        holder.tvTranslate.setText(historyItemArrayList.get(position).getTranslate());
        holder.tvSourceLang.setText(historyItemArrayList.get(position).getSourceLang()+" - ");
        holder.tvTargetLang.setText(historyItemArrayList.get(position).getTargetLang());
        //holder.tvTranslate.setTag(historyItemArrayList.get(position));
        if(historyItemArrayList.get(position).getFavorites()==1){
            holder.ivFavorites.setImageResource(R.drawable.star_gold);
                    //setText(historyItemArrayList.get(position).getTranslate().substring(0,20)+"...");
        }
        else {
            holder.ivFavorites.setImageResource(R.drawable.star_black);
            //holder.textViewDescription.setText(historyItemArrayList.get(position).getTranslate());
        }
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

            tvText = (TextView) itemView.findViewById(R.id.tvText);
            tvTranslate = (TextView) itemView.findViewById(R.id.tvTranslate);
            tvSourceLang = (TextView) itemView.findViewById(R.id.tvSourceLang);
            tvTargetLang = (TextView) itemView.findViewById(R.id.tvTargetLang);
            ivFavorites = (ImageView) itemView.findViewById(R.id.ivFavorites);

            tvText.setOnClickListener(this);

            /*Animation animation= AnimationUtils.loadAnimation(context,R.anim.anim_transform);
            imageViewIcon.startAnimation(animation);
            animation= AnimationUtils.loadAnimation(context,R.anim.anim_transform_1);
            textViewName.startAnimation(animation);
            textViewDescription.startAnimation(animation);
            textViewDateEnd.startAnimation(animation);*/
        }
        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(context , ActivityEditToDo.class);
            intent.putExtra("id",((HistoryItem) v.getTag()).getId());
            intent.putExtra("text",((HistoryItem) v.getTag()).getText());
            intent.putExtra("translate",((HistoryItem) v.getTag()).getTranslate());
            intent.putExtra("source_lang",((HistoryItem) v.getTag()).getSourceLang());
            intent.putExtra("target_lang",((HistoryItem) v.getTag()).getTargetLang());
            ((Activity)context).startActivityForResult(intent,REQUEST_EDIT_TODO);*/
        }
    }

    public void clear() {
        int size = this.historyItemArrayList.size();
        this.historyItemArrayList.clear();
        notifyItemRangeRemoved(0, size);
    }
}

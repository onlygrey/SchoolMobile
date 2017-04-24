package com.example.schalbyshev.yandex.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schalbyshev.yandex.R;
import com.example.schalbyshev.yandex.core.Lang;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by schalbyshev on 13.04.2017.
 */

public class LangRecyclerViewAdapter extends RecyclerView.Adapter<LangRecyclerViewAdapter.ItemHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";
    private View view;
    private Lang currentLang;
    private Context context;
    private ArrayList<Lang> langItemArrayList= new ArrayList<>();

    public LangRecyclerViewAdapter(Context context, ArrayList<Lang> langItemArrayList,Lang currentLang){
        //Log.d(LOG_TAG, "Constructor");
        this.context=context;
        this.currentLang=currentLang;
        this.langItemArrayList=langItemArrayList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(LOG_TAG, "onCreateViewHolder");
         view= LayoutInflater.from(context).inflate(R.layout.lang_item_list,parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.tvLang.setText(langItemArrayList.get(position).getValue());
        holder.tvLang.setTag(langItemArrayList.get(position).getKey());
        if (langItemArrayList.get(position).getKey().equals(currentLang.getKey())){
            //Log.d(LOG_TAG, "onBindViewHolder "+langItemArrayList.get(position).getValue()+" "+langItemArrayList.get(position).getKey());
            holder.ivCheck.setVisibility(View.VISIBLE);
        }else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return langItemArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvLang;
        ImageView ivCheck;

        public ItemHolder(View itemView) {
            super(itemView);
            //Log.d(LOG_TAG, "ItemHolder Constructor");
            tvLang = (TextView) itemView.findViewById(R.id.tvText);
            ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
            tvLang.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent= new Intent();
            String stringValue=((TextView) v).getText().toString();
            if (stringValue.length()>18) stringValue=stringValue.substring(0,14)+"...";
            intent.putExtra("value",stringValue);
            intent.putExtra("key",v.getTag().toString());
            ((Activity)context).setResult(RESULT_OK,intent);
            //Log.d(LOG_TAG,"onClick "+"value = "+intent.getStringExtra("value")+" key = "+intent.getStringExtra("key"));
            ((Activity)context).finish();
        }
    }
}

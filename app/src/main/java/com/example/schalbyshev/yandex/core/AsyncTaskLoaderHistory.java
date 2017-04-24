package com.example.schalbyshev.yandex.core;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.example.schalbyshev.yandex.db.DatabaseHelper;

/**
 * Created by schalbyshev on 15.04.2017.
 */

public class AsyncTaskLoaderHistory extends AsyncTaskLoader {

    private final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";
    private Bundle  bundle;
    private DatabaseHelper db;
    private boolean update;     //признак что нужно обновить запись

    public AsyncTaskLoaderHistory(Context context, Bundle args) {
        super(context);
        bundle=args;
        db=new DatabaseHelper(context);
        update=(args.getString("update")!=null)?true:false;
        //Log.d(LOG_TAG, "Constructor "+text);
    }

    @Override
    public Object loadInBackground() {
        //Log.d(LOG_TAG, "loadInBackground");
        if(!db.isHistory(bundle)) { //если в БД нет такой записи то вставляем
             db.insertHistory(bundle);
        }else if (update) db.updateHistory(bundle);     //если есть и нужно обновить - обновляем
        return null;
    }
}

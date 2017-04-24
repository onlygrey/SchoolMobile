package com.example.schalbyshev.yandex.core;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.schalbyshev.yandex.db.DatabaseHelper;

/**
 * Created by schalbyshev on 15.04.2017.
 */

public class AsyncTaskLoaderHistory extends AsyncTaskLoader {
    public final String TAG = this.getClass().getSimpleName()+" !TAG! ";
    private String text;
    private Bundle  bundle;
    private DatabaseHelper db;
    RecyclerView rvListFiles;

    public AsyncTaskLoaderHistory(Context context, Bundle args) {
        super(context);
        bundle=args;
        db=new DatabaseHelper(context);
        text=args.getString("text");
        Log.d(TAG, "Constructor "+text);
    }

    @Override
    public Object loadInBackground() {
        Log.d(TAG, "loadInBackground");
        //SystemClock.sleep(3000);

        if(!db.isHistory(bundle)) db.insertHistory(bundle);
        return null;
    }
}

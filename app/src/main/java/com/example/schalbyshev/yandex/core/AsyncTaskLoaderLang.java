package com.example.schalbyshev.yandex.core;

import android.content.Context;
import android.content.AsyncTaskLoader;

/**
 * Created by schalbyshev on 16.04.2017.
 */

public class AsyncTaskLoaderLang extends AsyncTaskLoader {
    public AsyncTaskLoaderLang(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {
        return null;
    }
}

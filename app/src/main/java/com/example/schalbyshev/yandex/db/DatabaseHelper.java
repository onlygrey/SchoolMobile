package com.example.schalbyshev.yandex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.example.schalbyshev.yandex.core.HistoryItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by schalbyshev on 12.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public final String TAG = this.getClass().getSimpleName()+" !TAG! ";
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE="DB_HISTORY";
    private static final String TABLE="HISTORY";

    private static final String KEY_ID="id";
    private static final String KEY_TEXT="text";
    private static final String KEY_TRANSLATE="translate";
    private static final String KEY_S_LANG="source_lang";
    private static final String KEY_T_LANG="target_lang";
    private static final String KEY_FAVORITES="favorites";
    private static final String KEY_DATE="fav_insert_date";


    public DatabaseHelper(Context context ) {
        super(context, DATABASE, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TEXT+" TEXT, "+KEY_TRANSLATE+" TEXT, "
                +KEY_S_LANG+" TEXT, "+KEY_T_LANG+" TEXT, "+KEY_FAVORITES+" INTEGER, "+KEY_DATE+" TIMESTAMP)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+ TABLE);
        onCreate(db);
    }

    public void insertHistory(Bundle bundle){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TEXT,bundle.getString(KEY_TEXT));
        contentValues.put(KEY_TRANSLATE,bundle.getString(KEY_TRANSLATE));
        contentValues.put(KEY_S_LANG,bundle.getString(KEY_S_LANG));
        contentValues.put(KEY_T_LANG,bundle.getString(KEY_T_LANG));
        contentValues.put(KEY_FAVORITES,bundle.getInt(KEY_FAVORITES));
        contentValues.put(KEY_DATE,(new Date()).getTime());
        SQLiteDatabase db= this.getWritableDatabase();
        db.insert(TABLE,null,contentValues);
    }

    public ArrayList<HistoryItem> getAllHistory(){
        ArrayList<HistoryItem> historyArrayList=new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TABLE;

        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor =db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do {
                HistoryItem historyItem=new HistoryItem();
                historyItem.setId(cursor.getInt(0));
                historyItem.setText(cursor.getString(1));
                historyItem.setTranslate(cursor.getString(2));
                historyItem.setSourceLang(cursor.getString(3));
                historyItem.setTargetLang(cursor.getString(4));
                historyItem.setFavorites(cursor.getInt(5));

                historyArrayList.add(historyItem);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return historyArrayList;
    }

    public boolean isHistory(Bundle bundle) {
        SQLiteDatabase db= this.getWritableDatabase();
        String selectQuery="SELECT id FROM "+TABLE+" " +
                "WHERE ("+KEY_TEXT+" = '"+bundle.getString(KEY_TEXT)+"' ) " +
                "AND ( "+KEY_TRANSLATE+" = '"+bundle.getString(KEY_TRANSLATE)+"' ) " +
                "AND ( "+KEY_S_LANG+" = '"+bundle.getString(KEY_S_LANG)+"' ) " +
                "AND ( "+KEY_T_LANG+" = '"+bundle.getString(KEY_T_LANG)+"' );";
        Cursor cursor =db.rawQuery(selectQuery,null);
        Log.d(TAG, "selectQuery "+selectQuery);

        if (cursor.moveToFirst()){
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    public void updateHistory(Bundle bundle){
        SQLiteDatabase db= this.getWritableDatabase();

        String updateQuery="UPDATE "+TABLE+" SET "+KEY_FAVORITES+" = "+bundle.getInt(KEY_FAVORITES)+
                " WHERE ( "+KEY_TEXT+" = '"+bundle.getString(KEY_TEXT)+"' ) " +
                "AND ( "+KEY_TRANSLATE+" = '"+bundle.getString(KEY_TRANSLATE)+"' ) " +
                "AND ( "+KEY_S_LANG+" = '"+bundle.getString(KEY_S_LANG)+"' ) " +
                "AND ( "+KEY_T_LANG+" = '"+bundle.getString(KEY_T_LANG)+"' );";
        db.rawQuery(updateQuery,null);
        Log.d(TAG, "updateHistory "+updateQuery);
    }

    public ArrayList<HistoryItem> getAllFavorites() {
        ArrayList<HistoryItem> allFavorites=new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TABLE+" WHERE "+KEY_FAVORITES+" = 1 ;";
        Log.d(TAG, "selectQuery "+selectQuery);

        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor =db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do {
                HistoryItem historyItem=new HistoryItem();
                historyItem.setId(cursor.getInt(0));
                historyItem.setText(cursor.getString(1));
                historyItem.setTranslate(cursor.getString(2));
                historyItem.setSourceLang(cursor.getString(3));
                historyItem.setTargetLang(cursor.getString(4));
                historyItem.setFavorites(cursor.getInt(5));
                allFavorites.add(historyItem);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return allFavorites;
    }

    public void deleteHistory() {
        String deleteQuery = "DELETE FROM "+TABLE+";";
        Log.d(TAG, "deleteQuery "+deleteQuery);
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(deleteQuery);
    }
}

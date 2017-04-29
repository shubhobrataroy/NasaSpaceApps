package com.shubhobrata.roy.nasaspaceapps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shubh on 4/23/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table coordinates " +
                        "(lat text , long text )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void Insert(double lat , double lon)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lat", lat+"");
        contentValues.put("long", lon+"");
        db.insert("coordinates", null, contentValues);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from coordinates",null );
        return res;
    }
    public void deleteEntry(double lat , double lon)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("coordinates","lat ="+lat+" AND long="+lon,null);
    }
}

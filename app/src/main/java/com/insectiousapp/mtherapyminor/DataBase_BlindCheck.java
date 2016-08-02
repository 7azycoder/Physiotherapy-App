package com.insectiousapp.mtherapyminor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




/**
 * Created by Codev on 10/24/2015.
 */
public class DataBase_BlindCheck extends SQLiteOpenHelper {




    public static final String ROOM="room";
    public static final String BLIND_CHECK="blindcheck";

    //if blind check=1 means blind else if blind check=0 normal

    public static final String _ID="id";
    public static final String DATABASE="database";



    public DataBase_BlindCheck(Context context) {
        super(context, DATABASE, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ROOM+" ( "+ _ID + " INTEGER PRIMARY KEY, "+ BLIND_CHECK + " INTEGER )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

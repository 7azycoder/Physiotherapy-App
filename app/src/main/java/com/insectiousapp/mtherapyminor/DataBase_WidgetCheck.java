package com.insectiousapp.mtherapyminor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Codev on 12/7/2015.
 */
public class DataBase_WidgetCheck extends SQLiteOpenHelper {




    public static final String ROOM="room2";
    public static final String WIDGET_CHECK="blindcheck";

    //if widgetcheck=1 means widget is on else off

    public static final String _ID="id";
    public static final String DATABASE="database";



    public DataBase_WidgetCheck(Context context) {
        super(context, DATABASE, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ROOM+" ( "+ _ID + " INTEGER PRIMARY KEY, "+ WIDGET_CHECK + " INTEGER )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

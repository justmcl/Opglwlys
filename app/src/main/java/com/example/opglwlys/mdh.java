package com.example.opglwlys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mdh extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, " + "coord text, " + "color text, " + "pressure text, " + "book text, " + "other text,"+" xmin integer,"+" xmax integer,"+" ymin integer,"+" ymax integer,"+" page integer,"+" line integer,"+" other1 integer)";

    public mdh(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context; }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
//        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
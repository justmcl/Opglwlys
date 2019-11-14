package com.example.opglwlys;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    public static mdh dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置为全屏
        Button update = (Button) findViewById(R.id.bt2);
        update.setOnClickListener(this);

        dbHelper = new mdh(this, "stroke.db", null, 1);
        SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
        ContentValues values = new ContentValues(); // 开始组装第一条数据
        values.put("id",0);
        values.put("pressure","0");

//        db2.insert("Book", null, values); // 插入第一条数据



    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt2:
                Intent intent = new Intent(MainActivity.this, mActivity.class);
                startActivity(intent);


        }

//    public String get1(){
//        SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
////        Cursor cursor = db2.query("Book", new String[]{"coord,pressure,page"}, "page=?", new String[]{"1"}, null, null, null);
//        Cursor cursor = db2.query("Book", null, null, null, null, null, null);
//
//        while (cursor.moveToNext()) {
//            String cooord = cursor.getString(cursor.getColumnIndex("coord"));
//            String pressure = cursor.getString(cursor.getColumnIndex("pressure"));
//            int page = cursor.getInt(cursor.getColumnIndex("page"));
//            if(page==1){
//                Log.e("fff", cooord );
//                Log.e("fff", pressure);
//                Log.e("fff", String.valueOf(page));
//            }
//        }
//        return
//    }
//    public String get1(){}

}}
package com.example.administrator.steps_count.step;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fySpring
 * Date : 2017/1/16
 * To do :
 */

public class Map_DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Map.db"; //数据库名称
    private static final int DB_VERSION = 1;//数据库版本,大于0
    private SQLiteDatabase db;

    //用于创建step表
    private static final String CREATE_TABLE = "create table map (_id INTEGER PRIMARY KEY AUTOINCREMENT, time TEXT,speed TEXT,km TEXT,ka TEXT,date TEXT);";
    private static final String CREATE_TABLE1 = "create table latLng (_id INTEGER PRIMARY KEY AUTOINCREMENT, latitude TEXT,longitude TEXT,date TEXT);";

    public Map_DBHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
        //db.execSQL(NEW_TABLE);
    }

    public Map_DBHelper(Context context, String name, int version, DatabaseErrorHandler errorHandler) {
        super(context, name,null, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewMapData(Map weight) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("time", weight.getTime());
        values.put("speed", weight.getSpeed());
        values.put("km",weight.getKm());
        values.put("ka",weight.getKa());
        values.put("date",weight.getDate());
        db.insert("map", null, values);

    }

    public void addNewLatLngData(LatLng_1 weight) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("latitude", weight.getLatitude());
        values.put("longitude", weight.getLongitude());
        values.put("date",weight.getDate());
        db.insert("latLng", null, values);
    }


    public List<Map> getCurUserDateByDate(String curDate) {
        db=getReadableDatabase();
        List<Map> list=new ArrayList<>();
        Map weight = null;
        Cursor cursor = db.query("map", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String w_date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            if (curDate.equals(w_date)) {
                String w_weight = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String w_text = cursor.getString(cursor.getColumnIndexOrThrow("speed"));
                String w_aim = cursor.getString(cursor.getColumnIndexOrThrow("km"));
                String w_ka = cursor.getString(cursor.getColumnIndexOrThrow("ka"));
                weight = new Map(w_weight, w_text,w_aim,w_ka,w_date);
                list.add(weight);
            }
        }
        //关闭
        cursor.close();
        return list;
    }

    public List<LatLng_1> getCurLatLngByDate(String curDate) {
        db=getReadableDatabase();
        LatLng_1 weight = null;
        List<LatLng_1> list=new ArrayList<>();
        Cursor cursor = db.query("latLng", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String w_date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            if (curDate.equals(w_date)) {
                String w_weight = cursor.getString(cursor.getColumnIndexOrThrow("latitude"));
                String w_text = cursor.getString(cursor.getColumnIndexOrThrow("longitude"));
                weight = new LatLng_1(w_weight, w_text,w_date);
                list.add(weight);
            }
        }
        //关闭
        cursor.close();
        return list;
    }



    public void updateCurUserData(Map stepEntity) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("time", stepEntity.getTime());
        values.put("speed", stepEntity.getSpeed());
        values.put("km",stepEntity.getKm());
        values.put("ka",stepEntity.getKa());
        values.put("date",stepEntity.getDate());
        db.update("map", values, "date=?", new String[]{stepEntity.getDate()});
    }

    public void updateCurLatLngData(LatLng_1 stepEntity) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("latitude", stepEntity.getLatitude());
        values.put("longitude", stepEntity.getLongitude());
        values.put("date",stepEntity.getDate());
        db.update("latLng", values, "date=?", new String[]{stepEntity.getDate()});
    }
    //关闭数据库
    public void close(){
        if (db!=null)
            db.close();
    }
}

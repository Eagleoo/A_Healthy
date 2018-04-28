package com.example.administrator.steps_count.step;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fySpring
 * Date : 2017/1/16
 * To do :
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "StepCounter.db"; //数据库名称
    private static final int DB_VERSION = 1;//数据库版本,大于0
    private SQLiteDatabase db;

    //用于创建Banner表
    private static final String CREATE_BANNER = "create table step (_id INTEGER PRIMARY KEY AUTOINCREMENT, curDate TEXT, totalSteps TEXT);";


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    public DBOpenHelper(Context context, String name, int version, DatabaseErrorHandler errorHandler) {
        super(context, name,null, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANNER);Log.e("onCreate","数据库创建了"+CREATE_BANNER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addNewData(StepEntity stepEntity) {
       db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        db.insert("step", null, values);

        Log.e("addNewData","加入了数据");

        db.close();
    }

    public StepEntity getCurDataByDate(String curDate) {
        db=getReadableDatabase();
        StepEntity stepEntity = null;
        Cursor cursor = db.query("step", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (curDate.equals(date)) {
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                stepEntity = new StepEntity(date, steps);
                //跳出循环
                break;
            }
        }
        //关闭
        db.close();
        cursor.close();
        return stepEntity;
    }

    //遍历数据
    public Cursor mquery(){
        //获取到SQLiteDatabase对象
        db=getReadableDatabase();
        //获取Cursor
        Cursor cursor=db.query("step",null,null,null,null,null,null);
        return cursor;
    }

    //删除数据  根据id删除数据
    public void delete(int id){
        db=getWritableDatabase();
        db.delete(DB_NAME,"name=?",new String[]{String.valueOf(id)});
    }

    public void updateCurData(StepEntity stepEntity) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate",stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        db.update("step", values, "curDate=?", new String[]{stepEntity.getCurDate()});
        db.close();
    }
    //关闭数据库
    public void close(){
        if (db!=null)
            db.close();
    }
}

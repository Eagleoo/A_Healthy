package com.example.administrator.steps_count.step;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.steps_count.Main_Activity.User_Data;
import com.example.administrator.steps_count.Main_Activity.Weight;

import java.util.ArrayList;

/**
 * Created by fySpring
 * Date : 2017/1/16
 * To do :
 */

public class User_DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "UserData.db"; //数据库名称
    private static final int DB_VERSION = 1;//数据库版本,大于0
    private SQLiteDatabase db;

    //用于创建step表
    private static final String CREATE_BANNER = "create table user_data (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_ka TEXT, user_drink TEXT,user_coffee TEXT,user_sleep TEXT,user_date TEXT);";
    private static final String CREATE_TABLE = "create table user_ka (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_aim_ka TEXT);";
    //private static final String NEW_TABLE="create table if not exists eat_plan(id INTEGER PRIMARY KEY AUTOINCREMENT, totals_ka TEXT, aim_ka TEXT,date_ka TEXT);";

    public User_DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
        //db.execSQL(NEW_TABLE);
    }

    public User_DBOpenHelper(Context context, String name, int version, DatabaseErrorHandler errorHandler) {
        super(context, name,null, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANNER);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewUserData(User_Data weight) {
        db = getReadableDatabase();
        String cur_date=TimeUtil.getCurrentDate();

        if(getCurUserDateByDate(cur_date)==null){
            ContentValues values = new ContentValues();
            values.put("user_ka", weight.getUser_ka());
            values.put("user_drink", weight.getUser_drink());
            values.put("user_coffee",weight.getUser_coffee());
            values.put("user_sleep",weight.getUser_sleep());
            values.put("user_date",weight.getUser_date());
            db.insert("user_data", null, values);
        }
        else {
            updateCurUserData(weight);
        }

    }

    public void addNewUserKa(String aim) {
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        //Cursor cursor = db.query("user_ka", null, null, null, null, null, null);

            values.put("user_aim_ka",aim);
            db.insert("user_ka", null, values);
            Log.e("加入了卡路里",aim);

//            values.put("user_aim_ka",aim);
//            db.update("user_ka", values, "user_aim_ka=?", new String[]{aim});
//            Log.e("ssssss","更新了"+aim);

    }
    public void updateKa(String aim){
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_aim_ka",aim);
        db.update("user_ka", values, "user_aim_ka=?", new String[]{getAimKa()});
    }


    public String getAimKa() {
        db=getReadableDatabase();
        String aim = null;
        Cursor cursor = db.query("user_ka", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            aim = cursor.getString(cursor.getColumnIndexOrThrow("user_aim_ka"));
        }
        //关闭
        cursor.close();
        return aim;
    }

    public User_Data getCurUserDateByDate(String curDate) {
        db=getReadableDatabase();
        User_Data weight = null;
        Cursor cursor = db.query("user_data", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String w_date = cursor.getString(cursor.getColumnIndexOrThrow("user_date"));
            if (curDate.equals(w_date)) {
                String w_weight = cursor.getString(cursor.getColumnIndexOrThrow("user_ka"));
                String w_text = cursor.getString(cursor.getColumnIndexOrThrow("user_drink"));
                String w_aim = cursor.getString(cursor.getColumnIndexOrThrow("user_coffee"));
                weight = new User_Data(w_weight, w_text,w_aim,w_aim,w_date);
                //跳出循环
                break;
            }
        }
        //关闭
        cursor.close();
        return weight;
    }

    //遍历数据
    public Cursor user_query(){
        //获取到SQLiteDatabase对象
        db=getReadableDatabase();
        //获取Cursor
        Cursor cursor=db.query("user_ka",null,null,null,null,null,null);
        return cursor;
    }


    public void updateCurUserData(User_Data stepEntity) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_ka",stepEntity.getUser_ka());
        values.put("user_drink", stepEntity.getUser_drink());
        values.put("user_coffee",stepEntity.getUser_coffee());
        values.put("user_sleep",stepEntity.getUser_sleep());
        values.put("user_date",stepEntity.getUser_date());
        db.update("user_data", values, "user_date=?", new String[]{stepEntity.getUser_date()});
    }
    //关闭数据库
    public void close(){
        if (db!=null)
            db.close();
    }
}

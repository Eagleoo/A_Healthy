package com.example.administrator.steps_count.step;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.steps_count.Main_Activity.Weight;

import java.util.ArrayList;

/**
 * Created by fySpring
 * Date : 2017/1/16
 * To do :
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "StepCounter.db"; //数据库名称
    private static final int DB_VERSION = 1;//数据库版本,大于0
    private SQLiteDatabase db;

    //用于创建step表
    private static final String CREATE_BANNER = "create table step (_id INTEGER PRIMARY KEY AUTOINCREMENT, curDate TEXT, totalSteps TEXT,totalStepsKm TEXT,totalStepsKa TEXT);";
    private static final String CREATE_PLAN = "create table step_plan (_id INTEGER PRIMARY KEY AUTOINCREMENT, p_step TEXT, p_km TEXT,p_ka TEXT,u_id INTEGER);";
    private static final String CREATE_WEIGHT= "create table weight (_id INTEGER PRIMARY KEY AUTOINCREMENT, w_weight TEXT, w_date TEXT,w_text TEXT,w_aim TEXT);";
    //private static final String NEW_TABLE="create table if not exists eat_plan(id INTEGER PRIMARY KEY AUTOINCREMENT, totals_ka TEXT, aim_ka TEXT,date_ka TEXT);";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
        //db.execSQL(NEW_TABLE);
    }

    public DBOpenHelper(Context context, String name, int version, DatabaseErrorHandler errorHandler) {
        super(context, name,null, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANNER);
        db.execSQL(CREATE_PLAN);
        db.execSQL(CREATE_WEIGHT);Log.e("onCreate","数据库创建了"+CREATE_BANNER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addNewData(StepEntity stepEntity) {
       db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        values.put("totalStepsKm",stepEntity.getTotalStepsKm());
        values.put("totalStepsKa",stepEntity.getTotalStepsKa());
        db.insert("step", null, values);

        Log.e("addNewData","加入了数据");

    }

    public void addNewStepPlan(StepPlan stepPlan) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("p_step", stepPlan.getP_steps());
        values.put("p_km", stepPlan.getP_km());
        values.put("p_ka",stepPlan.getP_ka());
        values.put("u_id",stepPlan.getU_id());
        db.insert("step_plan", null, values);

        Log.e("addNewStepPlan","加入了数据");

    }

    public void addNewWeight(Weight weight) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("w_weight", weight.getWeight());
        values.put("w_date", weight.getW_date());
        values.put("w_text",weight.getW_text());
        values.put("w_aim",weight.getW_aim());
        db.insert("weight", null, values);

        Log.e("addNewData","加入了数据");

    }

    public void addNewKa(Weight weight) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("w_weight", weight.getWeight());
        values.put("w_date", weight.getW_date());
        values.put("w_text",weight.getW_text());
        values.put("w_aim",weight.getW_aim());
        db.insert("weight", null, values);

        Log.e("addNewData","加入了数据");

    }

    public void updateStepPlan(StepPlan stepPlan) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("p_step", stepPlan.getP_steps());
        values.put("p_km", stepPlan.getP_km());
        values.put("p_ka",stepPlan.getP_ka());
        values.put("u_id",stepPlan.getU_id());
        db.update("step_plan", values, "u_id=?", new String[]{String.valueOf(stepPlan.getU_id())});
        Log.e("addNewStepPlan","更新了数据");
        db.close();
    }

    public void updateWeight(Weight weight) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("w_weight", weight.getWeight());
        values.put("w_date", weight.getW_date());
        values.put("w_text",weight.getW_text());
        values.put("w_aim",weight.getW_aim());
        db.update("weight", values, "w_date=?", new String[]{String.valueOf(weight.getW_date())});
        Log.e("更新了",weight.getWeight()+weight.getW_aim());
    }

    public StepEntity getCurDataByDate(String curDate) {
        db=getReadableDatabase();
        StepEntity stepEntity = null;
        Cursor cursor = db.query("step", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (curDate.equals(date)) {
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                String km = cursor.getString(cursor.getColumnIndexOrThrow("totalStepsKm"));
                String ka = cursor.getString(cursor.getColumnIndexOrThrow("totalStepsKa"));
                stepEntity = new StepEntity(date, steps,km,ka);
                //跳出循环
                break;
            }
        }
        //关闭
        cursor.close();
        return stepEntity;
    }

    public Weight getCurWeightByDate(String curDate) {
        db=getReadableDatabase();
        Weight weight = null;
        Cursor cursor = db.query("weight", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String w_date = cursor.getString(cursor.getColumnIndexOrThrow("w_date"));
            if (curDate.equals(w_date)) {
                String w_weight = cursor.getString(cursor.getColumnIndexOrThrow("w_weight"));
                String w_text = cursor.getString(cursor.getColumnIndexOrThrow("w_text"));
                String w_aim = cursor.getString(cursor.getColumnIndexOrThrow("w_aim"));
                weight = new Weight(w_weight, w_date,w_text,w_aim);
                //跳出循环
                break;
            }
        }
        //关闭
        cursor.close();
        return weight;
    }

    public StepPlan getCurDataByUid(int u_id) {
        db=getReadableDatabase();
        StepPlan stepPlan = null;
        Cursor cursor = db.query("step_plan", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("u_id"));
            if (u_id==id) {
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("p_step"));
                String km = cursor.getString(cursor.getColumnIndexOrThrow("p_km"));
                String ka = cursor.getString(cursor.getColumnIndexOrThrow("p_ka"));
                stepPlan= new StepPlan(steps,km,ka,id);
                //跳出循环
                break;
            }
        }
        //关闭
        cursor.close();
        return stepPlan;
    }

    public String getCurWeight(String cur_date) {
        db=getReadableDatabase();
        String cur_weight = null;
        Cursor cursor = db.query("weight", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date=cursor.getString(cursor.getColumnIndexOrThrow("w_date"));
            if(cur_date.equals(date)){
                cur_weight = cursor.getString(cursor.getColumnIndexOrThrow("w_weight"));
            }
        }
        //关闭
        cursor.close();
        return cur_weight;
    }

    public String getWeight() {
        db=getReadableDatabase();
        String cur_weight = null;
        Cursor cursor = db.query("weight", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            cur_weight = cursor.getString(cursor.getColumnIndexOrThrow("w_weight"));
        }
        //关闭
        cursor.close();
        return cur_weight;
    }

    public String getAimWeight() {
        db=getReadableDatabase();
        String aim = null;
        Cursor cursor = db.query("weight", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            aim = cursor.getString(cursor.getColumnIndexOrThrow("w_aim"));
        }
        //关闭
        cursor.close();
        return aim;
    }

    public ArrayList<Weight> getAllWeight() {
        db=getReadableDatabase();
        ArrayList<Weight> weightList=new ArrayList<>();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
//        String str = formatter.format(curDate);

        Cursor cursor = db.query("weight", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Weight weight=new Weight();
            String cur_weight = cursor.getString(cursor.getColumnIndexOrThrow("w_weight"));
            String cur_date = cursor.getString(cursor.getColumnIndexOrThrow("w_date"));
            String cur_text = cursor.getString(cursor.getColumnIndexOrThrow("w_text"));
            weight.setWeight(cur_weight);
            weight.setW_date(cur_date);
            weight.setW_text(cur_text);

            weightList.add(weight);

        }
        //关闭
        cursor.close();
        return weightList;
    }

    public String getCurWeightAim(String cur_date) {
        db=getReadableDatabase();
        String cur_weight_aim = null;
        Cursor cursor = db.query("weight", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date=cursor.getString(cursor.getColumnIndexOrThrow("w_date"));
            if(cur_date.equals(date)){
                cur_weight_aim = cursor.getString(cursor.getColumnIndexOrThrow("w_aim"));
            }
        }
        //关闭
        cursor.close();
        return cur_weight_aim;
    }

    //遍历数据
    public Cursor mquery(){
        //获取到SQLiteDatabase对象
        db=getReadableDatabase();
        //获取Cursor
        Cursor cursor=db.query("step",null,null,null,null,null,null);
        return cursor;
    }

    public Cursor mquery_weight(){
        //获取到SQLiteDatabase对象
        db=getReadableDatabase();
        //获取Cursor
        Cursor cursor=db.query("weight",null,null,null,null,null,null);
        return cursor;
    }

    public Cursor mquery_step_plan(){
        //获取到SQLiteDatabase对象
        db=getReadableDatabase();
        //获取Cursor
        Cursor cursor=db.query("weight",null,null,null,null,null,null);
        return cursor;
    }


    //删除数据  根据id删除数据
    public void delete(int id){
        db=getWritableDatabase();
        db.delete(DB_NAME,"name=?",new String[]{String.valueOf(id)});
    }

    public void updateAim(String aim,String date) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("w_aim",aim);
        values.put("w_date",date);
        db.update("weight", values, "w_date=?", new String[]{date});
        Log.e("更新了aim",aim);
    }

    public void updateCurData(StepEntity stepEntity) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate",stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        values.put("totalStepsKm",stepEntity.getTotalStepsKm());
        values.put("totalStepsKa",stepEntity.getTotalStepsKa());
        db.update("step", values, "curDate=?", new String[]{stepEntity.getCurDate()});
    }    public void close(){

        //关闭数据库
        if (db!=null)
            db.close();
    }
}

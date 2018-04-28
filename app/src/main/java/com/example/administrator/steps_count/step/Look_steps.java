package com.example.administrator.steps_count.step;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Look_steps_adapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26/026.
 */

public class Look_steps extends AppCompatActivity {

    private ListView steps_list;
    private DBOpenHelper db;
    private List<StepEntity> mData=new LinkedList<>();
    private Context mContext;
    private Look_steps_adapter look_steps_adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_steps);

        steps_list=(ListView)findViewById(R.id.steps_list);

        db=new DBOpenHelper(this);
        mContext=Look_steps.this;

        Cursor cursor=db.mquery();
        while (cursor.moveToNext()) {

            String sDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String step = cursor.getString(cursor.getColumnIndex("totalSteps"));
            Log.e("ssssssssssssss","sssssssssssss"+sDate);
            Log.e("ssssssssssssss","sssssssssssss"+step);

            StepEntity stepEntity=new StepEntity();
            stepEntity.setCurDate(sDate);
            stepEntity.setSteps(step);


            mData.add(stepEntity);
        }


        look_steps_adapter=new Look_steps_adapter((LinkedList<StepEntity>)mData,mContext);
        steps_list.setAdapter(look_steps_adapter);
        look_steps_adapter.notifyDataSetChanged();
    }
}

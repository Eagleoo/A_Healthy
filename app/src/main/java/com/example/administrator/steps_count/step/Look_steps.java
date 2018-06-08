package com.example.administrator.steps_count.step;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Look_steps_adapter;
import com.example.administrator.steps_count.mall.User;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/26/026.
 */

public class Look_steps extends AppCompatActivity {

    private ListView steps_list;
    private DBOpenHelper db;
    private Context mContext;
    private Look_steps_adapter look_steps_adapter = null;
    private Button btn_toWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_steps);

        steps_list=(ListView)findViewById(R.id.steps_list);
        btn_toWeb=(Button)findViewById(R.id.btn_toWeb);

        db=new DBOpenHelper(this);
        mContext=Look_steps.this;
        final List<StepEntity> Data=Show_Step();

        look_steps_adapter=new Look_steps_adapter((LinkedList<StepEntity>)Data,mContext);
        steps_list.setAdapter(look_steps_adapter);
        look_steps_adapter.notifyDataSetChanged();

        btn_toWeb.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Json_Tools json_tools=new Json_Tools();
                        if(json_tools.isNetworkAvailable(mContext)){
                            new Thread(new Runnable(){
                                @Override
                                public void run() {

                                    Json_Tools jst=new Json_Tools();
                                    try {
                                        String str_json=jst.Step_ToJson(Data);
                                        OkHttpClient okHttpClient=new OkHttpClient();

                                        RequestBody requestBody = new FormBody.Builder()
                                                .add("sStep",str_json).build();
                                        //创建一个Request
                                        final Request request = new Request.Builder()
                                                .url(Constant.CONNECTURL+"Step_Servelet")
                                                .post(requestBody)//传递请求体
                                                .build();
                                        Response response = okHttpClient.newCall(request).execute();
                                        //判断请求是否成功
                                        if(response.isSuccessful()){
                                            Looper.prepare();
                                            //打印服务端返回结果
                                            Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                        else {

                            Toast.makeText(mContext, "上传失败，请检查网络！", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
        );
    }

    private List<StepEntity> Show_Step(){
        List<StepEntity> mData=new LinkedList<>();
        db=new DBOpenHelper(this);
        mContext=Look_steps.this;

        Cursor cursor=db.mquery();
        while (cursor.moveToNext()) {

            String sDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String step = cursor.getString(cursor.getColumnIndex("totalSteps"));

            StepEntity stepEntity=new StepEntity();
            stepEntity.setCurDate(sDate);
            stepEntity.setSteps(step);
            mData.add(stepEntity);
        }
        return mData;
    }
}

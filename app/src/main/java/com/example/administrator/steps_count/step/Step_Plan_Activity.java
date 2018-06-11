package com.example.administrator.steps_count.step;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.ActivityControl;
import com.example.administrator.steps_count.Main_Activity.Plan_Activity;
import com.example.administrator.steps_count.Main_Activity.Plan_Add_Activity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Step_Plan_Activity extends AppCompatActivity {
    private EditText edt_steps,edt_km,edt_ka;
    private Button make_plan;
    private DBOpenHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_plan);

        edt_steps=(EditText)findViewById(R.id.edt_steps);
        edt_km=(EditText)findViewById(R.id.edt_km);
        edt_ka=(EditText)findViewById(R.id.edt_ka);
        make_plan=(Button)findViewById(R.id.make_plan);
        final StepPlan stepPlan=new StepPlan();
        make_plan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if(edt_steps.getText().toString().equals("")||edt_km.getText().toString().equals("")||edt_ka.getText().toString().equals("")){
                                            Looper.prepare();
                                            Toast.makeText(Step_Plan_Activity.this, "请填写完整数据！", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                        else {
                                            stepPlan.setP_steps(edt_steps.getText().toString());
                                            stepPlan.setP_km(edt_km.getText().toString());
                                            stepPlan.setP_ka(edt_ka.getText().toString());
                                            //这里放用户id·············································
                                            stepPlan.setU_id(1);
                                            db=new DBOpenHelper(Step_Plan_Activity.this);
                                            Cursor cursor=db.mquery_step_plan();
                                            if (!cursor.moveToNext()){
                                                db.addNewStepPlan(stepPlan);

                                            }
                                            else {
                                                db.updateStepPlan(stepPlan);
                                            }
                                            Json_Tools json_tools=new Json_Tools();
                                            Looper.prepare();
                                            Toast.makeText(Step_Plan_Activity.this, "创建成功！", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                            try {
                                                String jsonString=json_tools.StepPlan_ToJson(stepPlan);

                                                OkHttpClient okHttpClient=new OkHttpClient();
                                                RequestBody requestBody = new FormBody.Builder()
                                                        .add("StepPlan",jsonString).build();
                                                //创建一个Request
                                                final Request request = new Request.Builder()
                                                        .url(Constant.CONNECTURL+"StepPlan_Into_Servlet")
                                                        .post(requestBody)//传递请求体
                                                        .build();
                                                Response response = okHttpClient.newCall(request).execute();

                                                if(response.isSuccessful()){
                                                    //打印服务端返回结果

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }
                                }
                        ).start();
                    }
                }
        );





    }
}

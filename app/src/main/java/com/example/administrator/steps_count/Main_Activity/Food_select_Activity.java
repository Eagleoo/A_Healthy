package com.example.administrator.steps_count.Main_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Step_About_Activity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.step.User_DBOpenHelper;
import com.example.administrator.steps_count.tools.Json_Tools;
import com.lsp.RulerView;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Food_select_Activity extends AppCompatActivity {
    private RulerView food_rulerView;
    private TextView f_name,f_ka,food_ok,food_cancel,cur_ka;
    private EditText tv_food_choose;
    private ImageView food_chen;
    private String temp_cur_ka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_select_layout);

        food_rulerView = (RulerView) findViewById(R.id.food_rulerView);
        tv_food_choose=(EditText) findViewById(R.id.tv_food_choose);
        f_name=(TextView)findViewById(R.id.f_name);
        f_ka=(TextView)findViewById(R.id.f_ka);
        food_ok=(TextView) findViewById(R.id.food_ok);
        food_cancel=(TextView)findViewById(R.id.food_cancel);
        cur_ka=(TextView)findViewById(R.id.cur_ka);
        food_chen=(ImageView)findViewById(R.id.food_chen);

        Intent intent=getIntent();
        final String temp_name=intent.getStringExtra("f_name");
        final String temp_ka=intent.getStringExtra("f_ka");
        final String temp_time=intent.getStringExtra("f_time");
        temp_cur_ka=intent.getStringExtra("cur_ka");
        final String temp_date=TimeUtil.getCurrentDate();

        final DecimalFormat df=new DecimalFormat("#.#");
        f_name.setText(temp_name);
        f_ka.setText(temp_ka);
        cur_ka.setText(temp_cur_ka);

        food_rulerView.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {

            @Override
            public void onEndResult(String s) {
                tv_food_choose.setText(df.format(Float.valueOf(s)));
            }

            @Override
            public void onScrollResult(String s) {

            }
        });

        food_ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        DecimalFormat df=new DecimalFormat("#.#");
                                        U_Food u_food=new U_Food();
                                        u_food.setU_id(Constant.UID);
                                        u_food.setF_name(temp_name);
                                        u_food.setF_ka(String.valueOf(df.format(Double.valueOf(temp_ka)*0.01*Double.valueOf(tv_food_choose.getText().toString()))));
                                        u_food.setF_time(temp_time);
                                        u_food.setF_date(temp_date);
                                        u_food.setF_ke(tv_food_choose.getText().toString());

                                        Json_Tools json_tools = new Json_Tools();
                                        try {
                                            String jsonString = json_tools.U_Food_ToJson(u_food);
                                            OkHttpClient okHttpClient = new OkHttpClient();
                                            RequestBody requestBody = new FormBody.Builder()
                                                    .add("u_food", jsonString).build();
                                            //创建一个Request
                                            final Request request = new Request.Builder()
                                                    .url(Constant.CONNECTURL + "UFood_Servlet")
                                                    .post(requestBody)//传递请求体
                                                    .build();
                                            Response response = okHttpClient.newCall(request).execute();

                                            if (response.isSuccessful()) {
                                                //打印服务端返回结果
                                                Toast.makeText(Food_select_Activity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Food_select_Activity.this, Eat_Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        Looper.loop();
                                    }
                                }
                        ).start();

                    }
                }
        );

        food_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        food_chen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Food_select_Activity.this, Step_About_Activity.class));
            }
        });

    }
}

package com.example.administrator.steps_count.Main_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.ActivityControl;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Food_Self_Activity extends AppCompatActivity {
    private EditText edt_name,edt_ka;
    private Button btn_food_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_add_self);

        edt_name=(EditText)findViewById(R.id.edt_f_name);
        edt_ka=(EditText)findViewById(R.id.edt_f_ka);
        btn_food_add=(Button)findViewById(R.id.btn_food_add);

        btn_food_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        if (!edt_name.getText().toString().equals("") && !edt_ka.getText().toString().equals("")) {
                                            Food food = new Food();
                                            food.setF_name(edt_name.getText().toString());
                                            food.setF_ka(edt_ka.getText().toString());
                                            food.setF_type(String.valueOf(Constant.UID));
                                            Json_Tools json_tools = new Json_Tools();

                                            try {
                                                String jsonString = json_tools.Food_ToJson(food);
                                                OkHttpClient okHttpClient = new OkHttpClient();
                                                RequestBody requestBody = new FormBody.Builder()
                                                        .add("food", jsonString).build();
                                                //创建一个Request
                                                final Request request = new Request.Builder()
                                                        .url(Constant.CONNECTURL + "Eat_Servlet")
                                                        .post(requestBody)//传递请求体
                                                        .build();
                                                Response response = okHttpClient.newCall(request).execute();

                                                if (response.isSuccessful()) {
                                                    //打印服务端返回结果
                                                    Toast.makeText(Food_Self_Activity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Food_Self_Activity.this, Eat_Add_Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            Toast.makeText(Food_Self_Activity.this, "请填写完整数据！", Toast.LENGTH_SHORT).show();
                                        }
                                        Looper.loop();

                                    }
                                }
                        ).start();



                    }
                }
        );
    }
}

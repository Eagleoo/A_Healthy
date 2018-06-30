package com.example.administrator.steps_count.Main_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Text_Result extends AppCompatActivity {
    private TextView tv_result;
    private String answer_string,type_string;
    private List<TextResult> list=new ArrayList<>();
    private int a=0,b=0,c=0;
    private ImageView result_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_result);
        tv_result=(TextView)findViewById(R.id.tv_result);
        result_back=(ImageView)findViewById(R.id.result_back);
        Intent intent = getIntent();
        answer_string=intent.getStringExtra("answer");
        type_string=intent.getStringExtra("type");
        result_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        LoadResult();

    }

    public void LoadResult(){

        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Text_Result_Servlet")
                .get()//传递请求体
                .build();
        //封装成可执行的call对象
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                final Json_Tools json_tools=new Json_Tools();
                list=json_tools.Json_To_TextResult("result",json);
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                for(int i=0;i<answer_string.length();i++){
                                    char answer = answer_string.charAt(i);
                                    if(answer=='A'){
                                        a++;
                                    }
                                    else if(answer=='B'){
                                        b++;
                                    }
                                    else {
                                        c++;
                                    }
                                }
                                if(a>b){
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getT_type().equals(type_string)&&list.get(i).getAnswer().equals("A")){
                                            tv_result.setText(list.get(i).getResult());
                                        }
                                    }
                                }
                                else if(b>a){
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getT_type().equals(type_string)&&list.get(i).getAnswer().equals("B")){
                                            tv_result.setText(list.get(i).getResult());
                                        }
                                    }
                                }
                                else {
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getT_type().equals(type_string)&&list.get(i).getAnswer().equals("default")){
                                            tv_result.setText(list.get(i).getResult());
                                        }
                                    }
                                }

                            }
                        }
                );
            }
        });
    }
}

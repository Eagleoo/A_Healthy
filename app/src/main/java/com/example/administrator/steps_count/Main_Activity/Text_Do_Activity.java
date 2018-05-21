package com.example.administrator.steps_count.Main_Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Text_Do_Activity extends AppCompatActivity {
    private TextView text_tittle,text_a,text_b,text_c,index;
    private List<Text> TextList=new ArrayList<Text>();
    private List<Text> TextListType=new ArrayList<Text>();
    private int i=0;
    private StringBuffer stringBuffer=new StringBuffer();
    private Button btn_textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_do_layout);
        text_tittle=(TextView)findViewById(R.id.text_tittle);
        text_a=(TextView)findViewById(R.id.text_a);
        text_b=(TextView)findViewById(R.id.text_b);
        text_c=(TextView)findViewById(R.id.text_c);
        index=(TextView)findViewById(R.id.index);
        btn_textResult=(Button)findViewById(R.id.btn_Result);
        LinearLayout select_a = (LinearLayout) findViewById(R.id.select_a);
        LinearLayout select_b = (LinearLayout) findViewById(R.id.select_b);
        LinearLayout select_c = (LinearLayout) findViewById(R.id.select_c);
        LinearLayout text_linearLayout = (LinearLayout) findViewById(R.id.text_linearLayout);


        final Intent intent = getIntent();
        final String stringType=intent.getStringExtra("type");
        switch (stringType){
            case "生活":
                text_linearLayout.setBackgroundColor(getResources().getColor(R.color.shenghuo));
                        select_a.setBackgroundResource(R.drawable.text_btn_4);
                        select_b.setBackgroundResource(R.drawable.text_btn_4);
                        select_c.setBackgroundResource(R.drawable.text_btn_4);break;
            case "女性":
                text_linearLayout.setBackgroundColor(getResources().getColor(R.color.woman));break;
            case "烟瘾":
                text_linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                        select_a.setBackgroundResource(R.drawable.text_btn_1);
                        select_b.setBackgroundResource(R.drawable.text_btn_1);
                        select_c.setBackgroundResource(R.drawable.text_btn_1);break;
            case "弱点":
                text_linearLayout.setBackgroundColor(getResources().getColor(R.color.ruodian));
                        select_a.setBackgroundResource(R.drawable.text_btn_4);
                        select_b.setBackgroundResource(R.drawable.text_btn_4);
                        select_c.setBackgroundResource(R.drawable.text_btn_4);break;
            case "短信":
                text_linearLayout.setBackgroundColor(getResources().getColor(R.color.duanxin));
                        select_a.setBackgroundResource(R.drawable.text_btn_2);
                        select_b.setBackgroundResource(R.drawable.text_btn_2);
                        select_c.setBackgroundResource(R.drawable.text_btn_2);break;
            case "手机":
                text_linearLayout.setBackgroundColor(getResources().getColor(R.color.shouji));
                        select_a.setBackgroundResource(R.drawable.text_btn_3);
                        select_b.setBackgroundResource(R.drawable.text_btn_3);
                        select_c.setBackgroundResource(R.drawable.text_btn_3);break;
        }

        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Text_Servlet")
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
                runOnUiThread(
                        new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                TextList=json_tools.Json_ToText("text",json);
                                for(int j=0;j<TextList.size();j++){
                                    if(TextList.get(j).getT_type().equals(stringType)){
                                        TextListType.add(TextList.get(j));
                                    }
                                }
                                if(TextListType.get(i).getT_C().equals("")){
                                    findViewById(R.id.select_c).setVisibility(View.GONE);
                                }
                                else {
                                    findViewById(R.id.select_c).setVisibility(View.VISIBLE);
                                }


                                index.setText(i+1+"/"+TextListType.size());
                                text_tittle.setText(TextListType.get(i).getT_tittle());
                                text_a.setText(TextListType.get(i).getT_A());
                                text_b.setText(TextListType.get(i).getT_B());
                                text_c.setText(TextListType.get(i).getT_C());
                                btn_textResult.setVisibility(View.GONE);


                            }
                        }
                );


            }
        });

        select_a.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        stringBuffer.append("A");

                        if(i<TextListType.size()-1){
                            i++;
                            if(TextListType.get(i).getT_C().equals("")){
                                findViewById(R.id.select_c).setVisibility(View.GONE);
                            }
                            else {
                                findViewById(R.id.select_c).setVisibility(View.VISIBLE);
                            }
                            index.setText(i+1+"/"+TextListType.size());
                            text_tittle.setText(TextListType.get(i).getT_tittle());
                            text_a.setText(TextListType.get(i).getT_A());
                            text_b.setText(TextListType.get(i).getT_B());
                            text_c.setText(TextListType.get(i).getT_C());
                            btn_textResult.setVisibility(View.GONE);

                        }
                        else {
                            btn_textResult.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        select_b.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        stringBuffer.append("B");

                        if(i<TextListType.size()-1){
                            i++;
                            if(TextListType.get(i).getT_C().equals("")){
                                findViewById(R.id.select_c).setVisibility(View.GONE);
                            }
                            else {
                                findViewById(R.id.select_c).setVisibility(View.VISIBLE);
                            }
                            index.setText(i+1+"/"+TextListType.size());
                            text_tittle.setText(TextListType.get(i).getT_tittle());
                            text_a.setText(TextListType.get(i).getT_A());
                            text_b.setText(TextListType.get(i).getT_B());
                            text_c.setText(TextListType.get(i).getT_C());
                            btn_textResult.setVisibility(View.GONE);

                        }
                        else {
                            btn_textResult.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        select_c.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        stringBuffer.append("C");

                        if(i<TextListType.size()-1){
                            i++;
                            if(TextListType.get(i).getT_C().equals("")){
                                findViewById(R.id.select_c).setVisibility(View.GONE);
                            }
                            else {
                                findViewById(R.id.select_c).setVisibility(View.VISIBLE);
                            }
                            index.setText(i+1+"/"+TextListType.size());
                            text_tittle.setText(TextListType.get(i).getT_tittle());
                            text_a.setText(TextListType.get(i).getT_A());
                            text_b.setText(TextListType.get(i).getT_B());
                            text_c.setText(TextListType.get(i).getT_C());

                        }
                        else {
                            btn_textResult.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        btn_textResult.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s=stringBuffer.toString();
                        Intent intent1=new Intent();
                        intent1.putExtra("answer",s);
                        intent1.putExtra("type",stringType);
                        intent1.setClass(Text_Do_Activity.this, Text_Result.class);
                        startActivity(intent1);
                    }
                }
        );
    }

}

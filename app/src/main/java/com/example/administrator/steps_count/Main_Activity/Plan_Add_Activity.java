package com.example.administrator.steps_count.Main_Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.Frg_Plan1;
import com.example.administrator.steps_count.fragment.Frg_Plan2;
import com.example.administrator.steps_count.fragment.Frg_Plan3;
import com.example.administrator.steps_count.fragment.Frg_Plan4;
import com.example.administrator.steps_count.fragment.Frg_Plan5;
import com.example.administrator.steps_count.mall.User;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Plan_Add_Activity extends AppCompatActivity {
    private List<String> listtitle = new ArrayList<String>();
    private List<Map<String, Object>> mData;
    private ViewPager plan_viewpager;
    private PagerTabStrip tabStrip;
    private LayoutInflater inflater;
    private List<Fragment> fragmentList;
    private Button create_plan;
    private Context context=this;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_add_layout);

        plan_viewpager = (ViewPager)findViewById(R.id.plan_viewpager);
        create_plan=(Button)findViewById(R.id.create_plan);
        back=(ImageView)findViewById(R.id.back);
        inflater=getLayoutInflater();
        listtitle = new ArrayList<String>();

        listtitle.add("饮食");
        listtitle.add("运动");
        listtitle.add("喝水");
        listtitle.add("睡眠");
        listtitle.add("其他");

        fragmentList=new ArrayList<Fragment>();

        fragmentList.add(new Frg_Plan1());
        fragmentList.add(new Frg_Plan2());
        fragmentList.add(new Frg_Plan3());
        fragmentList.add(new Frg_Plan4());
        fragmentList.add(new Frg_Plan5());

        plan_viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragmentList));

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Plan_Add_Activity.this,Plan_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        create_plan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        final EditText et_plan = new EditText(context);
                                        Looper.prepare();
                                        new AlertDialog.Builder(context).setTitle("自定义计划")
                                                .setIcon(R.drawable.plan)
                                                .setView(et_plan)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Plan plan=new Plan();
                                                        String input = et_plan.getText().toString();
                                                        plan.setP_name(input);
                                                        plan.setP_select(1);
                                                        plan.setP_type("其他");
                                                        Json_Tools json_tools=new Json_Tools();
                                                        try {
                                                            String jsonString=json_tools.Plan_ToJson(plan);
                                                            OkHttpClient okHttpClient=new OkHttpClient();
                                                            RequestBody requestBody = new FormBody.Builder()
                                                                    .add("Plan",jsonString).build();
                                                            //创建一个Request
                                                            final Request request = new Request.Builder()
                                                                    .url(Constant.CONNECTURL+"Plan_Servlet")
                                                                    .post(requestBody)//传递请求体
                                                                    .build();
                                                            Response response = okHttpClient.newCall(request).execute();

                                                            if(response.isSuccessful()){
                                                                //打印服务端返回结果
                                                                Toast.makeText(context, "创建成功！", Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(Plan_Add_Activity.this,Plan_Activity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                } )
                                                .setNegativeButton("取消", null)
                                                .show();
                                        Looper.loop();
                                    }
                                }
                        ).start();

                    }
                }
        );

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        public List<Fragment> mFragmentList;


        public MyPagerAdapter( FragmentManager fm,List<Fragment> fragmentList) {
            super(fm);
            this.mFragmentList=fragmentList;
        }


        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listtitle.get(position);
        }
    }


}

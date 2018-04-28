package com.example.administrator.steps_count.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.CircleFragment;
import com.example.administrator.steps_count.fragment.FootFragment;
import com.example.administrator.steps_count.fragment.MainFragment;
import com.example.administrator.steps_count.fragment.MallFragment;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.step.MainActivity;

public class Frag_MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FrameLayout frameLayout;
    private RadioButton tv_main,tv_mall,tv_foot,tv_circle,tv_me;
    public static String data;
    public static String name;
    private MyBroadcost myBroadcost;
    // public static String baiduinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_mainactivity);

        myBroadcost=new MyBroadcost();
        IntentFilter intentFilter=new IntentFilter("android.intent.action.Broadcast");
        registerReceiver(myBroadcost,intentFilter);
        dynamicFragment(new MainFragment(),"mainFragment");
        initView();
        Intent intent = getIntent();
        data= intent.getStringExtra("head");
        name=intent.getStringExtra("name");
        tv_main.setChecked(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcost);
    }


    public void initView(){
        frameLayout=(FrameLayout)findViewById(R.id.framelayout);
        tv_main= (RadioButton) findViewById(R.id.tv_main);
        tv_mall= (RadioButton) findViewById(R.id.tv_mall);
        tv_foot= (RadioButton) findViewById(R.id.tv_foot);
        tv_circle= (RadioButton) findViewById(R.id.tv_circle);
        tv_me= (RadioButton) findViewById(R.id.tv_me);

        tv_main.setOnClickListener(this);
        tv_mall.setOnClickListener(this);
        tv_foot.setOnClickListener(this);
        tv_circle.setOnClickListener(this);
        tv_me.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_main:
                dynamicFragment(new MainFragment(),"mainFragment");
                break;
            case R.id.tv_mall:
                dynamicFragment(new MallFragment(),"mallFragment");
                break;
            case R.id.tv_foot:
                dynamicFragment(new FootFragment(),"footFragment");
                break;
            case R.id.tv_circle:
                dynamicFragment(new CircleFragment(),"circleFragment");
                break;
            case R.id.tv_me:
                dynamicFragment(new MeFragment(),"meFragment");
            case R.id.new_plan:
                Toast.makeText(this, "添加新计划", Toast.LENGTH_SHORT).show();break;
            case R.id.fin_plan:
                Toast.makeText(this, "去完成", Toast.LENGTH_SHORT).show();break;

            case R.id.text_more:
                Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show();break;
            case R.id.fin_text:
                Toast.makeText(this, "去测评", Toast.LENGTH_SHORT).show();break;
        }
    }

    //动态添加Fragment
    public void dynamicFragment(android.support.v4.app.Fragment fragment,String tag){
        //1.获取碎片管理器
        FragmentManager supportFramentManager=getSupportFragmentManager();
        //2.开启一个事务
        FragmentTransaction beginTransaction=supportFramentManager.beginTransaction();
        //3.添加碎片
        beginTransaction.replace(R.id.framelayout,fragment,tag);
        //4.提交事务
        beginTransaction.commit();
    }
    class  MyBroadcost extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}

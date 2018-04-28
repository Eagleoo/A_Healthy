package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;


/**
 * Created by PC on 2018/3/24.
 */

public class Mall_Detail_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView mall_detail_shop;
    private TextView mall_detail_detail;
    private ImageView mall_detail_exit;
    private Button mall_detail_makeorder;
    private String mall_id;
    private FrameLayout frameLayout;

    private Mall_Shop_Fragment Mall_Shop_Fragment;
    private View line1;
    private View line2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_detail_layout);
        init();

        Bundle bundle=new Bundle();
        bundle = this.getIntent().getExtras();
        mall_id=bundle.getString("0x0");

        dynamicFragment(new Mall_Shop_Fragment(),"mall_shop_fragment");
    }


    public void init()
    {
        mall_detail_detail= (TextView) findViewById(R.id.mall_detail_detail);
        mall_detail_shop= (TextView) findViewById(R.id.mall_detail_shop);
        line1=findViewById(R.id.mall_detail_line1);
        line2=findViewById(R.id.mall_detail_line2);
        mall_detail_exit= (ImageView) findViewById(R.id.mall_detail_exit);
        mall_detail_makeorder= (Button) findViewById(R.id.mall_detail_makeorder);
        mall_detail_detail.setOnClickListener(this);
        mall_detail_shop.setOnClickListener(this);
        mall_detail_exit.setOnClickListener(this);
        mall_detail_makeorder.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mall_detail_shop:
                refrshlinecolor();
                line1.setBackgroundColor(Color.BLACK);
                dynamicFragment(new Mall_Shop_Fragment(),"mall_shop_fragment");
                break;
            case R.id.mall_detail_detail:
                refrshlinecolor();
                line2.setBackgroundColor(Color.BLACK);
                dynamicFragment(new Mall_detail_Fragment(),"mall_shop_fragment");
                break;
            case R.id.mall_detail_exit:
                finish();
                break;
            case R.id.mall_detail_makeorder:
                Intent intent=new Intent(getApplicationContext(),Mall_Firmorder_Acitvity.class);
                Bundle bundle=new Bundle();
                bundle.putString("0x3",mall_id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
    public void refrshlinecolor()
    {
        line1.setBackgroundColor(Color.parseColor("#00ffffff"));
        line2.setBackgroundColor(Color.parseColor("#00ffffff"));
    }

    public void dynamicFragment(android.support.v4.app.Fragment fragment,String tag){
        //1.获取碎片管理器
        FragmentManager supportFramentManager=getSupportFragmentManager();
        //2.开启一个事务
        FragmentTransaction beginTransaction=supportFramentManager.beginTransaction();
        //3.添加碎片
        beginTransaction.replace(R.id.mall_detail_framelayout,fragment,tag);
        //4.提交事务
        beginTransaction.commit();
    }



}

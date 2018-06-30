package com.example.administrator.steps_count.mall;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.steps_count.R;

import java.util.ArrayList;
import java.util.List;

public class Order_Management_Activity  extends AppCompatActivity implements View.OnClickListener{
    private ViewPager pager;

    private Button btn_quanbudingdan;
    private Button btn_fukuan;
    private Button btn_fahuo;
    private Button btn_shouhuo;
    private View cursor;
    private Bundle bundle1;
    private int pagecount =0;
    private List<Fragment> fragList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_management);
        bundle1 =new Bundle();
        bundle1 =this.getIntent().getExtras();
        pagecount = Integer.parseInt(bundle1.getString("0x0"));
        initview();

    }

    public void initview()
    {
        pager=(ViewPager) findViewById(R.id.view_pager);

        cursor=findViewById(R.id.img_cursor);

        btn_quanbudingdan=(Button)this.findViewById(R.id.btn_quanbudingdan);
        btn_fukuan=(Button)this.findViewById(R.id.btn_fukuan);
        btn_fahuo=(Button)this.findViewById(R.id.btn_fahuo);
        btn_shouhuo=(Button)this.findViewById(R.id.btn_shouhuo);

        btn_quanbudingdan.setOnClickListener(this);
        btn_fukuan.setOnClickListener(this);
        btn_shouhuo.setOnClickListener(this);
        btn_fahuo.setOnClickListener(this);

        fragList=new ArrayList<Fragment>();
        fragList.add(new Fragment_allorder());
        fragList.add(new Fragment_fukuan());
        fragList.add(new Fragment_fahuo());
        fragList.add(new Fragment_shouhuo());

        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(), fragList);

        pager.setAdapter(adapter);

        resetButtonColor();

        pager.setCurrentItem(pagecount);
        switch (pagecount)
        {
            case 0:
                btn_quanbudingdan.setTextColor(Color.BLACK);
                break;
            case 1:
                btn_fukuan.setTextColor(Color.BLACK);
                break;
            case 2:
                btn_fahuo.setTextColor(Color.BLACK);
                break;
            case 3:
                btn_shouhuo.setTextColor(Color.BLACK);
                break;
        }


        pager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        int lineWidth = getLineWidth(4);

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cursor.getLayoutParams();
                        params.leftMargin = (int) ((positionOffset+ position )* lineWidth);
                        cursor.setLayoutParams(params);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        changeTabColor(position);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }


    //重置按钮颜色
    public void resetButtonColor()
    {
        btn_quanbudingdan.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_fukuan.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_fahuo.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_shouhuo.setBackgroundColor(Color.parseColor("#FFFFFF"));

        btn_quanbudingdan.setTextColor(Color.GRAY);
        btn_fukuan.setTextColor(Color.GRAY);
        btn_fahuo.setTextColor(Color.GRAY);
        btn_shouhuo.setTextColor(Color.GRAY);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_quanbudingdan:
                pager.setCurrentItem(0);
                btn_quanbudingdan.setTextColor(Color.BLACK);
                break;
            case R.id.btn_fukuan:
                pager.setCurrentItem(1);
                btn_fukuan.setTextColor(Color.BLACK);
                break;
            case R.id.btn_fahuo:
                pager.setCurrentItem(2);
                btn_fahuo.setTextColor(Color.BLACK);
                break;
            case R.id.btn_shouhuo:
                pager.setCurrentItem(3);
                btn_shouhuo.setTextColor(Color.BLACK);
                break;
        }
    }
    public int getLineWidth(int tabCount){

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int lineWidth = metric.widthPixels/tabCount;
        return lineWidth;
    }

    private void changeTabColor(int position){
        resetButtonColor();

        if(0 == position) {
            btn_quanbudingdan.setTextColor(Color.parseColor("#000000"));
        }

        if(1 == position) {
            btn_fukuan.setTextColor(Color.parseColor("#000000"));
        }

        if(2 == position) {
            btn_fahuo.setTextColor(Color.parseColor("#000000"));
        }
        if(3 == position) {
            btn_shouhuo.setTextColor(Color.parseColor("#000000"));
        }
    }
    public void destory()
    {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }}


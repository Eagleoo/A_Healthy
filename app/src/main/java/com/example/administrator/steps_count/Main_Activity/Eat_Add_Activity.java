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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.Frg_Food1;
import com.example.administrator.steps_count.fragment.Frg_Food2;
import com.example.administrator.steps_count.fragment.Frg_Plan1;
import com.example.administrator.steps_count.fragment.Frg_Plan2;
import com.example.administrator.steps_count.fragment.Frg_Plan3;
import com.example.administrator.steps_count.fragment.Frg_Plan4;
import com.example.administrator.steps_count.fragment.Frg_Plan5;
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

public class Eat_Add_Activity extends AppCompatActivity {
    private List<String> listtitle = new ArrayList<String>();
    private List<Map<String, Object>> mData;
    private ViewPager eat_viewpager;
    private PagerTabStrip tabStrip;
    private LayoutInflater inflater;
    private List<Fragment> fragmentList;
    private Button btn_food_add;
    private Context context=this;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_add_layout);

        eat_viewpager = (ViewPager)findViewById(R.id.eat_viewpager);
        btn_food_add=(Button)findViewById(R.id.btn_food_add);
        inflater=getLayoutInflater();
        listtitle = new ArrayList<String>();

        listtitle.add("常见");
        listtitle.add("自定义");

        fragmentList=new ArrayList<Fragment>();

        fragmentList.add(new Frg_Food1());
        fragmentList.add(new Frg_Food2());

        eat_viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragmentList));

        btn_food_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Eat_Add_Activity.this,Food_Self_Activity.class));
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

package com.example.administrator.steps_count.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.example.administrator.steps_count.step.Step_Map;
import com.example.administrator.steps_count.Main_Activity.Eat_Activity;
import com.example.administrator.steps_count.Main_Activity.Weight_Activity;
import com.example.administrator.steps_count.Main_Activity.Weight_Add_Activity;
import com.example.administrator.steps_count.Main_Activity.Text_Activity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.Step_MainActivity;
import com.example.administrator.steps_count.step.Step_Map_Activity;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.step.User_DBOpenHelper;
import com.example.administrator.steps_count.tools.Json_Tools;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener{
    private GridView grid_view;
    private GridLayout grid_layout;
    private List<Map<String,Object>> dataList;
    private int[] icon={R.drawable.explorer,R.drawable.question1,R.drawable.dct};
    private String[] str={"搜索","提问","问医生"};
    private SimpleAdapter adapter;
    private ViewAnimator viewAnimator,animator_text;
    private final long TIME_INTERVAL = 4000L;
    private RollPagerView mRollViewPager;
    private boolean autoPlayFlag = true;
    private TextView txt_timeCount,txt_min,txt_plan,tv_weight,tv_cur_ka;
    private ScrollView sc;
    private String string;
    private Button btn_weight,btn_eat,btn_step_start,btn_run_start;
    private LinearLayout linear_weight,linear_eat,linear_run,linear_step;
    private DBOpenHelper db;
    private User_DBOpenHelper u_db;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(autoPlayFlag){
                showNext();
            }
            else {
                super.handleMessage(msg);
            }
            handler.sendMessageDelayed(new Message(),TIME_INTERVAL);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_layout, container, false);


        sc=(ScrollView)view.findViewById(R.id.sc);
        grid_view=(GridView)view.findViewById(R.id.grid_view);
        txt_timeCount=(TextView)view.findViewById(R.id.txt_timeCount);
        txt_min=(TextView)view.findViewById(R.id.txt_min);
        txt_plan=(TextView)view.findViewById(R.id.txt_plan);
        tv_weight=(TextView)view.findViewById(R.id.tv_weight);
        tv_cur_ka=(TextView)view.findViewById(R.id.tv_cur_ka);
        btn_weight=(Button)view.findViewById(R.id.btn_weight);
        btn_eat=(Button)view.findViewById(R.id.btn_eat);
        btn_step_start=(Button)view.findViewById(R.id.btn_step_start);
        btn_run_start=(Button)view.findViewById(R.id.btn_run_start);
        linear_weight=(LinearLayout)view.findViewById(R.id.linear_weight);
        linear_eat=(LinearLayout)view.findViewById(R.id.linear_eat);
        linear_step=(LinearLayout)view.findViewById(R.id.linear_step);
        linear_run=(LinearLayout)view.findViewById(R.id.linear_run);

        viewAnimator=(ViewAnimator)view.findViewById(R.id.animator) ;
        animator_text=(ViewAnimator)view.findViewById(R.id.animator_text) ;
        mRollViewPager = (RollPagerView)view.findViewById(R.id.roll_view_pager);
        grid_layout=(GridLayout)view.findViewById(R.id.grid_layout);

        Log.e("","");

        db=new DBOpenHelper(getActivity());
        u_db=new User_DBOpenHelper(getActivity());

        if(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate())==null){
            tv_cur_ka.setText("0.00千卡");
        }
        else {
            tv_cur_ka.setText(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate()).getUser_ka()+"千卡");
        }

        if(db.getWeight()==null){
            tv_weight.setText("0.00公斤");
        }
        else {
            tv_weight.setText(db.getWeight()+"公斤");
        }

        grid_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Fragment fragment=new MallFragment();
                        fm.beginTransaction().replace(R.id.framelayout,fragment).commit();
                    }
                }
        );

        btn_weight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Weight_Add_Activity.class));
                    }
                }
        );

        linear_weight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Weight_Activity.class));
                    }
                }
        );

        linear_eat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Eat_Activity.class));
                    }
                }
        );
        btn_eat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Eat_Activity.class));
                    }
                }
        );

        linear_run.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Step_Map_Activity.class));
                    }
                }
        );

        linear_step.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Step_MainActivity.class));
                    }
                }
        );

        btn_run_start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Step_Map.class));
                    }
                }
        );

        btn_step_start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Step_MainActivity.class));
                    }
                }
        );

        handler.sendMessageDelayed(new Message(),TIME_INTERVAL);
        adapter=new SimpleAdapter(getActivity(),getData(),R.layout.main_grid_layout,new String[]{"image","step_chart"},
                new int[]{R.id.image,R.id.text});

        grid_view.setAdapter(adapter);
        grid_view.setOnItemClickListener(this);


        //设置播放时间间隔
        mRollViewPager.setPlayDelay(4000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW,Color.WHITE));
        sale_TimeCount();
        Plan_Count();
        txt_plan.setText(string);
        animator_text.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Text_Activity.class));
                    }
                }
        );
        viewAnimator.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Fragment fragment=new CircleFragment();
                        fm.beginTransaction().replace(R.id.framelayout,fragment).commit();
                    }
                }
        );
        return view;
    }

    private void sale_TimeCount(){
        TimeCount time = new TimeCount(60000, 1000);
        time.start();
    }

    private List<Map<String,Object>> getData(){
        dataList=new ArrayList<>();
        for(int i=0;i<icon.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("image",icon[i]);
            map.put("step_chart",str[i]);
            dataList.add(map);
        }
        return dataList;
    }

    //问医生点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void showNext(){

        if(getActivity()!=null){
            viewAnimator.setOutAnimation(getActivity(),R.anim.slide_out_up);
            viewAnimator.setInAnimation(getActivity(),R.anim.slide_in_down);
            animator_text.setOutAnimation(getActivity(),R.anim.slide_out_up);
            animator_text.setInAnimation(getActivity(),R.anim.slide_in_down);
            animator_text.showNext();
            viewAnimator.showNext();
        }

    }

    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.runwoman,
                R.drawable.bycle,
                R.drawable.panpa,
        };


        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发


            int  num=Integer.valueOf(txt_min.getText().toString());
            num--;
            txt_min.setText(String.valueOf(num));
            sale_TimeCount();

            if(num==0){
                txt_timeCount.setText("已结束");
                txt_min.setText("已结束");
            }


        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            txt_timeCount.setText(millisUntilFinished /1000+"秒");
        }
    }

    public String Plan_Count(){
        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Plan_Count_Servlet")
                .get()
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
                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    string=json_tools.Json_ToCount(json);
//                                    if(string.equals("0")){
//                                        txt_plan.setText("今天任务完成真棒！");
//                                        txt_plan.setTextColor(0x00EC00);
//                                    }
                                    if(string.equals("0")){
                                        txt_plan.setText("0");
                                    }
                                        txt_plan.setText(string);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                );


            }
        });
        return string ;
    }

}

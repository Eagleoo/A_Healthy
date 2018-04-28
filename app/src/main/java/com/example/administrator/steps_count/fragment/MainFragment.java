package com.example.administrator.steps_count.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.administrator.steps_count.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener{
    private GridView grid_view;
    private List<Map<String,Object>> dataList;
    private int[] icon={R.drawable.explorer,R.drawable.question1,R.drawable.dct};
    private String[] str={"搜索","提问","问医生"};
    private SimpleAdapter adapter;
    private ViewAnimator viewAnimator,animator_text;
    private final long TIME_INTERVAL = 4000L;
    private RollPagerView mRollViewPager;
    private boolean autoPlayFlag = true;
    private TextView txt_timeCount,txt_min;
    private ScrollView sc;
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
        viewAnimator=(ViewAnimator)view.findViewById(R.id.animator) ;
        animator_text=(ViewAnimator)view.findViewById(R.id.animator_text) ;
        mRollViewPager = (RollPagerView)view.findViewById(R.id.roll_view_pager);

        handler.sendMessageDelayed(new Message(),TIME_INTERVAL);
        adapter=new SimpleAdapter(getActivity(),getData(),R.layout.main_grid_layout,new String[]{"image","text"},
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
            map.put("text",str[i]);
            dataList.add(map);
        }
        return dataList;
    }

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

}

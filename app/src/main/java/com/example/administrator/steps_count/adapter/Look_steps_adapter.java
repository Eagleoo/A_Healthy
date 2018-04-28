package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.StepEntity;

import java.util.LinkedList;

/**
 * Created by Administrator on 2018/3/26/026.
 */

public class Look_steps_adapter extends BaseAdapter {

    private LinkedList<StepEntity> mData;
    private Context mContext;

    public Look_steps_adapter(LinkedList<StepEntity> mData,Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;

        if(view==null) {
            //创建缓冲布局界面，获取界面上的组件
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.look_steps_adapter, viewGroup, false);
            //  Log.v("AnimalAdapter","改进后调用一次getView方法");
            holder=new ViewHolder();
            holder.curDate=(TextView)view.findViewById(R.id.tv_date);
            holder.steps=(TextView)view.findViewById(R.id.tv_step);

            Log.e("qqqqqqqqqqqqq","qqqqqqqqqqq");

            view.setTag(holder);
        }
        else {
            //用原有组件
            holder=(ViewHolder)view.getTag();
            Log.e("qqqqqqqqqqqqq","qqqqqqqqqqq");
        }
        holder.curDate.setText(mData.get(i).getCurDate());
        holder.steps.setText(mData.get(i).getSteps());


        return view;
    }
    static final class ViewHolder {

        TextView curDate;
        TextView steps;
    }
}




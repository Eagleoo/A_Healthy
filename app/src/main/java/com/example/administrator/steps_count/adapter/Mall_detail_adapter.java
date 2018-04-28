package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.Mall_detail_Bean;

import java.util.List;

/**
 * Created by PC on 2018/3/29.
 */

public class Mall_detail_adapter extends BaseAdapter {
    private Context context;
    private List<Mall_detail_Bean> list;
    private LayoutInflater layoutInflater;

    public Mall_detail_adapter(Context context, List<Mall_detail_Bean> list) {
        layoutInflater=LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Mall_detail_Bean mall_detail_bean=list.get(i);
        Mall_detail_adapter.ViewHolder holder;
        if (view==null)
        {
            holder=new Mall_detail_adapter.ViewHolder();
            view= layoutInflater.inflate(R.layout.mall_detail_item,null);
            holder.mall_detail_icon=(ImageView)view.findViewById(R.id.mall_detail_item_img);
            view.setTag(holder);
        }
        else
        {
            holder= (Mall_detail_adapter.ViewHolder) view.getTag();
        }
        holder.mall_detail_icon.setImageResource(mall_detail_bean.getMall_detail_img());

        return view;
    }
    static class ViewHolder
    {
        ImageView mall_detail_icon;
    }
}

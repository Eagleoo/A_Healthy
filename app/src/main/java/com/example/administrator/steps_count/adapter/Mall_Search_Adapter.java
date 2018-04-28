package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.Mall_Search_Bean;

import java.util.List;

/**
 * Created by PC on 2018/3/31.
 */

public class Mall_Search_Adapter extends BaseAdapter {
    private Context context;
    private List<Mall_Search_Bean> list;
    private LayoutInflater layoutInflater;

    public Mall_Search_Adapter(Context context, List<Mall_Search_Bean> list) {
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
        Mall_Search_Bean mall_search_bean=list.get(i);
        Mall_Search_Adapter.ViewHolder holder;
        if (view==null)
        {
            holder=new Mall_Search_Adapter.ViewHolder();
            view= layoutInflater.inflate(R.layout.mall_detail_item,null);
            holder.shop_name= (TextView) view.findViewById(R.id.mall_detail_item_img);
            view.setTag(holder);
        }
        else
        {
            holder= (Mall_Search_Adapter.ViewHolder) view.getTag();
        }
        holder.shop_name.setText(mall_search_bean.getShop_name());

        return view;
    }
    static class ViewHolder
    {
        TextView shop_name;
    }
}

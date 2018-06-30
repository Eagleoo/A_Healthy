package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.Mall;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by PC on 2018/3/8.
 */

public class Mall_adapter extends BaseAdapter {
    private Context context;
    private List<Mall> list1=new ArrayList<>();
    private LayoutInflater layoutInflater;

    private Bitmap imgBit;
    public Mall_adapter(Context context, List<Mall> list) {
        if(context!=null&&list1!=null&&list!=null){
            layoutInflater=LayoutInflater.from(context);
            this.context = context;
            this.list1 = list;
        }

    }
    @Override
    public int getCount() {
        if(list1.size()!=0){
            return list1.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return list1.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Mall mall=list1.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.mall_item,null);
            holder.mall_img= (ImageView) view.findViewById(R.id.mall_img);
            holder.mall_name=(TextView) view.findViewById(R.id.mall_name);
            holder.mall_price= (TextView) view.findViewById(R.id.mall_fine);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        Glide.with(context).load(mall.getMall_img()).into(holder.mall_img);
        holder.mall_name.setText(mall.getMall_name());
        holder.mall_price.setText(mall.getMall_price());
        return view;
    }
    static class ViewHolder
    {
        ImageView mall_img;
        TextView mall_name;
        TextView mall_price;
    }

}


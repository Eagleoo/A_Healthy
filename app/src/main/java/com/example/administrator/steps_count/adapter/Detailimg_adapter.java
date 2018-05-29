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
import com.example.administrator.steps_count.mall.Detail_img;
import com.example.administrator.steps_count.mall.Mall;

import java.io.IOException;
import java.io.InputStream;
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

public class Detailimg_adapter extends BaseAdapter {
    private Context context;
    private List<Detail_img> list;
    private LayoutInflater layoutInflater;

    private Bitmap imgBit;
    public Detailimg_adapter(Context context, List<Detail_img> list) {
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
        Detail_img detail_img=list.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.detailimg_item,null);
            holder.img_1= (ImageView) view.findViewById(R.id.detailimg_item_img);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        Glide.with(context).load(detail_img.getImg_1()).into(holder.img_1);
        return view;
    }
    static class ViewHolder
    {
        ImageView img_1;

    }

}


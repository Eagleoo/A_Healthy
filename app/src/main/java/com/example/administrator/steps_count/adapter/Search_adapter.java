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
import com.example.administrator.steps_count.mall.Mall_Name;

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

public class Search_adapter extends BaseAdapter {
    private Context context;
    private List<Mall_Name> list;
    private LayoutInflater layoutInflater;

    private Bitmap imgBit;
    public Search_adapter(Context context, List<Mall_Name> list) {
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
        Mall_Name mall=list.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.mall_search_item,null);
            holder.mall_name=(TextView) view.findViewById(R.id.mall_search_item);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        holder.mall_name.setText(mall.getMall_name());
        return view;
    }
    static class ViewHolder
    {
        TextView mall_name;
    }
}


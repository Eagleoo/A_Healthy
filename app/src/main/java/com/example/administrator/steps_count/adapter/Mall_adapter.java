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
    private List<Mall> list;
    private LayoutInflater layoutInflater;

    private Bitmap imgBit;
    public Mall_adapter(Context context, List<Mall> list) {
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
        Mall mall=list.get(i);
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


    private Bitmap getimg(String url) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("url", url).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://192.168.1.111:8080/OutPutImg")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("failure", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();
                        imgBit=BitmapFactory.decodeStream(inputStream);

                }
            }
        });

        return imgBit;
    }
}


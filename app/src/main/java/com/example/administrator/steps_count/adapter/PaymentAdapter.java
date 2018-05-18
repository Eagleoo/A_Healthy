package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Order;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;

/**
 * Created by Administrator on 2018/5/14.
 */

public class PaymentAdapter extends BaseAdapter {
    private LinkedList<Order> mData;
    private Context context;

    public PaymentAdapter(LinkedList<Order> mData, Context context) {
        this.mData = mData;
        this.context = context;
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null)
        {
            view= LayoutInflater.from(context).inflate(R.layout.activity_check_payment,viewGroup,false);
            holder=new ViewHolder();
            holder.img= (ImageView) view.findViewById(R.id.imag);
            holder.name= (TextView) view.findViewById(R.id.name);
            holder.describe= (TextView) view.findViewById(R.id.describe);
            holder.price= (TextView) view.findViewById(R.id.price);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
        ImageLoader.getInstance().displayImage(mData.get(i).getMall_img(), holder.img);
        holder.name.setText(mData.get(i).getMall_name());
        holder.describe.setText(mData.get(i).getMall_describe());
        holder.price.setText(mData.get(i).getMall_price());
        return view;
    }
    static class ViewHolder
    {
       ImageView img;
       TextView name;
       TextView describe;
       TextView price;
    }
}


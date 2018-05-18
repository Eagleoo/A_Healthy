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
import com.example.administrator.steps_count.mall.Address;
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

public class Address_adapter extends BaseAdapter {
    private Context context;
    private List<Address> list;
    private LayoutInflater layoutInflater;

    private Bitmap imgBit;
    public Address_adapter(Context context, List<Address> list) {
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
        Address address=list.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.mall_address_item,null);
            holder.consigneer= (TextView) view.findViewById(R.id.address_item_consignee);
            holder.cellnumber=(TextView) view.findViewById(R.id.address_item_cellnumber);
            holder.address= (TextView) view.findViewById(R.id.address_item_address);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        holder.consigneer.setText(address.getConsigneer());
        holder.cellnumber.setText(address.getCellnumber());
        holder.address.setText(address.getAddress());
        return view;
    }
    static class ViewHolder
    {
        TextView consigneer;
        TextView cellnumber;
        TextView address;
    }

}


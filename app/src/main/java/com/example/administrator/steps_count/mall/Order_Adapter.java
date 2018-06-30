package com.example.administrator.steps_count.mall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;

import java.util.List;


public class Order_Adapter extends BaseAdapter {
    private Context context;
    private List<Orderandmall> list;
    private LayoutInflater layoutInflater;
    private Order_function orderFunction;
    public Order_Adapter(Context context, List<Orderandmall> list) {
        layoutInflater= LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }
    public void setOrderFunction(Order_function orderFunction){
        this.orderFunction = orderFunction;
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
        Orderandmall orderandmall=list.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.order_item,null);
            holder.mall_describe= (TextView) view.findViewById(R.id.mall_describe);
            holder.mall_price= (TextView) view.findViewById(R.id.mall_price);
            holder.mall_img= (ImageView) view.findViewById(R.id.mall_img);
            holder.mall_count= (TextView) view.findViewById(R.id.mall_count);
            holder.order_price= (TextView) view.findViewById(R.id.order_price);
            holder.looklogistics= (Button) view.findViewById(R.id.looklogistics);
            holder.operate= (Button) view.findViewById(R.id.operate);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        Glide.with(context).load(orderandmall.getMall_img()).into(holder.mall_img);
        holder.mall_describe.setText(orderandmall.getMall_describe());
        holder.mall_price.setText("￥"+orderandmall.getMall_price());
        holder.mall_count.setText("x"+orderandmall.getOrder_count());
        holder.order_price.setText("￥"+orderandmall.getOrder_allprice());
        holder.operate.setTag(i);
        if (orderandmall.getIspay().equals("F") && orderandmall.getIssend().equals("F") && orderandmall.getIsreceive().equals("F") )
        {
            holder.operate.setText("付款");
            holder.operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = Integer.parseInt(view.getTag().toString());
                    String order_id=list.get(index).getOrder_id();
                    Log.i("pay", order_id);
                    orderFunction.pay(order_id);
                }
            });
        }
        else if (orderandmall.getIspay().equals("T") && orderandmall.getIssend().equals("F") && orderandmall.getIsreceive().equals("F") )
        {
            holder.operate.setText("等待发货");

        }
        else if (orderandmall.getIspay().equals("T") && orderandmall.getIssend().equals("T") && orderandmall.getIsreceive().equals("F") )
        {
            holder.operate.setText("收货");
            holder.operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = Integer.parseInt(view.getTag().toString());
                    String order_id=list.get(index).getOrder_id();
                    Log.i("receive", order_id);
                    orderFunction.receive(order_id);
                }
            });

        }
        else if (orderandmall.getIspay().equals("T") && orderandmall.getIssend().equals("T") && orderandmall.getIsreceive().equals("T") )
        {
            holder.operate.setText("订单完成");
        }


        holder.looklogistics.setTag(i);
        holder.looklogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = Integer.parseInt(view.getTag().toString());
                String order_id=list.get(index).getOrder_id();
                Intent intent=new Intent(context,Logistics_Activity.class);
                Bundle bundle=new Bundle();
                bundle.putString("order_id",order_id);
                intent.putExtras(bundle);
                context.startActivity(intent);
                Log.i("order_id", order_id);
            }
        });
        return view;
    }
    static class ViewHolder
    {
        ImageView mall_img;
        TextView mall_describe;
        TextView mall_price;
        TextView mall_count;
        TextView order_price;
        Button looklogistics;
        Button operate;
    }

    public interface Order_function
    {
        void pay(String order_id);
        void receive(String order_id);
    }

}

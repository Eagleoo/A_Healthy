package com.example.administrator.steps_count.mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.steps_count.R;

import java.util.List;


public class Logistics_Adapter extends BaseAdapter {
    private Context context;
    private List<Logistics> list;
    private LayoutInflater layoutInflater;
    public Logistics_Adapter(Context context, List<Logistics> list) {
        layoutInflater= LayoutInflater.from(context);
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
        Logistics logistics=list.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.logistics_item,null);
            holder.l_add= (TextView) view.findViewById(R.id.logistics_add);
            holder.l_time=(TextView) view.findViewById(R.id.logistics_time);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }

        holder.l_time.setText(logistics.getL_time());
        holder.l_add.setText(logistics.getL_add());
        return view;
    }
    static class ViewHolder
    {
        TextView l_time;
        TextView l_add;
    }
}

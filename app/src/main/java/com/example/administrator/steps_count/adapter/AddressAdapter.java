package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Adress;

import java.util.LinkedList;

public class AddressAdapter extends BaseAdapter {
    private LinkedList<Adress> mDate;//适配器中要显示数据的列表
    private Context mContext;//上下文的环境
    //获取当前适配器要显示数据的总数
    public boolean flag = false;
    Adress adress = new Adress();

    public AddressAdapter(LinkedList<Adress> mDate, Context mContext) {
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    //获取显示数据在ListView上的位置
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position,
                        View convertView, ViewGroup parent) {
//优化后的getView适配器调用该方法时，只需在converView为空时创建一次
        ViewHolder holder = null;
        if (convertView == null) {

            //创建布局.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.addressadapter, parent, false);
            //获得布局上的组件
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.sender);
            holder.tel = (TextView) convertView.findViewById(R.id.tel);
            holder.district = (TextView) convertView.findViewById(R.id.district);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mDate.get(position).getSender());
        holder.tel.setText(mDate.get(position).getClienttel());
        holder.district.setText(mDate.get(position).getClientaddress());
       final Adress adress=mDate.get(position);
        if (adress!=null) {
            if (flag) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }
            holder.checkBox.setChecked(mDate.get(position).isIscheck());

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDate.get(position).isIscheck()) {

                        adress.ischeck = false;
                    } else {
                        adress.ischeck = true;
                    }

                }
            });
        }
        return convertView;
    }

    //静态内部类保存缓冲布局界面上用到的所有组件，以备其他该类对象共享
    static class ViewHolder {
        TextView name;
        TextView tel;
        TextView district;
        CheckBox checkBox;
    }
}

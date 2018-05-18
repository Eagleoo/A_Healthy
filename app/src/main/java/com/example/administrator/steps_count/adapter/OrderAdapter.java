package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Adress;
import com.example.administrator.steps_count.model.Order;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;

import static com.example.administrator.steps_count.R.id.context;
import static com.example.administrator.steps_count.R.id.price;

public class OrderAdapter extends BaseAdapter {
    private LinkedList<Order> mDate;//适配器中要显示数据的列表
    private Context mContext;//上下文的环境
    //获取当前适配器要显示数据的总数


    public OrderAdapter(LinkedList<Order> mDate, Context mContext) {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
//优化后的getView适配器调用该方法时，只需在converView为空时创建一次
        ViewHolder holder = null;
        if (convertView == null) {

            //创建布局.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_order, parent, false);
            //获得布局上的组件
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.imag);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.describe = (TextView) convertView.findViewById(R.id.describe);
            holder.price = (TextView) convertView.findViewById(price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
        ImageLoader.getInstance().init(configuration);
        ImageLoader.getInstance().displayImage(mDate.get(position).getMall_img(), holder.image);
        holder.name.setText(mDate.get(position).getMall_name());
        holder.describe.setText(mDate.get(position).getMall_describe());
        holder.price.setText(mDate.get(position).getMall_price());

        return convertView;
    }

    //静态内部类保存缓冲布局界面上用到的所有组件，以备其他该类对象共享
    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView describe;
        TextView price;

    }
}

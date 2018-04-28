package com.example.administrator.steps_count.mall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Mall_detail_adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2018/3/25.
 */

public class Mall_detail_Fragment extends Fragment {
    private List<Mall_detail_Bean> mall_detail_List=new ArrayList<Mall_detail_Bean>();
    private Mall_detail_adapter mall_detail_adapter;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mall_detail_fragment, container, false);

        listView= (ListView) view.findViewById(R.id.mall_detail_list);

        mall_detail_List=getData();
        mall_detail_adapter=new Mall_detail_adapter(getContext(),mall_detail_List);
        listView.setAdapter(mall_detail_adapter);
        return view;
    }

    public List<Mall_detail_Bean> getData()
    {
        List<Mall_detail_Bean> list=new ArrayList<Mall_detail_Bean>();
/*        String uname= MainActivity.user.getUsername();
        Cursor cursor=dbHelper.select_all(uname);
//        将cursor对象中的数据依次放入list中

        while (cursor.moveToNext())
        {
            int head_img=cursor.getInt(cursor.getColumnIndex("oimg"));
            String name=cursor.getString(cursor.getColumnIndex("oname"));
            String price=cursor.getString(cursor.getColumnIndex("oprice"));

            Order_Bean order_bean =new Order_Bean();
            order_bean.setHead_img(head_img);
            order_bean.setName(name);
            order_bean.setPrice(price);

            list.add(order_bean);
        }*/
        for(int i=0;i<10;i++) {
           Mall_detail_Bean mall_detail_bean=new Mall_detail_Bean();
           mall_detail_bean.setMall_detail_img(R.drawable.appfirstimg);
           list.add(mall_detail_bean);
        }

        return list;
    }
}

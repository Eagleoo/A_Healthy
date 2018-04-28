package com.example.administrator.steps_count.mall;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Mall_adapter;
import com.example.administrator.steps_count.adapter.Mall_detail_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by PC on 2018/3/24.
 */

public class Mall_Shop_Fragment extends Fragment implements My_Scrollview.OnScrollChangeListener {
    private List<Mall> mall_list=new ArrayList<Mall>();
    private String mall_id;
    private ImageView shop_head;
    private TextView mall_shop_shopname;
    private TextView mall_shop_shopdescribe;
    private TextView mall_shop_shopprice;
    private My_Scrollview mall_shop_frag_scroll;
    private Mall_Detail_Activity mall_detail_activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mall_shop_fragment, container, false);
        mall_shop_frag_scroll= (My_Scrollview) view.findViewById(R.id.mall_shop_frag_scroll);
        mall_shop_frag_scroll.setOnScrollChangeListener(this);

        shop_head= (ImageView) view.findViewById(R.id.shop_head);
        mall_shop_shopname= (TextView) view.findViewById(R.id.mall_shop_shopname);
        mall_shop_shopdescribe= (TextView) view.findViewById(R.id.mall_shop_shopdescribe);
        mall_shop_shopprice= (TextView) view.findViewById(R.id.mall_shop_shopprice);


        mall_id=getActivity().getIntent().getExtras().getString("0x0");


        getDataAsync();


        return view;

    }

    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("action",mall_id).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://192.168.1.111:8080/Mall_Detail_Servlet")
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
                    final String json = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mall_list=getMall("mall",json);
                            Glide.with(getContext()).load(mall_list.get(0).getMall_img()).into(shop_head);
                            mall_shop_shopname.setText(mall_list.get(0).getMall_name());
                            mall_shop_shopdescribe.setText(mall_list.get(0).getMall_describe());
                            mall_shop_shopprice.setText(mall_list.get(0).getMall_price());
                        }
                    });
                }
            }
        });
    }

    private static List<Mall> getMall(String key, String jsonString) {
        List<Mall> list = new ArrayList<Mall>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Mall mall = new Mall();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                mall.setMall_id(jsonObject2.getString("mall_id"));
                mall.setMall_name(jsonObject2.getString("mall_name"));
                mall.setMall_describe(jsonObject2.getString("mall_describe"));
                mall.setMall_price(jsonObject2.getString("mall_price"));
                mall.setMall_img(jsonObject2.getString("mall_img"));
                mall.setMall_detail_img(jsonObject2.getString("mall_detail_img"));
                mall.setMall_type(jsonObject2.getString("mall_type"));
                list.add(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void onScrollChanged(My_Scrollview scrollView, int x, int y, int oldx, int oldy) {

        if (y>0)
        {
            mall_detail_activity=new Mall_Detail_Activity();
        }
    }

}
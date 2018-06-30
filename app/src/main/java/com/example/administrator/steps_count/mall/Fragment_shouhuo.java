package com.example.administrator.steps_count.mall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
 * Created by 59476 on 2017/12/12.
 */

public class Fragment_shouhuo extends Fragment implements Order_Adapter.Order_function{
    private List<Orderandmall> list=new ArrayList<>();
    private ListView listview_fukuan;
    private Order_Adapter order_adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fukuan, container, false);
        listview_fukuan= (ListView) view.findViewById(R.id.listview_fukuan);
        getDataAsync();
        return view;
    }
    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", Frag_MainActivity.user.getUsername())
                .add("state","shouhuo")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Selectorderbystate_Servlet")
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
                            list=getMall("shouhuo",json);
                            order_adapter = new Order_Adapter(getContext(),list);
                            listview_fukuan.setAdapter(order_adapter);
                            order_adapter.setOrderFunction(Fragment_shouhuo.this);
                        }
                    });
                }
            }
        });
    }
    private static List<Orderandmall> getMall(String key, String jsonString) {
        List<Orderandmall> list = new ArrayList<Orderandmall>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Orderandmall orderandmall = new Orderandmall();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                orderandmall.setOrder_id(jsonObject2.getString("order_id"));
                orderandmall.setMall_describe(jsonObject2.getString("mall_describe"));
                orderandmall.setMall_price(jsonObject2.getString("mall_price"));
                orderandmall.setMall_img(jsonObject2.getString("mall_img"));
                orderandmall.setOrder_count(jsonObject2.getString("order_count"));
                orderandmall.setOrder_allprice(jsonObject2.getString("order_allprice"));
                orderandmall.setIspay(jsonObject2.getString("ispay"));
                orderandmall.setIssend(jsonObject2.getString("issend"));
                orderandmall.setIsreceive(jsonObject2.getString("isreceive"));
                list.add(orderandmall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void pay(String order_id) {

    }

    @Override
    public void receive(String order_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("order_id", order_id)
                .add("dowhat","updatereceive")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Updateorderstate_Servlet")
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
                            Toast.makeText(getContext(),json, Toast.LENGTH_SHORT).show();
                            getDataAsync();
                        }
                    });
                }
            }
        });
    }
}

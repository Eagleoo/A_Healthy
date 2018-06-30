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

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Detailimg_adapter;
import com.example.administrator.steps_count.fragment.MallFragment;

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
 * Created by PC on 2018/3/25.
 */

public class Mall_detail_Fragment extends Fragment {
    private ListView mall_detail_list;
    private Detailimg_adapter adapter;
    private List<Detail_img> detailImgs_list=new ArrayList<Detail_img>();
    private ImageView mall_detail_img;
    private String mall_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mall_detail_fragment, container, false);
        mall_detail_list= (ListView) view.findViewById(R.id.mall_detail_list);
        mall_id=getActivity().getIntent().getExtras().getString("0x0");
        getDataAsync();
        return view;
    }
    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("mall_id",mall_id).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url(MallFragment.user.getUrl()+"Fordetailimg_Servlet")
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
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detailImgs_list=getMall("detail_img",json);
                                adapter=new Detailimg_adapter(getContext(),detailImgs_list);
                                mall_detail_list.setAdapter(adapter);
                            }
                        });
                    }

                }
            }
        });
    }

    private static List<Detail_img> getMall(String key, String jsonString) {
        List<Detail_img> list = new ArrayList<Detail_img>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Detail_img detail_img = new Detail_img();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                detail_img.setImg_1(jsonObject2.getString("img_1"));
                list.add(detail_img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}

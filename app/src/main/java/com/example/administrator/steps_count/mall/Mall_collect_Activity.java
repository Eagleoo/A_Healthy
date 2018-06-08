package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Mall_adapter;
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
 * Created by PC on 2018/5/29.
 */

public class Mall_collect_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Mall> MallList;
    private Mall_adapter mall_adapter;
    private GridView mall_collect_gridview;
    private String username=MallFragment.user.getUsername();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_collect_layout);
        mall_collect_gridview= (GridView) findViewById(R.id.mall_collect_gridview);
        mall_collect_gridview.setOnItemClickListener(this);
        getDataAsync();
    }
    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url(MallFragment.user.getUrl()+"Selectmallbycollect_Servlet")
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MallList=getMall("mall",json);
                            mall_adapter = new Mall_adapter(getApplicationContext(),MallList);
                            mall_collect_gridview.setAdapter(mall_adapter);
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
                mall.setMall_price(jsonObject2.getString("mall_price"));
                mall.setMall_img(jsonObject2.getString("mall_img"));
                list.add(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String id=MallList.get(i).getMall_id();
        Bundle bundle=new Bundle();
        bundle.putString("0x0",id);
        Log.i("mall_id", id);
        Intent intent = new Intent(getApplicationContext(),Mall_Detail_Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

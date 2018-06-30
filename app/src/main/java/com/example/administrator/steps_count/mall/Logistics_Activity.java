package com.example.administrator.steps_count.mall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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
 * Created by PC on 2018/6/21.
 */

public class Logistics_Activity extends AppCompatActivity {
    private ListView logistics_listview;
    private Bundle bundle;
    private String order_id;
    private List<Logistics> list;
    private Logistics_Adapter logistics_adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_layout);

        bundle=new Bundle();
        order_id=this.getIntent().getExtras().getString("order_id");
        logistics_listview= (ListView) findViewById(R.id.logistics_listview);
        Log.i("order", order_id);

        getlogistics();
    }

    private void getlogistics() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("order_id", order_id)
                .add("roof","android" ).build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Selectlogistics_Servlet")
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
                            list=getdata("logistics",json);
                            logistics_adapter = new Logistics_Adapter(getApplicationContext(),list);
                            logistics_listview.setAdapter(logistics_adapter);
                        }
                    });
                }
            }
        });
    }
    private static List<Logistics> getdata(String key, String jsonString) {
        List<Logistics> list = new ArrayList<Logistics>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Logistics logistics = new Logistics();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                logistics.setL_add(jsonObject2.getString("l_add"));
                logistics.setL_time(jsonObject2.getString("l_time"));
                list.add(logistics);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}

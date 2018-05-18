package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.steps_count.Activity.AboutActivity;
import com.example.administrator.steps_count.Activity.ActivityControl;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Address_adapter;
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
 * Created by PC on 2018/5/9.
 */

public class Mall_Address_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mall_address_list;
    private List<Address> list=new ArrayList<Address>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_address_layout);
        mall_address_list= (ListView) findViewById(R.id.mall_address_list);
        getAddr();
        mall_address_list.setOnItemClickListener(this);
    }


    private void getAddr() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", "lzy").build();
        final Request request = new Request.Builder()
                .url(MallFragment.user.getUrl()+"selectaddr_Servlet")
                .post(requestBody)
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
                            list=getAddress("address",json);
                            Address_adapter address_adapter = new Address_adapter(getApplicationContext(),list);
                            mall_address_list.setAdapter(address_adapter);
                        }
                    });
                }
            }
        });
    }

    private static List<Address> getAddress(String key, String jsonString) {
        List<Address> list = new ArrayList<Address>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Address address = new Address();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                address.setConsigneer(jsonObject2.getString("consigneer"));
                address.setCellnumber(jsonObject2.getString("cellnumber"));
                address.setAddress(jsonObject2.getString("address"));
                list.add(address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String consigneer=list.get(i).getConsigneer();
        String address=list.get(i).getAddress();
        String cellnumber=list.get(i).getCellnumber();
        Intent data=new Intent();
        data.putExtra("data1",consigneer);
        data.putExtra("data2",address);
        data.putExtra("data3",cellnumber);
        setResult(0x0003,data);
        finish();
    }
}

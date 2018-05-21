package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.OrderAdapter;
import com.example.administrator.steps_count.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private List<Order> list=new LinkedList<Order>();
    private OrderAdapter orderAdapter;
    private Context context;
    private ListView listView;

Handler handler=new Handler()
{
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        try {
            JSONArray jsonArray=new JSONArray(msg.obj.toString());
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=null;
                jsonObject=jsonArray.getJSONObject(i);
                Order order=new Order();
                order.setMall_img(jsonObject.get("mall_img").toString());
                order.setMall_name(jsonObject.get("mall_name").toString());
                order.setMall_describe(jsonObject.get("mall_describe").toString());
                order.setMall_price(jsonObject.get("mall_price").toString());
                list.add(order);

            }
            context=OrderActivity.this;
            orderAdapter=new OrderAdapter((LinkedList<Order>) list,context);
            listView.setAdapter(orderAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlistview);
        listView= (ListView) findViewById(R.id.orderlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(OrderActivity.this,OrderInformation.class);
                intent.putExtra("img",list.get(i).getMall_img());
                intent.putExtra("name",list.get(i).getMall_name());
                intent.putExtra("describe",list.get(i).getMall_describe());
                intent.putExtra("price",list.get(i).getMall_price());
                startActivity(intent);
}
        });
        String address="http://"+Frag_MainActivity.localhost+":8080/Health/servlet/SelectOrder?username="+Frag_MainActivity.user.getUsername();
        ReadURL(address);
    }
    public void ReadURL(final String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resultCode = connection.getResponseCode();
                    StringBuffer response = null;
                    if (HttpURLConnection.HTTP_OK == resultCode) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        response = new StringBuffer();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }

                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "1";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);
                super.onPostExecute(s);
            }


            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);
    }
}

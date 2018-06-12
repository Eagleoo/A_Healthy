package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.OrderAdapter;
import com.example.administrator.steps_count.model.Order;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import java.util.LinkedList;
import java.util.List;

public class NewMall extends AppCompatActivity {
    private BaseAdapter baseAdapter;
    private List<Order> list=new LinkedList<Order>();
    private ListView listView;
    private Context context;
private Handler handler=new Handler()
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
            context=NewMall.this;

            listView.setAdapter(baseAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mall);
        listView= (ListView) findViewById(R.id.newlistmall);
        String url="http://"+Frag_MainActivity.localhost+":8080/circle/servlet/New?num=2";
        ReadURL(url);
        baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder holder=null;
                if(view==null)
                {
                    holder=new ViewHolder();
                    view= LayoutInflater.from(context).inflate(R.layout.activity_order,viewGroup,false);
                    holder.imag= (ImageView) view.findViewById(R.id.imag);
                    holder.name= (TextView) view.findViewById(R.id.name);
                    holder.describle= (TextView) view.findViewById(R.id.describe);
                    holder.price= (TextView) view.findViewById(R.id.price);
                    view.setTag(holder);
                }else
                {
                    holder= (ViewHolder) view.getTag();
                }

                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(list.get(i).getMall_img(), holder.imag);
               holder.name.setText(list.get(i).getMall_name());
               holder.describle.setText(list.get(i).getMall_describe());
               holder.price.setText(list.get(i).getMall_price());
                return view;
            }
        };
    }
    static class ViewHolder
    {
        ImageView imag;
        TextView name;
        TextView describle;
        TextView price;
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

package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Order;
import com.example.administrator.steps_count.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderInformation extends AppCompatActivity {
    private ImageView imageView;
    private TextView name;
    private TextView describe;
    private TextView price;
   private TextView receiver;
   private TextView tel;
   private TextView district;
   private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        gson=new Gson();
        imageView = (ImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        describe = (TextView) findViewById(R.id.describe);
        price = (TextView) findViewById(R.id.price);
        receiver= (TextView) findViewById(R.id.receiver);
        tel= (TextView) findViewById(R.id.tel);
        district= (TextView) findViewById(R.id.district);
        Intent intent = getIntent();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(OrderInformation.this);
        ImageLoader.getInstance().init(configuration);
        ImageLoader.getInstance().displayImage(intent.getStringExtra("img"), imageView);
        name.setText(intent.getStringExtra("name"));
        describe.setText(intent.getStringExtra("describe"));
        price.setText(intent.getStringExtra("price"));
        String address = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/SelectOrder?function=show&order_id=" + Integer.parseInt(intent.getStringExtra("order_id"));
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
                String result = s;
                ArrayList<Order> list = new ArrayList<Order>();
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                list = gson.fromJson(result, listType);
                receiver.setText(list.get(0).getConsigee());
                tel.setText(list.get(0).getCellnumber());
                district.setText(list.get(0).getAddress());


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

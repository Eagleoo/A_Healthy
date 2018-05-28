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
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.OrderAdapter;
import com.example.administrator.steps_count.model.Dynamics;
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

public class NewDynamic extends AppCompatActivity {

    private List<Dynamics> list=new LinkedList<Dynamics>();
    private ListView listView;
    private Context context;
    private BaseAdapter baseAdapter;
    private List<Dynamics> dynamicsList=new LinkedList<>();
    Handler mhandlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Dynamics dynamics=new Dynamics();
                    dynamics.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    dynamics.setTitle(jsonObject.get("title").toString());
                    dynamics.setContent(jsonObject.get("content").toString());
                    dynamics.setImag(jsonObject.get("imag").toString());
                    dynamicsList.add(dynamics);


                }
                context =NewDynamic.this;
                listView.setAdapter(baseAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dynamic);
        listView= (ListView) findViewById(R.id.newdynamic);
        String url="http://"+Frag_MainActivity.localhost+":8080/Health/servlet/Dynamic?function=newdynamic";;
        ReadURL(url);
        baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return dynamicsList.size();
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
                ViewHolder viewHolder=null;
                if(view==null)
                {
                    viewHolder=new ViewHolder();
                    view=LayoutInflater.from(context).inflate(R.layout.dynamic_layout,viewGroup,false);
                    viewHolder=new ViewHolder();
                    viewHolder.imageView= (ImageView) view.findViewById(R.id.dynamicimag);
                    viewHolder.title= (TextView) view.findViewById(R.id.dynamicname);
                    view.setTag(viewHolder);
                }else {
                    viewHolder= (ViewHolder) view.getTag();
                }
                viewHolder.title.setText(dynamicsList.get(i).getTitle());
                ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(dynamicsList.get(i).getImag(),viewHolder.imageView);
                return view;
            }
        };

    }
    static  class ViewHolder
    {
        ImageView imageView;
        TextView title;
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
                mhandlers.sendMessage(message);
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

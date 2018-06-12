package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Circle;
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

public class PerCollect extends AppCompatActivity {
    private ListView listView;
    private Context context;
    private BaseAdapter baseAdapter;
    private List<Circle> list = new LinkedList<Circle>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray=new JSONArray(msg.obj.toString());
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=null;
                    jsonObject=jsonArray.getJSONObject(i);
                    Circle circle=new Circle();
                    circle.setImag(jsonObject.get("imag").toString());
                    circle.setTitle(jsonObject.get("title").toString());
                    circle.setContent(jsonObject.get("content").toString());
                    list.add(circle);

                }

                context=PerCollect.this;
                listView.setAdapter(baseAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_collect);
        listView = (ListView) findViewById(R.id.collectlist);
        String url = "http://"+Frag_MainActivity.localhost+":8080/circle/servlet/SelectConllect?username="+Frag_MainActivity.user.getUsername();
       ReadURL(url);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent=new Intent(PerCollect.this,CollectShow.class);
               intent.putExtra("title",list.get(i).getTitle());
               intent.putExtra("content",list.get(i).getContent());
               startActivity(intent);
           }
       });
        baseAdapter = new BaseAdapter() {
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
                ViewHolder holder = null;
                if (view == null) {
                    holder=new ViewHolder();
                    view = LayoutInflater.from(context).inflate(R.layout.conllect, viewGroup, false);
                    holder.imag = (ImageView) view.findViewById(R.id.coimag);
                    holder.title = (TextView) view.findViewById(R.id.title);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(list.get(i).getImag(), holder.imag);
                holder.title.setText(list.get(i).getTitle());
                return view;
            }
        };

    }

    static class ViewHolder {
        ImageView imag;
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

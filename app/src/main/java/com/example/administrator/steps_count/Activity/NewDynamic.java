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
                    dynamics.setTime(jsonObject.get("title").toString());
                    dynamics.setContent(jsonObject.get("content").toString());
                    dynamics.setAuthor(jsonObject.get("author").toString());
                    dynamics.setImg(jsonObject.getString("describe").toString());
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
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Intent intent=new Intent(NewDynamic.this, ShowDynamic.class);
              intent.putExtra("dyname",dynamicsList.get(i).getAuthor());
              intent.putExtra("dycontent",dynamicsList.get(i).getContent());
              intent.putExtra("dyid",String.valueOf(dynamicsList.get(i).getId()));

              startActivity(intent);
          }
      });
        String url="http://"+Frag_MainActivity.localhost+":8080/circle/servlet/Dynamic?function=newdynamic";;
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
                    viewHolder.describle= (TextView) view.findViewById(R.id.describle);
                    viewHolder.author= (TextView) view.findViewById(R.id.describe);
                    viewHolder.title= (TextView) view.findViewById(R.id.dynamicname);
                    view.setTag(viewHolder);
                }else {
                    viewHolder= (ViewHolder) view.getTag();
                }
                viewHolder.title.setText(dynamicsList.get(i).getAuthor());
                viewHolder.describle.setText(dynamicsList.get(i).getContent());
                viewHolder.author.setText(dynamicsList.get(i).getAuthor());
                return view;
            }
        };

    }
    static  class ViewHolder
    {
        TextView title;
        TextView describle;
        TextView author;
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

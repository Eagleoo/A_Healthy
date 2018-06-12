package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Respones;

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

public class ReplyActivity extends AppCompatActivity {
    private ListView reply;
    private List<Respones> respones = new LinkedList<Respones>();
    private int position = 0;
    private BaseAdapter baseAdapter;
    private Context context;
    private String username;
    private Intent intent;
    private int id=0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Respones reply = new Respones();
                    reply.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    reply.setReviewid(Integer.parseInt(jsonObject.get("reviewid").toString()));
                    reply.setName(jsonObject.get("responsename").toString());
                    reply.setContent(jsonObject.get("responsecon").toString());
                    respones.add(reply);


                }

                context = ReplyActivity.this;

                reply.setAdapter(baseAdapter);
                registerForContextMenu(reply);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        reply = (ListView) findViewById(R.id.replyadater);
        reply.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                return false;
            }
        });
        intent = getIntent();
        id=Integer.parseInt(intent.getStringExtra("id"));
        String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Dynamic?function=response&reviewid=" +id;
        ReadURL(url);
        username = intent.getStringExtra("name");
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return respones.size();
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
                    view = LayoutInflater.from(context).inflate(R.layout.responseadapter, viewGroup, false);
                    holder = new ViewHolder();
                    holder.replyname = (TextView) view.findViewById(R.id.responsename);
                    holder.replycont = (TextView) view.findViewById(R.id.responsecontent);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                holder.replyname.setText(respones.get(i).getName());
                holder.replycont.setText(respones.get(i).getContent());
                return view;
            }
        };

    }

    static class ViewHolder {
        TextView replyname;
        TextView replycont;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.delete_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_delete:
                if (Frag_MainActivity.user == null) {
                    Toast.makeText(ReplyActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                } else if (Frag_MainActivity.user.getUsername().equals(respones.get(position).getName())) {

                    String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Dynamic?function=delete&name=" + respones.get(position).getName()
                            + "&content=" + respones.get(position).getContent() + "&reviewid=" + respones.get(position).getReviewid();
                    DeleteURL(url);
                } else {
                    Toast.makeText(ReplyActivity.this, "你不能删除此项", Toast.LENGTH_LONG).show();
                }
                break;

        }
        return super.onContextItemSelected(item);
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
                super.onPostExecute(s);
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);


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

    public void DeleteURL(final String url) {
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
               Toast.makeText(ReplyActivity.this,"删除成功",Toast.LENGTH_LONG).show();

              finish();
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

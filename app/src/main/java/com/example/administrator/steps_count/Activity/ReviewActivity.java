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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Respones;
import com.example.administrator.steps_count.model.Review;
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

import static com.example.administrator.steps_count.R.id.discuss;



public class ReviewActivity extends AppCompatActivity {
    private ListView relist;
    private List<Review> reviews = new LinkedList<Review>();
    private LinearLayout linearLayout;
    private Button btn;
    private EditText edt;
    private BaseAdapter baseAdapter;
    private Context context;
    private int position = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Review review = new Review();
                    review.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    review.setContent(jsonObject.get("content").toString());
                    review.setUsername(jsonObject.get("username").toString());
                    review.setImag(jsonObject.get("imag").toString());
                    review.setConsult_id(Integer.parseInt(jsonObject.get("consult_id").toString()));
                    reviews.add(review);
                }

                context = ReviewActivity.this;
                relist.setAdapter(baseAdapter);
                registerForContextMenu(relist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        linearLayout = (LinearLayout) findViewById(R.id.res);
        btn = (Button) findViewById(R.id.btn_reply);
        edt = (EditText) findViewById(R.id.replycon);
        relist = (ListView) findViewById(discuss);
        Intent intent = getIntent();
        String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Dynamic?function=review&consult_id="
                + Integer.parseInt(intent.getStringExtra("dyid"));
        ReadURL(url);
        relist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;


                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Frag_MainActivity.user==null)
                {
                    Toast.makeText(ReviewActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                }else
                {
                    String content = edt.getText().toString().trim();
                    if (content.equals(""))
                    {
                        Toast.makeText(ReviewActivity.this,"请输入内容",Toast.LENGTH_LONG).show();
                    }else {
                        String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Dynamic?function=discuss&username=" + reviews.get(position).getUsername()
                                + "&name=" + Frag_MainActivity.user.getUsername() + "&content=" + content + "&reviewid=" + reviews.get(position).getId();
                        InserURL(url);
                    }
                }

            }
        });
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return reviews.size();
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
            public View getView(final int i, View view, ViewGroup viewGroup) {
                ViewHolder holder = null;
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.reviewlist, viewGroup, false);
                    holder = new ViewHolder();
                    holder.imag = (ImageView) view.findViewById(R.id.reimag);
                    holder.content = (TextView) view.findViewById(R.id.recontent);
                    holder.name = (TextView) view.findViewById(R.id.reusername);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                holder.name.setText(reviews.get(i).getUsername());
                holder.content.setText(reviews.get(i).getContent());
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(reviews.get(i).getImag(), holder.imag);

                return view;
            }
        };

    }

    static class ViewHolder {
        ImageView imag;
        TextView name;
        TextView content;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.rev_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reply:
                if (Frag_MainActivity.user==null)
                {
                    Toast.makeText(ReviewActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.delete:
                if (Frag_MainActivity.user==null)
                {
                    Toast.makeText(ReviewActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                }else if(Frag_MainActivity.user.getUsername().equals(reviews.get(position).getUsername()))
            {
                String url="http://"+Frag_MainActivity.localhost+":8080/circle/servlet/Dynamic?function=deletereview&name="+reviews.get(position).getUsername()+
                        "&content="+reviews.get(position).getContent()+"&consult_id="+reviews.get(position).getConsult_id();
                DeleteURL(url);
            }else
                {
                    Toast.makeText(ReviewActivity.this, "你不能删除此项", Toast.LENGTH_LONG).show();
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

    public void InserURL(final String url) {
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
                Intent intent = new Intent(ReviewActivity.this, ReplyActivity.class);
                intent.putExtra("id", String.valueOf(reviews.get(position).getId()));
                intent.putExtra("name",reviews.get(position).getUsername());
                startActivity(intent);


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
              Toast.makeText(ReviewActivity.this,"删除成功",Toast.LENGTH_LONG).show();
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

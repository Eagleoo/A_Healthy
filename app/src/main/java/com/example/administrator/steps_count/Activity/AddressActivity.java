package com.example.administrator.steps_count.Activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.AddressAdapter;
import com.example.administrator.steps_count.model.Adress;
import com.example.administrator.steps_count.model.User;
import com.example.administrator.steps_count.tools.Text;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Button insert;
    private List<Adress> list = new LinkedList<Adress>();
    private AddressAdapter addressAdapter;
    private Context context;
    private ListView listView;
    private String id;
    private String sender;
    private String tel;
    private String district;
    private TextView control;
    private CheckBox allcheck;
    private LinearLayout linear;
    private TextView delete;
    private AlertDialog.Builder builder=null;
    private AlertDialog alertDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Adress adress = new Adress();
                    adress.setId(jsonObject.get("id").toString());
                    adress.setSender((jsonObject.get("sender")).toString());
                    adress.setClienttel((jsonObject.get("clienttel")).toString());
                    adress.setClientaddress((jsonObject.get("clientaddress")).toString());
                    list.add(adress);

                }
                context = AddressActivity.this;
                addressAdapter = new AddressAdapter((LinkedList<Adress>) list, context);
                listView.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
                registerForContextMenu(listView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        listView = (ListView) findViewById(R.id.addresslist);
        allcheck = (CheckBox) findViewById(R.id.allcheck);
        control = (TextView) findViewById(R.id.control);
        linear = (LinearLayout) findViewById(R.id.linear);
        delete = (TextView) findViewById(R.id.delete);
        allcheck.setOnCheckedChangeListener(this);
        if (Frag_MainActivity.user != null) {
            String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/AdressQuery?name=" + Frag_MainActivity.user.getUsername();
            QueryReadURL(url);
        }

        insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maAddress = new Intent(AddressActivity.this, InsertAddress.class);
                startActivity(maAddress);
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                sender = list.get(position).getSender();
                tel = list.get(position).getClienttel();
                district = list.get(position).getClientaddress();
                String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/SelectId?sender=" + sender
                        + "&tel=" + tel + "&address=" + district;
                IdReadURL(url);
                return false;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> ids = new LinkedList<>();

                if (addressAdapter.flag) {

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).ischeck) {
                            ids.add(list.get(i).getId());

                        }
                    }
                builder = new AlertDialog.Builder(AddressActivity.this);
                alertDialog = builder.setIcon(R.drawable.pe)
                        .setMessage("确认删除?")
                        .setTitle("系统提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<String> ids = new LinkedList<>();

                                if (addressAdapter.flag) {

                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).ischeck) {
                                            ids.add(list.get(i).getId());
                                        }
                                    }
                                    String url ="http://"+Frag_MainActivity.localhost+":8080/circle/servlet/AddressDelete?id="+ids.toString();
                                    DeleteReadURL(url);
                                    finish();


                                }
                            }
                        })
                        .setNegativeButton("取消", null)

                        .create();
                alertDialog.show();


                }
            }
        });
    }


    public void btnEditList(View view) {

        addressAdapter.flag = !addressAdapter.flag;

        if (addressAdapter.flag) {
            control.setText("完成");
            linear.setVisibility(View.VISIBLE);
        } else {
            control.setText("管理");
            linear.setVisibility(View.GONE);
        }

        addressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (compoundButton.isChecked()) {
            if (addressAdapter.flag) {

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).ischeck = true;

                }
                addressAdapter.notifyDataSetChanged();
            }
        } else {
            if (addressAdapter.flag) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).ischeck = false;
                }

                addressAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_update:
                Intent intent = new Intent(AddressActivity.this, UpdateAddress.class);
                intent.putExtra("id", id);
                intent.putExtra("sender", sender);
                intent.putExtra("tel", tel);
                intent.putExtra("district", district);
                startActivity(intent);
                break;

        }
        return super.onContextItemSelected(item);
    }


    public void QueryReadURL(final String url) {
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

    public void IdReadURL(final String address) {
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
                id = s;
            }


            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(address);


    }
    public void DeleteReadURL(final String address) {
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
                Intent intent=new Intent(AddressActivity.this,AddressActivity.class);
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
        }.execute(address);


    }
}

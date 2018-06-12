package com.example.administrator.steps_count.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateAddress extends AppCompatActivity {
    private TextView sender;
    private TextView telephone;
    private TextView location;
    private String id;
    private String addresser;
    private String tel;
    private String district;
    private Button sure;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        sender = (TextView) findViewById(R.id.sender);
        telephone = (TextView) findViewById(R.id.telephone);
        location = (TextView) findViewById(R.id.location);
        sure = (Button) findViewById(R.id.sure);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        addresser = intent.getStringExtra("sender");
        tel = intent.getStringExtra("tel");
        district = intent.getStringExtra("district");
        sender.setText(addresser);
        telephone.setText(tel);
        location.setText(district);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(UpdateAddress.this);
                alertDialog = builder.setIcon(R.drawable.pe)
                        .setMessage("确认修改?")
                        .setTitle("系统提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/AddressUpdate?id=" + id
                                        + "&sender=" + sender.getText().toString()
                                        + "&tel=" + telephone.getText().toString()
                                        + "&address=" + location.getText().toString();
                                ReadURL(url);
                            }
                        })
                        .setNegativeButton("取消", null)

                        .create();
                alertDialog.show();

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
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
                Toast.makeText(UpdateAddress.this,"修改成功！",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(UpdateAddress.this,AddressActivity.class);
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

}

package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
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

public class CotentActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView title;
    private TextView content;
    private Switch aSwitch;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.context);
        aSwitch = (Switch) findViewById(R.id.switchcon);
        aSwitch.setOnCheckedChangeListener(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        if (Frag_MainActivity.user != null) {
            String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Colletction?id=" + id + "&username=" + Frag_MainActivity.user.getUsername();
            ReadURL(url);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (aSwitch.isChecked()) {
            if (Frag_MainActivity.user == null) {
                Toast.makeText(CotentActivity.this, "请先登录账号", Toast.LENGTH_LONG).show();
                aSwitch.setChecked(false);
            } else {

                String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Collectdy?id=" + id + "&username=" + Frag_MainActivity.user.getUsername();
                ReadURL(url);
            }

        } else {
            String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/CancelCon?id=" + id + "&username=" + Frag_MainActivity.user.getUsername();
            ReadURL(url);
        }
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
                if (s.equals("true")) {
                    aSwitch.setChecked(true);
                }
                if (s.equals("2")) {

                    Toast.makeText(CotentActivity.this, "已收藏", Toast.LENGTH_LONG).show();
                } else if (s.equals("1")) {
                    Toast.makeText(CotentActivity.this, "取消收藏", Toast.LENGTH_LONG).show();
                }


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

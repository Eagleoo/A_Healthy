package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QQBaiduRegister extends AppCompatActivity {
private Button sure;
    private EditText tall;
    private EditText weight;
    private EditText age;
    private Gson gson=new Gson();
  private User users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqbaidu_register);
        sure= (Button) findViewById(R.id.sure);
        tall = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
Intent intent=getIntent();
users= (User) intent.getSerializableExtra("user");

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = "http://"+Frag_MainActivity.localhost+":8080/circle/servlet/ClientUpdate?user_id="+users.getUser_id()
                        + "&user_name=" + users.getUsername()
                        + "&user_sex=" + users.getSex()
                        + "&user_tall=" + tall.getText().toString()
                        + "&user_weight=" + weight.getText().toString()
                        + "&user_age=" + age.getText().toString();

                ReadURL(address);

            }
        });


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

                if (!s.equals("")) {
                    User user =gson.fromJson(s,User.class);
                    Intent intent=new Intent(QQBaiduRegister.this,Frag_MainActivity.class);
                    intent.putExtra("user",user);

                    startActivity(intent);

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

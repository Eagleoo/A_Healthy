package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.MainFragment;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateMsgActivity extends AppCompatActivity {
    private Button btn_update;
    private Button btn_exit;
    private EditText username;
    private EditText sex;
    private EditText tall;
    private EditText weight;
    private EditText age;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_msg);
        btn_update = (Button) findViewById(R.id.update);
        username = (EditText) findViewById(R.id.username);
        sex = (EditText) findViewById(R.id.sex);
        tall = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);

        gson=new Gson();
        btn_exit = (Button) findViewById(R.id.exit);
        if (Frag_MainActivity.user != null) {
            username.setText(Frag_MainActivity.user.getUsername());
            sex.setText(Frag_MainActivity.user.getSex());
            tall.setText(String.valueOf(Frag_MainActivity.user.getUser_tall()));
            weight.setText(String.valueOf(Frag_MainActivity.user.getUser_weight()));
            age.setText(String.valueOf(Frag_MainActivity.user.getUser_age()));

        }
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "http://"+Frag_MainActivity.localhost+":8080/circle/servlet/ClientUpdate?function=update&user_id="+Frag_MainActivity.user.getUser_id()
                        + "&user_name=" + username.getText().toString()
                        + "&user_sex=" + sex.getText().toString()
                        + "&user_tall=" + tall.getText().toString()
                        + "&user_weight=" + weight.getText().toString()
                        + "&user_age=" + age.getText().toString();

                ReadURL(address);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    Toast.makeText(UpdateMsgActivity.this,"修改信息成功！",Toast.LENGTH_LONG).show();
                    User user =gson.fromJson(s,User.class);
                    Intent intent = new Intent(UpdateMsgActivity.this,Frag_MainActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("name",user.getUsername());
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

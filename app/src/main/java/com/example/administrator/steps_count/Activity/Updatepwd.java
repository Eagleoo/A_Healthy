package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.steps_count.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Updatepwd extends AppCompatActivity {
    private Button ensure;
    private EditText oldpwd;
    private EditText newpwd;
    private EditText surepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);
        oldpwd = (EditText) findViewById(R.id.oldpwd);
        newpwd = (EditText) findViewById(R.id.newpwd);
        surepwd = (EditText) findViewById(R.id.surepwd);
        ensure = (Button) findViewById(R.id.sure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newpwd.getText().toString().equals(surepwd.getText().toString())) {
                    Toast.makeText(Updatepwd.this, "请输入相同的密码！", Toast.LENGTH_LONG).show();
                } else {
                    String address = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/UpdatePwd?"
                            + "user_id=" + Frag_MainActivity.user.getUser_id()
                            + "&oldpassword=" + oldpwd.getText().toString()
                            + "&newpassword=" + newpwd.getText().toString();
                    ReadURL(address);

                }
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
                if (!s.equals("fall")) {
                     Toast.makeText(Updatepwd.this,"修改密码成功！",Toast.LENGTH_LONG).show();
                     finish();

                }else
                {
                    Toast.makeText(Updatepwd.this,"请输入正确的登录密码！",Toast.LENGTH_LONG).show();
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

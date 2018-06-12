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
import java.util.regex.Pattern;

public class InsertAddress extends AppCompatActivity {
    private Button save;
    private EditText clientname;
    private EditText tel;
    private EditText address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_activity);
        clientname = (EditText) findViewById(R.id.person);
        tel = (EditText) findViewById(R.id.tel);
        address = (EditText) findViewById(R.id.inaddress);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = isMobile(tel.getText().toString());
                if (check == false) {
                    Toast.makeText(InsertAddress.this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();
                }else {
                    String adurl = "?name=" + Frag_MainActivity.user.getUsername()
                            + "&sender=" + clientname.getText().toString()
                            + "&tel=" + tel.getText().toString()
                            + "&address=" + address.getText().toString();
                    String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/InsertAdress" + adurl;
                    ReadURL(url);
                    finish();

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
                if (!s.equals("")) {
                    Toast.makeText(InsertAddress.this, "添加地址成功！", Toast.LENGTH_LONG).show();
Intent intent=new Intent(InsertAddress.this,AddressActivity.class);
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

    public static boolean isMobile(String mobile) {
        String regex = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        return Pattern.matches(regex, mobile);
    }

}

package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class Publishdy extends AppCompatActivity {
private EditText title;
private EditText describe;
private EditText content;
private Button publish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishdy);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        title= (EditText) findViewById(R.id.title);
        describe= (EditText) findViewById(R.id.describe);
        content= (EditText) findViewById(R.id.content);
        publish= (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String url="http://"+Frag_MainActivity.localhost+":8080/circle/servlet/Dynamic?function=insert&title="+title.getText().toString()
                            +"&describe="+describe.getText().toString()+"&content="+content.getText().toString()
                            +"&author="+Frag_MainActivity.user.getUsername();

                    ReadURL(url);


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
Toast.makeText(Publishdy.this,"发表动态成功",Toast.LENGTH_LONG).show();
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

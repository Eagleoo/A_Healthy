package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class InsertReview extends AppCompatActivity {
private EditText recontent;
private Button button;
private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);
        recontent= (EditText) findViewById(R.id.reviewcontent);
        button= (Button) findViewById(R.id.anccreview);
        Intent intent=getIntent();
id=Integer.parseInt(intent.getStringExtra("dyid"));
        recontent.setMovementMethod(ScrollingMovementMethod.getInstance());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Frag_MainActivity.user!=null)
                {
                    String url="http://"+Frag_MainActivity.localhost+":8080/circle/servlet/Dynamic?function=insertreview&username="+Frag_MainActivity.user.getUsername()
                            +"&consult_id="+id+"&content="+recontent.getText().toString().trim();

                    ReadURL(url);


                }else {
                    Toast.makeText(InsertReview.this,"请先登录",Toast.LENGTH_LONG).show();
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
                super.onPostExecute(s);

                    Toast.makeText(InsertReview.this, "评论成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InsertReview.this, ReviewActivity.class);
                    intent.putExtra("dyid",s);

                    startActivity(intent);
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

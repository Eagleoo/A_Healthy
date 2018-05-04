package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.User;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_no;
    private Button btn_ok;
    private EditText username;
    private EditText password;
    private EditText sex;
    private EditText tall;
    private EditText weight;
    private EditText age;
    private EditText address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.pwd);
        sex= (EditText) findViewById(R.id.sex);
        tall= (EditText) findViewById(R.id.height);
        weight= (EditText) findViewById(R.id.weight);
        age= (EditText) findViewById(R.id.age);
        address= (EditText) findViewById(R.id.address);
        btn_no= (Button) findViewById(R.id.btn_no);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_ok:
                String adress="http://192.168.43.53:8080/Health/servlet/ClientRegister?user_name="+username.getText().toString()+"&user_password=" +
                        password.getText().toString()+"&user_sex="+sex.getText().toString()+"&user_tall="+tall.getText().toString()
                        +"&user_weight="+weight.getText().toString()
                 +"&user_age="+age.getText().toString()
                        +"&user_adress="+address.getText().toString();


                ReadURL(adress);
                break;
            case R.id.btn_no:
                finish();
                break;
        }
    }
    public void ReadURL(final String url){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    int resultCode=connection.getResponseCode();
                    StringBuffer response =null;
                    if(HttpURLConnection.HTTP_OK==resultCode){
                        InputStream in =connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        response = new StringBuffer();
                        String line = null;
                        while((line=reader.readLine())!=null){
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

                if (s.equals("OK")) {
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
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

    };
}

package com.example.administrator.steps_count.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;


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
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_ok;
    private EditText username;
    private EditText password;
    private EditText tall;
    private EditText weight;
    private EditText age,user_phone;
    private String sex;
    private String string = null;
    private TextView notice1,notice2,notice3,notice4,notice5;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RadioGroup radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pwd);
        tall = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
        user_phone= (EditText) findViewById(R.id.user_phone);
        notice1=(TextView)findViewById(R.id.notice1);
        notice2=(TextView)findViewById(R.id.notice2);
        notice3=(TextView)findViewById(R.id.notice3);
        notice4=(TextView)findViewById(R.id.notice4);
        notice5=(TextView)findViewById(R.id.notice5);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        user_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern china= Pattern.compile("[0-9]");
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(s);
                for(int i=0;i<stringBuffer.length();i++){
                    char answer = stringBuffer.charAt(i);
                    if(!china.matcher(String.valueOf(answer)).matches()){
                        notice1.setVisibility(View.VISIBLE);
                    }
                }
                if(stringBuffer.toString().equals("")){
                    notice1.setVisibility(View.GONE);
                }


            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern china= Pattern.compile("[0-9]");
                Pattern english= Pattern.compile("[a-zA-Z]");
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(s);
                for(int i=0;i<stringBuffer.length();i++){
                    char answer = stringBuffer.charAt(i);
                    if(!china.matcher(String.valueOf(answer)).matches()&&!english.matcher(String.valueOf(answer)).matches()){
                        notice2.setVisibility(View.VISIBLE);
                    }
                }
                if(stringBuffer.toString().equals("")){
                    notice2.setVisibility(View.GONE);
                }

            }
        });
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern china= Pattern.compile("[0-9]");
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(s);
                for(int i=0;i<stringBuffer.length();i++){
                    char answer = stringBuffer.charAt(i);
                    if(!china.matcher(String.valueOf(answer)).matches()){
                        notice3.setVisibility(View.VISIBLE);
                    }
                }
                if(stringBuffer.toString().equals("")){
                    notice3.setVisibility(View.GONE);
                }

            }
        });
        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern china= Pattern.compile("[0-9]");
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(s);
                for(int i=0;i<stringBuffer.length();i++){
                    char answer = stringBuffer.charAt(i);
                    if(!china.matcher(String.valueOf(answer)).matches()){
                        notice4.setVisibility(View.VISIBLE);
                    }
                }
                if(stringBuffer.toString().equals("")){
                    notice4.setVisibility(View.GONE);
                }

            }
        });

        tall.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern china= Pattern.compile("[0-9]");
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(s);
                for(int i=0;i<stringBuffer.length();i++){
                    char answer = stringBuffer.charAt(i);
                    if(!china.matcher(String.valueOf(answer)).matches()){
                        notice5.setVisibility(View.VISIBLE);
                    }
                }
                if(stringBuffer.toString().equals("")){
                    notice5.setVisibility(View.GONE);
                }

            }
        });

        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                sex=radbtn.getText().toString();
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (user_phone.getText().toString().equals("") ||password.getText().toString().equals("") || sex.equals("")
                        || tall.getText().toString().equals("") || weight.getText().toString().equals("")
                        || age.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入完整您的信息", Toast.LENGTH_LONG).show();
                }
                else if(user_phone.getText().toString().length()<11){
                        notice1.setVisibility(View.VISIBLE);
                        notice1.setText("手机号至少11位！");
                        }
                else {
                    Insert_User();
//                    String adress = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/ClientRegister?user_phone=" + user_phone.getText().toString().trim() + "&user_name=" + username.getText().toString().trim()+"&user_password=" +
//                            password.getText().toString().trim() + "&user_sex=" + sex + "&user_tall=" + tall.getText().toString().trim()
//                            + "&user_weight=" + weight.getText().toString().trim()
//                            + "&user_age=" + age.getText().toString().trim();
//                    ReadURL(adress);
                }

                break;
        }
    }
//    public void Insert_User(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    OkHttpClient client = new OkHttpClient();
//                    FormBody.Builder requestbuilder = new FormBody.Builder();
//                    RequestBody builder = requestbuilder
//                            .add("user_phone",user_phone.getText().toString().trim())
//                            .add("user_password", password.getText().toString().trim())
//                            .add("user_name",username.getText().toString().trim())
//                            .add("user_sex",sex)
//                            .add("user_weight",weight.getText().toString().trim() )
//                            .add("user_age",age.getText().toString().trim())
//                            .add("user_tall",tall.getText().toString().trim())
//                            .build();
//
//
//                    Request request = new Request.Builder()
//                            .url(Constant.CONNECTURL + "circle/servlet/ClientRegister").post(builder)
//                            .build();
//
//                    Response response = client.newCall(request).execute();
//                    String responseData=response.body().string();
//                    Log.e("sssssss", responseData);
//                    if (responseData.equals("OK")) {
//                        Log.e("sssssss","sssssss" );
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                    if (responseData.equals("F")) {
//                        Toast.makeText(RegisterActivity.this, "用户名已存在，请重新输入！", Toast.LENGTH_LONG).show();
//                        username.setText("");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private void Insert_User(){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("user_phone",user_phone.getText().toString().trim())
                .add("user_password", password.getText().toString().trim())
                .add("user_name",username.getText().toString().trim())
                .add("user_sex",sex)
                .add("user_weight",weight.getText().toString().trim() )
                .add("user_age",age.getText().toString().trim())
                .add("user_tall",tall.getText().toString().trim())
                .build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CONNECTURL + "circle/servlet/ClientRegister")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                string = response.body().string();
                if (response.isSuccessful()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String responseData= null;
                            try {
                                responseData = new Json_Tools().Json_To_String(string);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (responseData.equals("OK")) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            if (responseData.equals("F")) {
                                Toast.makeText(RegisterActivity.this, "手机号已注册，请使用账号密码登录！", Toast.LENGTH_LONG).show();
                                user_phone.setText("");
                            }
                        }
                    });



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

                if (s.equals("OK")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                if (s.equals("F")) {
                    Toast.makeText(RegisterActivity.this, "用户名已存在，请重新输入！", Toast.LENGTH_LONG).show();
                    username.setText("");
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

package com.example.administrator.steps_count.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.example.administrator.steps_count.R;


import com.example.administrator.steps_count.model.User;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    ;
    private Button btn_ok;
    private TextView btn_no,notice1;
    private TextView register;
    private ImageView qqlogin, baidulogin,notice_img;
    private Gson gson;
    private EditText user_name;
    private EditText user_password;
    private String address;





    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        gson = new Gson();
        user_name = (EditText) findViewById(R.id.etd_user);
        user_password = (EditText) findViewById(R.id.edt_password);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        register = (TextView) findViewById(R.id.register);
        btn_no = (TextView) findViewById(R.id.btn_no);
        notice1= (TextView) findViewById(R.id.notice1);
        qqlogin = (ImageView) findViewById(R.id.qqlogin);
        baidulogin = (ImageView) findViewById(R.id.baidulogin);
        notice_img= (ImageView) findViewById(R.id.notice_img);
        baidulogin.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_no.setOnClickListener(this);
        register.setOnClickListener(this);
        qqlogin.setOnClickListener(this);

        user_name.addTextChangedListener(
                new TextWatcher() {
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
                                notice_img.setVisibility(View.VISIBLE);
                            }
                        }
                        if(stringBuffer.toString().equals("")){
                            notice1.setVisibility(View.GONE);
                            notice_img.setVisibility(View.GONE);
                        }
                    }
                }
        );

        user_password.setOnTouchListener(
                new View.OnTouchListener() {
                    int touch_flag=0;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        touch_flag++;
                        if(touch_flag==2){
                            if(!user_name.getText().toString().equals("")){
                                btn_ok.setBackgroundResource(R.drawable.btn_text_result);
                                btn_ok.setTextColor(Color.parseColor("#000000"));
                            }

                        }

                        return false;
                    }
                }
        );

        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, LoginActivity.this.getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                address = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/ClientLogin?user_name=" + user_name.getText().toString() +
                        "&user_password=" + user_password.getText().toString();
                ReadURL(address);
                break;
            case R.id.btn_no:
                Intent no = new Intent(LoginActivity.this, Frag_MainActivity.class);
                startActivity(no);
                break;
            case R.id.register:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                break;
            case R.id.qqlogin:
                mIUiListener = new BaseUiListener();
                mTencent.login(LoginActivity.this, "", mIUiListener);

                break;
            case R.id.baidulogin:
                final Baidu baidu = new Baidu("PgepZ7EHdeUOPYKdCiks6wz1", LoginActivity.this);
                baidu.authorize(LoginActivity.this, true, true, new BaiduDialog.BaiduDialogListener() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        String url = "https://openapi.baidu.com/rest/2.0/passport/users/getInfo";
                        AsyncBaiduRunner run = new AsyncBaiduRunner(baidu);
                        run.request(url, null, "GET", new AsyncBaiduRunner.RequestListener() {
                            @Override
                            public void onComplete(String s) {
                                RefreshUse(s);


                            }

                            @Override
                            public void onIOException(IOException e) {
                                RefreshUse("IOException");
                            }

                            @Override
                            public void onBaiduException(BaiduException e) {
                            }
                        });
                    }

                    @Override
                    public void onBaiduException(BaiduException e) {

                    }

                    @Override
                    public void onError(BaiduDialogError baiduDialogError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
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

                if (!s.equals("fall")) {
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, Frag_MainActivity.class);
                    User user = gson.fromJson(s, User.class);
                    intent.putExtra("user", user);
                    startActivity(intent);


                } else {
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_LONG).show();
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

    public void QQBaiduReadURL(final String url) {
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
                Intent intent = new Intent(LoginActivity.this, Frag_MainActivity.class);
                User user = gson.fromJson(s, User.class);
                intent.putExtra("user", user);
                startActivity(intent);

            }


                @Override
                protected void onCancelled (String s){
                    super.onCancelled(s);
                }

                @Override
                protected void onCancelled () {
                    super.onCancelled();
                }
            }.execute(url);
}

    public void RefreshUse(final String msg)

    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                User usr = gson.fromJson(msg, User.class);
                String username = gson.toJson(usr.getUsername()).replace("\"", "");
                String userimg = gson.toJson(usr.getPortrait()).replace("\"", "");
                String sex=gson.toJson(usr.getSex());
               if(sex.equals("1"))
               {
                   usr.setSex("男");
               }else
               {
                   usr.setSex("女");
               }
                String url = "http://tb.himg.baidu.com/sys/portrait/item/%7B$" + userimg + "%7D";

                String address = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/ClientRegister?user_name=" + username
                        + "&user_password="+"&user_sex="+usr.getSex()+"&portrait="+url;
               QQBaiduReadURL(address);




            }
        });
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();

            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        //是一个json串response.tostring，直接使用gson解析就好


                       JSONObject  oo = (JSONObject) response;
                        try {
                            String na = oo.getString("nickname");
                            String url = oo.getString("figureurl_2");
                            String sex=oo.getString("gender");
                            String address = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/ClientRegister?user_name=" + na
                                    + "&user_password="+"&user_sex="+sex+"&portrait="+url;
                            QQBaiduReadURL(address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

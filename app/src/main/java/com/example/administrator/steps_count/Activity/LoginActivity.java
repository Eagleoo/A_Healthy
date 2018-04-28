package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.UserModel;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;;
    private Button btn_ok;
    private Button btn_no;
    private TextView register;
    private ImageView qqlogin,baidulogin;
    private Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        gson=new Gson();

        btn_ok= (Button) findViewById(R.id.btn_ok);
        register= (TextView) findViewById(R.id.register);
        btn_no= (Button) findViewById(R.id.btn_no);
        qqlogin= (ImageView) findViewById(R.id.qqlogin);
        baidulogin= (ImageView) findViewById(R.id.baidulogin);
        baidulogin.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_no.setOnClickListener(this);
        register.setOnClickListener(this);
        qqlogin.setOnClickListener(this);
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, LoginActivity.this.getApplicationContext());



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_ok:

                break;
            case R.id.btn_no:
                Intent no=new Intent(LoginActivity.this,Frag_MainActivity.class);
                startActivity(no);
                break;
            case R.id.register:
                Intent register=new Intent(this,RegisterActivity.class);
                startActivity(register);
                break;
            case R.id.qqlogin:
                mIUiListener = new BaseUiListener();
                mTencent.login(LoginActivity.this, "all", mIUiListener);
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
    public void RefreshUse(final String msg)

    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                UserModel usr=gson.fromJson(msg,UserModel.class);
                String username=gson.toJson(usr.getUsername());
                String userimg=gson.toJson(usr.getPortrait()).replace("\"","");
                String url="http://tb.himg.baidu.com/sys/portrait/item/{$"+userimg+"}";
                Intent baidulogin=new Intent(LoginActivity.this,Frag_MainActivity.class);
                baidulogin.putExtra("name",username);
                baidulogin.putExtra("head",url);
                startActivity(baidulogin);


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


                        JSONObject oo= (JSONObject) response;
                        try {
                            String   na = oo.getString("nickname");
                            String url=oo.getString("figureurl_2");
                            Intent qqintent=new Intent(LoginActivity.this,Frag_MainActivity.class);
                            qqintent.putExtra("head",url);
                            qqintent.putExtra("name",na);
                            startActivity(qqintent);




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

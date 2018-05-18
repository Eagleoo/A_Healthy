package com.example.administrator.steps_count.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.CheckPayment;
import com.example.administrator.steps_count.Activity.CheckReceive;
import com.example.administrator.steps_count.Activity.CheckSend;
import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.LoginActivity;
import com.example.administrator.steps_count.Activity.OrderActivity;
import com.example.administrator.steps_count.Activity.PerCollect;
import com.example.administrator.steps_count.Activity.PerMassageActivity;
import com.example.administrator.steps_count.Activity.QQBaiduRegister;
import com.example.administrator.steps_count.Activity.SettingActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.User;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

import static com.loc.g.s;

public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView txt_login;
    private TextView per_message;
    private ImageView head;
    private TextView setting;
    private TextView order;
private TextView isreceive;
private TextView payment;
private TextView issend;
private TextView collect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_layout, container, false);


        txt_login = (TextView) view.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
        per_message = (TextView) view.findViewById(R.id.per_message);
        per_message.setOnClickListener(this);
        head = (ImageView) view.findViewById(R.id.head);
        setting = (TextView) view.findViewById(R.id.setting);
        setting.setOnClickListener(this);
        order=(TextView)view.findViewById(R.id.order);
        order.setOnClickListener(this);
        isreceive= (TextView) view.findViewById(R.id.isreceive);
        isreceive.setOnClickListener(this);
        payment= (TextView) view.findViewById(R.id.payment);
        payment.setOnClickListener(this);
        issend= (TextView) view.findViewById(R.id.issend);
        issend.setOnClickListener(this);
        collect=(TextView)view.findViewById(R.id.collect);
        collect.setOnClickListener(this);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);

        if (Frag_MainActivity.user != null) {
            txt_login.setText(Frag_MainActivity.user.getUsername());
            txt_login.setEnabled(false);
            ImageLoader.getInstance().displayImage(Frag_MainActivity.user.getPortrait(), head);

        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_login:
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
                break;
            case R.id.per_message:

                if (Frag_MainActivity.user!=null) {
                    Intent message = new Intent(getActivity(), PerMassageActivity.class);
                    startActivity(message);
                }else {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.order:
                if (Frag_MainActivity.user!=null)
                {
                    Intent intent=new Intent(getActivity(), OrderActivity.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.issend:
                if (Frag_MainActivity.user!=null)
                {
                    Intent intent=new Intent(getActivity(), CheckSend.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.isreceive:
                if (Frag_MainActivity.user!=null)
                {
                    Intent intent=new Intent(getActivity(), CheckReceive.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.payment:
                if (Frag_MainActivity.user!=null)
                {
                    Intent intent=new Intent(getActivity(), CheckPayment.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.collect:
                if (Frag_MainActivity.user!=null)
                {
                    Intent intent=new Intent(getActivity(), PerCollect.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.setting:
                Intent setting = new Intent(getActivity(), SettingActivity.class);
                startActivity(setting);
                break;
        }
    }


}

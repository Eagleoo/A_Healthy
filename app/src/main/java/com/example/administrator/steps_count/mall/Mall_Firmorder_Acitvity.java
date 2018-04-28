package com.example.administrator.steps_count.mall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.tools.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by PC on 2018/3/30.
 */

public class Mall_Firmorder_Acitvity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mall_firmorder_less;
    private ImageView mall_firmorder_more;
    private ImageView mall_firmorder_exit;
    private ImageView mall_firmorder_img;
    private TextView mall_firmorder_num;
    private TextView mall_firmorder_allmoney;
    private TextView mall_firmorder_unitprice;
    private TextView mall_firmorder_extra;
    private TextView mall_firmorder_name;
    private TextView mall_firmorder_price;
    private RadioButton mall_firmorder_rb_addaddress;
    private FrameLayout mall_firmorder_framelayout;
    private FrameLayout mall_firmorder_radiolayout;
    private FrameLayout mall_firmorder_addresslayout;
    private double unitprice;
    private double extra;
    private String mall_id;
    private int mNum;
    private List<Mall>mallList=new ArrayList<Mall>();
    private String mNumS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_firmorder_layout);
        Bundle bundle=new Bundle();
        bundle = this.getIntent().getExtras();
        mall_id=bundle.getString("0x3");
        init();
        getDataAsync();

    }

    private void init()
    {
        mall_firmorder_less= (ImageView) findViewById(R.id.mall_firmorder_less);
        mall_firmorder_more= (ImageView) findViewById(R.id.mall_firmorder_more);
        mall_firmorder_exit= (ImageView) findViewById(R.id.mall_firmorder_exit);
        mall_firmorder_num= (TextView) findViewById(R.id.mall_firmorder_num);
        mall_firmorder_allmoney= (TextView) findViewById(R.id.mall_firmorder_allmoney);
        mall_firmorder_extra= (TextView) findViewById(R.id.mall_firmorder_extra);
        mall_firmorder_unitprice= (TextView) findViewById(R.id.mall_firmorder_unitprice);
        mall_firmorder_rb_addaddress= (RadioButton) findViewById(R.id.mall_firmorder_rb_addaddress);
        mall_firmorder_framelayout= (FrameLayout) findViewById(R.id.mall_firmorder_framelayout);
        mall_firmorder_radiolayout= (FrameLayout) findViewById(R.id.mall_firmorder_Radiolayout);
        mall_firmorder_addresslayout= (FrameLayout) findViewById(R.id.mall_firmorder_addresslayout);
        mall_firmorder_price= (TextView) findViewById(R.id.mall_firmorder_price);
        mall_firmorder_img= (ImageView) findViewById(R.id.mall_firmorder_img);
        mall_firmorder_name= (TextView) findViewById(R.id.mall_firmorder_name);
        mall_firmorder_less.setOnClickListener(this);
        mall_firmorder_more.setOnClickListener(this);
        mall_firmorder_exit.setOnClickListener(this);
        mall_firmorder_rb_addaddress.setOnClickListener(this);

//        unitprice= Double.parseDouble(mall_firmorder_unitprice.getText().toString());
        extra= Double.parseDouble(mall_firmorder_extra.getText().toString());
        mNum= Integer.parseInt(mall_firmorder_num.getText().toString());
        mall_firmorder_framelayout.removeView(mall_firmorder_addresslayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mall_firmorder_less:
                if (Integer.parseInt(mall_firmorder_num.getText().toString())<=1)
                {
                    Toast.makeText(getApplicationContext(),"不能再少了",Toast.LENGTH_SHORT).show();
                }else {
                mall_firmorder_num.setText(String.valueOf(Integer.parseInt(mall_firmorder_num.getText().toString())-1));
                dymic_allmoney(unitprice,extra,Integer.parseInt(mall_firmorder_num.getText().toString()));
                }
                break;
            case R.id.mall_firmorder_more:
                mall_firmorder_num.setText(String.valueOf(Integer.parseInt(mall_firmorder_num.getText().toString())+1));
                dymic_allmoney(unitprice,extra,Integer.parseInt(mall_firmorder_num.getText().toString()));
                break;
            case R.id.mall_firmorder_exit:
                finish();
                break;
            case R.id.mall_firmorder_rb_addaddress:
                mall_firmorder_framelayout.removeView(mall_firmorder_radiolayout);
                mall_firmorder_framelayout.addView(mall_firmorder_addresslayout);
                break;
        }
    }
    //刷新总付款金额
    private void dymic_allmoney(double unitprice,double extra,int num)
    {
        String Aprice= String.valueOf(unitprice*num+extra);
        mall_firmorder_allmoney.setText(Aprice);
    }

    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("action",mall_id).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://192.168.1.111:8080/Mall_Detail_Servlet")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mallList=getMall("mall",json);
                            Glide.with(getApplicationContext()).load(mallList.get(0).getMall_img()).into(mall_firmorder_img);
                            mall_firmorder_name.setText(mallList.get(0).getMall_name());
                            mall_firmorder_price.setText(mallList.get(0).getMall_price());
                            mall_firmorder_unitprice.setText(mallList.get(0).getMall_price());
                            unitprice= Double.parseDouble(mallList.get(0).getMall_price());
                            dymic_allmoney(unitprice,extra,mNum);
                        }
                    });
                }
            }
        });
    }

    private static List<Mall> getMall(String key, String jsonString) {
        List<Mall> list = new ArrayList<Mall>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Mall mall = new Mall();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                mall.setMall_name(jsonObject2.getString("mall_name"));
                mall.setMall_describe(jsonObject2.getString("mall_describe"));
                mall.setMall_price(jsonObject2.getString("mall_price"));
                mall.setMall_img(jsonObject2.getString("mall_img"));
                list.add(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}

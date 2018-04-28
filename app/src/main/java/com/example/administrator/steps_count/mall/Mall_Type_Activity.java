package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Mall_adapter;

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
 * Created by PC on 2018/4/1.
 */

public class Mall_Type_Activity extends AppCompatActivity {
    private ImageView mall_type_exit;
    private TextView mall_type_title;
    private Mall_adapter mall_adapter;
    private GridView mall_type_grid;
    private String type;
    private List<Mall_Bean> mall_list = new ArrayList<Mall_Bean>();
    private List<Mall> list = new ArrayList<Mall>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_type_layout);
        init();
    }
    public void init()
    {
        mall_type_grid= (GridView) findViewById(R.id.mall_type_grid);
        mall_type_exit= (ImageView) findViewById(R.id.mall_type_exit);
        mall_type_title= (TextView) findViewById(R.id.mall_type_title);

        //mall_adapter = new Mall_adapter(getApplicationContext(), list);
        mall_type_grid.setAdapter(mall_adapter);
        mall_type_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        mall_type_title.setText(bundle.getString("mall_btn"));
        type=bundle.getString("type");
        Toast.makeText(getApplicationContext(),type,Toast.LENGTH_LONG).show();

    }
//    public List<Mall_Bean> getData() {
//
///*        String uname= MainActivity.user.getUsername();
//        Cursor cursor=dbHelper.select_all(uname);
////        将cursor对象中的数据依次放入list中
//
//        while (cursor.moveToNext())
//        {
//            int head_img=cursor.getInt(cursor.getColumnIndex("oimg"));
//            String name=cursor.getString(cursor.getColumnIndex("oname"));
//            String price=cursor.getString(cursor.getColumnIndex("oprice"));
//
//            Order_Bean order_bean =new Order_Bean();
//            order_bean.setHead_img(head_img);
//            order_bean.setName(name);
//            order_bean.setPrice(price);
//
//            list.add(order_bean);
//        }*/
//
//    }



}

package com.example.administrator.steps_count.mall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Search_adapter;
import com.nostra13.universalimageloader.utils.L;

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
 * Created by PC on 2018/3/31.
 */

public class Mall_Search_Activity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private ImageView mall_search_delete;
    private EditText mall_search_edit;
    private ListView mall_search_list;
    private Button mall_search_btn;
    private List<Mall_Name> mall_names_list=new ArrayList<Mall_Name>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_search_layout);
        init();
        getMallName("");
        Search_adapter search_adapter=new Search_adapter(getApplicationContext(),mall_names_list);
        mall_search_list.setAdapter(search_adapter);

    }
    private void init()
    {
        mall_search_delete= (ImageView) findViewById(R.id.mall_search_delete);
        mall_search_edit= (EditText) findViewById(R.id.mall_search_edit);
        mall_search_list= (ListView) findViewById(R.id.mall_search_list);
        mall_search_btn= (Button) findViewById(R.id.mall_search_btn);

        mall_search_delete.setOnClickListener(this);
        mall_search_btn.setOnClickListener(this);
        mall_search_edit.addTextChangedListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.mall_search_delete:
            //把EditText内容设置为空
            mall_search_edit.setText("");
            //把ListView隐藏
            mall_search_list.setVisibility(View.GONE);
            mall_search_delete.setVisibility(View.GONE);
            break;
            case R.id.mall_search_btn:
                if(mall_search_edit.getText()==null)
                {
                    Toast.makeText(getApplicationContext(),"请输入搜索内容！",Toast.LENGTH_LONG).show();
                }
                else {

                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            //隐藏“删除”图片
            mall_search_delete.setVisibility(View.GONE);
        } else {//长度不为0
            //显示“删除图片”
            mall_search_delete.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getMallName(String.valueOf(charSequence));
        Search_adapter search_adapter=new Search_adapter(getApplicationContext(),mall_names_list);
        mall_search_list.setAdapter(search_adapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
    private void getMallName(String mall_name) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("search",mall_name).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://192.168.1.111:8080/Search_Servlet")
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
                            mall_names_list=getMall("mall_name",json);
                            Log.i("mall_name", String.valueOf(mall_names_list));

                        }
                    });
                }
            }
        });
    }
    private static List<Mall_Name> getMall(String key, String jsonString) {
        List<Mall_Name> list = new ArrayList<Mall_Name>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Mall_Name mall_name = new Mall_Name();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                mall_name.setMall_name(jsonObject2.getString("mall_name"));
                list.add(mall_name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}

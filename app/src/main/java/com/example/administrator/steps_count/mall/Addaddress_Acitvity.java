package com.example.administrator.steps_count.mall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by PC on 2018/6/22.
 */

public class Addaddress_Acitvity extends AppCompatActivity {
    private EditText edit_consignee;
    private EditText edit_cellnumber;
    private EditText edit_address;
    private Button add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaddress_layout);
        edit_consignee= (EditText) findViewById(R.id.edit_consignee);
        edit_cellnumber= (EditText) findViewById(R.id.edit_cellnumber);
        edit_address= (EditText) findViewById(R.id.edit_address);
        add_btn= (Button) findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_consignee.getText().toString().trim().equals("")||edit_cellnumber.getText().toString().trim().equals("")||edit_address.getText().toString().trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(),"不能有空哦！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    insertadd(Frag_MainActivity.user.getUsername(),edit_consignee.getText().toString().trim(),edit_cellnumber.getText().toString().trim(),edit_address.getText().toString().trim());}
                    finish();
            }
        });

    }
    private void insertadd(String username, String consignee, String cellnumber, String address) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("roof","android")
                .add("username", Frag_MainActivity.user.getUsername())
                .add("consignee",consignee)
                .add("cellnumber",cellnumber)
                .add("address",address)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Insertaddress_Servlet")
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
                            Toast.makeText(getApplicationContext(),json, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}

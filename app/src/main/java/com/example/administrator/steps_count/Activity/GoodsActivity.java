package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.steps_count.R;

public class GoodsActivity extends AppCompatActivity {
    private Button insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        insert= (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maAddress=new Intent(GoodsActivity.this,AddressActivity.class);
                startActivity(maAddress);
            }
        });
    }
}

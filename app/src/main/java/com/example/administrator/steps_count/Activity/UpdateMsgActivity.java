package com.example.administrator.steps_count.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.steps_count.R;

public class UpdateMsgActivity extends AppCompatActivity {
    private Button btn_update;
    private Button btn_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_msg);
        btn_update= (Button) findViewById(R.id.update);
        btn_exit= (Button) findViewById(R.id.exit);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

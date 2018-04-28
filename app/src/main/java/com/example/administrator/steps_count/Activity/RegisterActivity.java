package com.example.administrator.steps_count.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.steps_count.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_no;
    private Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_no= (Button) findViewById(R.id.btn_no);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_ok:
                break;
            case R.id.btn_no:
                finish();
                break;
        }
    }
}

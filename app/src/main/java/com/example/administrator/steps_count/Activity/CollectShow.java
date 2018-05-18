package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.steps_count.R;

public class CollectShow extends AppCompatActivity {
    private TextView title;
    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_show);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.context);
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
    }
}

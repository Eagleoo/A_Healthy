package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowDynamic extends AppCompatActivity {
private TextView title;
private TextView content;
private Button review;
private Button insertreview;
private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dynamic);
        title= (TextView) findViewById(R.id.dytitle);
        content= (TextView) findViewById(R.id.dycontent);
        review= (Button) findViewById(R.id.dyreview);
        insertreview= (Button) findViewById(R.id.reviewcontent);
        final Intent intent=getIntent();
        title.setText(intent.getStringExtra("dyname"));
        content.setText(intent.getStringExtra("dycontent"));

        id=intent.getStringExtra("dyid");

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(ShowDynamic.this,ReviewActivity.class);
              intent.putExtra("dyid",id);
              startActivity(intent);
            }
        });
        insertreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ShowDynamic.this,InsertReview.class);
                intent1.putExtra("dyid",id);
                startActivity(intent1);
            }
        });
    }

}

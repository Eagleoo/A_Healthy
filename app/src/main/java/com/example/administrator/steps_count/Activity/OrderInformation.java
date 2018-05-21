package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OrderInformation extends AppCompatActivity {
private ImageView imageView;
private TextView name;
private TextView describe;
private TextView price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        imageView= (ImageView) findViewById(R.id.img);
        name= (TextView) findViewById(R.id.name);
        describe= (TextView) findViewById(R.id.describe);
        price= (TextView) findViewById(R.id.price);
        Intent intent=getIntent();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(OrderInformation.this);
        ImageLoader.getInstance().init(configuration);
      ImageLoader.getInstance().displayImage(intent.getStringExtra("img"),imageView);
        name.setText(intent.getStringExtra("name"));
 describe.setText(intent.getStringExtra("describe"));
      price.setText(intent.getStringExtra("price"));
    }
}

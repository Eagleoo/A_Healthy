package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.User;
import com.example.administrator.steps_count.tools.Text;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PerMassageActivity extends AppCompatActivity {
    private Button btn_update;
    private Button btn_exit;
    private ImageView head;
    private TextView username;
    private TextView sex;
    private TextView tall;
    private TextView weight;
    private TextView age;
    private TextView address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_massage);
        btn_update= (Button) findViewById(R.id.update);
        username= (TextView) findViewById(R.id.username);
        sex= (TextView) findViewById(R.id.sex);
        tall= (TextView) findViewById(R.id.height);
        weight= (TextView) findViewById(R.id.weight);
        age= (TextView) findViewById(R.id.age);
        address= (TextView) findViewById(R.id.address);
        btn_exit= (Button) findViewById(R.id.exit);
        head= (ImageView) findViewById(R.id.image);


        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

      if(Frag_MainActivity.user!=null)
      {
          username.setText(Frag_MainActivity.user.getUsername());
          sex.setText(Frag_MainActivity.user.getSex());
          tall.setText(String.valueOf(Frag_MainActivity.user.getUser_tall()));
          weight.setText(String.valueOf(Frag_MainActivity.user.getUser_weight()));
          age.setText(String.valueOf(Frag_MainActivity.user.getUser_age()));
          ImageLoader.getInstance().displayImage(Frag_MainActivity.user.getPortrait().trim(),head);

      }


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update_msg=new Intent(PerMassageActivity.this,UpdateMsgActivity.class);
                startActivity(update_msg);
            }
        });
    }
}

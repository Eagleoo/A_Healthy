package com.example.administrator.steps_count.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PerMassageActivity extends AppCompatActivity {
    private Button btn_update;
    private Button btn_exit;
    private ImageView head;
    private TextView user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_massage);
        btn_update= (Button) findViewById(R.id.update);
        btn_exit= (Button) findViewById(R.id.exit);
        head= (ImageView) findViewById(R.id.image);
        user= (TextView) findViewById(R.id.name);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        if (Frag_MainActivity.data!=null)
        {
            ImageLoader.getInstance().displayImage(Frag_MainActivity.data,head);
        }
        if(Frag_MainActivity.name!=null)
        {
            user.setVisibility(View.GONE);

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

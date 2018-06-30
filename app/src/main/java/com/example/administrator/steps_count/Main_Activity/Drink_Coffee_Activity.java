package com.example.administrator.steps_count.Main_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.ProgressView;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.step.User_DBOpenHelper;

public class Drink_Coffee_Activity extends AppCompatActivity {
    private ProgressView progressView1,progressView2;
    private User_DBOpenHelper u_db;
    private User_Data user_data;
    private ImageView drink_back;
    private TextView drink_warn,coffee_warn,coffee_notice,drink_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_coffee);

        drink_back=(ImageView)findViewById(R.id.drink_back);
        drink_warn=(TextView)findViewById(R.id.drink_warn);
        coffee_warn=(TextView)findViewById(R.id.coffee_warn);
        drink_notice=(TextView)findViewById(R.id.drink_notice);
        coffee_notice=(TextView)findViewById(R.id.coffee_notice);
        progressView1=(ProgressView)findViewById(R.id.progressView1);
        progressView1.setColor3();
        progressView2=(ProgressView)findViewById(R.id.progressView2);
        progressView2.setColor4();
        drink_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        u_db=new User_DBOpenHelper(this);
        user_data=u_db.getCurUserDateByDate(TimeUtil.getCurrentDate());
        if(user_data!=null){
            int drink_num=Integer.valueOf(user_data.getUser_drink());
            int coffee_num=Integer.valueOf(user_data.getUser_coffee());
            if(drink_num<=3){
                drink_warn.setText("偏低");
                drink_notice.setText("记得多喝水哟");
            }
            else if(drink_num>3&&drink_num<8){
                drink_warn.setText("良好");
                drink_notice.setText("    继续坚持");
            }
            else {
                drink_warn.setText("完成");
                drink_notice.setText("    水分充足");
            }
            if(coffee_num<=2){
                coffee_warn.setText("正常");
                coffee_notice.setText("    继续保持");
            }
            else if (coffee_num==3||coffee_num==4){
                coffee_warn.setText("偏高");
                coffee_notice.setText("可不要贪杯哦");
            }
            else {
                coffee_warn.setText("极高");
                coffee_notice.setText(" 咖啡因过量！");
            }
            if(drink_num<8){
                progressView1.setMaxProgress(8);
                progressView1.setCurrentProgress(drink_num);
            }
            else {
                progressView1.setMaxProgress(drink_num);
                progressView1.setCurrentProgress(drink_num);
            }
            if(coffee_num<3){
                progressView2.setMaxProgress(3);
                progressView2.setCurrentProgress(coffee_num);
            }
            else {
                progressView2.setMaxProgress(coffee_num);
                progressView2.setCurrentProgress(coffee_num);
            }


        }

    }
}

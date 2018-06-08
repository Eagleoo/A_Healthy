package com.example.administrator.steps_count.Main_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.ActivityControl;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.TimeUtil;
import com.lsp.RulerView;

import java.text.DecimalFormat;

public class Weight_Aim_Activity extends AppCompatActivity {
    private RulerView rulerView_aim;
    private TextView tv_weight_choose_aim,cur_weight;
    private Button ok_aim;
    private DBOpenHelper db;
    private ImageView weight_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_aim);

        rulerView_aim = (RulerView) findViewById(R.id.rulerView_aim);
        tv_weight_choose_aim=(TextView)findViewById(R.id.tv_weight_choose_aim);
        cur_weight=(TextView)findViewById(R.id.cur_weight);
        ok_aim=(Button)findViewById(R.id.ok_aim);
        weight_back=(ImageView)findViewById(R.id.aim_back);

        final DecimalFormat df=new DecimalFormat("#.#");
        db=new DBOpenHelper(this);
        final String date= String.valueOf(TimeUtil.getCurrentDay());
        cur_weight.setText(db.getWeight()+"kg");
        rulerView_aim.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {

            @Override
            public void onEndResult(String s) {

                tv_weight_choose_aim.setText(df.format(Float.valueOf(s)));

            }

            @Override
            public void onScrollResult(String s) {

            }
        });

        ok_aim.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tv_weight_choose_aim.getText().toString().equals("目标")){
                            Toast.makeText(Weight_Aim_Activity.this, "请设置目标！", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            db.updateAim(tv_weight_choose_aim.getText().toString(),date);
                            startActivity(new Intent(Weight_Aim_Activity.this,Weight_Activity.class));
                            finish();
                        }

                    }
                }
        );

        weight_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Weight_Aim_Activity.this,Weight_Activity.class));
                        finish();
                    }
                }
        );


    }
}

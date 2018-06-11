package com.example.administrator.steps_count.Main_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.TimeUtil;
import com.lsp.RulerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Weight_Add_Activity extends AppCompatActivity {
    private RulerView rulerView;
    private TextView tv_weight_choose,weight_cancel,weight_ok,cur_time;
    private EditText weight_text;
    private DBOpenHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_add_layout);

        rulerView = (RulerView) findViewById(R.id.rulerView);
        tv_weight_choose=(TextView)findViewById(R.id.tv_weight_choose);
        cur_time=(TextView)findViewById(R.id.cur_time);
        weight_cancel=(TextView)findViewById(R.id.weight_cancel);
        weight_ok=(TextView)findViewById(R.id.weight_ok);
        weight_text=(EditText)findViewById(R.id.weight_text);
        final DecimalFormat df=new DecimalFormat("#.#");
        db=new DBOpenHelper(Weight_Add_Activity.this);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        cur_time.setText(str);

        rulerView.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {
            @Override
            public void onEndResult(String result) {
                tv_weight_choose.setText(df.format(Float.valueOf(result)));
            }

            @Override
            public void onScrollResult(String result) {

            }
        });


        weight_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        weight_ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            String curDate= String.valueOf(TimeUtil.getCurrentDay());
                            Weight weight1=db.getCurWeightByDate(curDate);
                            Weight weight=new Weight();
                            weight.setWeight(tv_weight_choose.getText().toString());
                            weight.setW_date(curDate);
                            weight.setW_text(weight_text.getText().toString());
                            weight.setW_aim(db.getAimWeight());
                            if(weight1==null){
                                db.addNewWeight(weight);
                            }
                            else {
                                db.updateWeight(weight);
                            }
                        startActivity(new Intent(Weight_Add_Activity.this,Weight_Activity.class));
                            finish();
                    }
                }
        );
    }

}

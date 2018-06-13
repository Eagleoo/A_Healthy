package com.example.administrator.steps_count.step;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.steps_count.Activity.ActivityControl;
import com.example.administrator.steps_count.R;

public class Run_Timer extends AppCompatActivity {
    private TextView countDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_timer);

        countDown =  (TextView) findViewById(R.id.countDown);

        init();

    }

    private void init(){

        CountDownTimer timer = new CountDownTimer(3200,1000) {
            int num = 3;

            @Override
            public void onTick(long millisUntilFinished) {
                countDown.setText(String.valueOf(num));
                num--;
            }

            @Override
            public void onFinish() {
                countDown.setText("GO!");
                startActivity(new Intent(Run_Timer.this,Step_Map.class));
                finish();
            }
        };
        timer.start();
    }
}

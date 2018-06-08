package com.example.administrator.steps_count.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.ProgressView;

public class TextActivity extends AppCompatActivity {
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        ProgressView progressView = (ProgressView) findViewById(R.id.progressView);
        progressView.setMaxProgress(4000);
        progressView.setCurrentProgress(2855);

    }
}

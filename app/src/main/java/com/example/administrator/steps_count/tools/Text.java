package com.example.administrator.steps_count.tools;

import java.util.ArrayList;
import java.util.Random;

import com.example.administrator.steps_count.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Text extends AppCompatActivity {
    private LineChart chart;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        chart=(LineChart)findViewById(R.id.chart);
        chart.setBackgroundColor(Color.GREEN);
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();
        float arr[]={10,8,5,10,7,4,6,9,7,10,12,20};

            for(int j=0;j<arr.length;j++){
                float temp=arr[j];
                yVals.add(  new Entry(temp,j));
                //xVals.add((j+1)+"月");
        }
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYAxis = chart.getAxisRight();
        YAxis yAxis =chart.getAxisLeft();


        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示

        dataSet=new LineDataSet(yVals,"历史步数");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
        data.setDrawValues(false);
        chart.setData(data);
        chart.setDescription("历史步数");
        chart.animateY(3000);
    }
}


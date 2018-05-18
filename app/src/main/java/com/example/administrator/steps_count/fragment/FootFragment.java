package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.Step_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.TimeUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//@SuppressLint("ValidFragment")
public class FootFragment extends Fragment {
    private boolean isBind = false;
    private LineChart chart;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private DBOpenHelper db;
    private Button btn_step;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.step_chart, container, false);
        chart=(LineChart)view.findViewById(R.id.chart1);
        btn_step=(Button)view.findViewById(R.id.btn_step);
        db=new DBOpenHelper(getActivity());
        init();

        btn_step.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Step_MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        return view;
    }

    private void init(){
        String curDate= TimeUtil.getCurrentDate();
        int i=0;



        xVals=new ArrayList<>();
        yVals=new ArrayList<>();

        Cursor cursor=db.mquery();
        while (cursor.moveToNext()){
            SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
            String sDate = cursor.getString(cursor.getColumnIndex("curDate"));

            float step=Float.parseFloat(cursor.getString(cursor.getColumnIndex("totalSteps")));
            try {
                Date d1 = sdf.parse(sDate);
                Date d2=sdf.parse(curDate);
                if(Math.abs(((d1.getTime()-d2.getTime())/(24*3600*1000)))<=7){
                    xVals.add(sDate);
                    yVals.add(new Entry(i,step ));
                    i++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYAxis = chart.getAxisRight();
        YAxis yAxis =chart.getAxisLeft();

        AxisValueFormatter xFormatter = new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if(xVals.size()!=0) {
                    return xVals.get((int) value % xVals.size());
                }
                else {
                    return "";
                }

            }
            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };
        AxisValueFormatter yFormatter = new AxisValueFormatter() {
            public DecimalFormat mFormat=new DecimalFormat("####");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(value)+ "步";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };
        xAxis.setLabelCount(xVals.size(),true);
        yAxis.setLabelCount(yVals.size(),true);
        xAxis.setValueFormatter(xFormatter);
        yAxis.setValueFormatter(yFormatter);

        xAxis.setAxisLineWidth(3f);
        yAxis.setAxisLineWidth(3f);
        xAxis.setAxisLineColor(Color.LTGRAY);
        yAxis.setAxisLineColor(Color.LTGRAY);
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(false);
        xAxis.setAxisMinValue(0f);
        yAxis.setAxisMinValue(0f);

        chart.setScaleEnabled(false);
        dataSet=new LineDataSet(yVals,"历史步数");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        data=new LineData(xVals,dataSet);
        data.setDrawValues(false);
        chart.setData(data);
        chart.setDescription("历史步数");
        chart.animateX(1000);
    }
}

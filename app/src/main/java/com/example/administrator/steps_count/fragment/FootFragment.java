package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.steps_count.Activity.Step_About_Activity;
import com.example.administrator.steps_count.Activity.Step_Map;
import com.example.administrator.steps_count.Main_Activity.Plan_Btn;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.Look_steps;
import com.example.administrator.steps_count.step.ProgressView;
import com.example.administrator.steps_count.step.StepEntity;
import com.example.administrator.steps_count.step.StepPlan;
import com.example.administrator.steps_count.step.Step_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Step_Plan_Activity;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.tools.Json_Tools;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FootFragment extends Fragment {

    private LineChart chart;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private DBOpenHelper db;
    private LinearLayout btn_step_run,btn_step;
    private ImageView setting1,record1;
    private TextView totalStepsTv,totalStepsKm,totalStepsKa,aim,finish_steps;
    private StepPlan stepPlan=new StepPlan();
    private ProgressView progressView;
    private ImageView left,right,step_about;
    private int step, u_id=1;
    private float km,ka;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.step_chart, container, false);
        chart=(LineChart)view.findViewById(R.id.chart1);
        btn_step_run=(LinearLayout) view.findViewById(R.id.btn_step_run);
        btn_step=(LinearLayout) view.findViewById(R.id.btn_step);
        setting1=(ImageView)view.findViewById(R.id.setting1);
        record1=(ImageView)view.findViewById(R.id.record1);
        totalStepsTv=(TextView)view.findViewById(R.id.totalStepsTv);
        totalStepsKm=(TextView)view.findViewById(R.id.totalStepsKm);
        totalStepsKa=(TextView)view.findViewById(R.id.totalStepsKa);
        finish_steps=(TextView)view.findViewById(R.id.finish_steps);
        aim=(TextView)view.findViewById(R.id.aim);
        left=(ImageView)view.findViewById(R.id.left);
        right=(ImageView)view.findViewById(R.id.right);
        step_about=(ImageView)view.findViewById(R.id.step_about) ;
        progressView = (ProgressView)view.findViewById(R.id.progressView);
        progressView.setColor1();
        db=new DBOpenHelper(getActivity());
        String curDate= TimeUtil.getCurrentDate();
        StepEntity entity = db.getCurDataByDate(curDate);

        step=Integer.valueOf(entity.getSteps());
        km= Float.parseFloat(entity.getTotalStepsKm());
        ka= Float.parseFloat(entity.getTotalStepsKa());
        totalStepsTv.setText(entity.getSteps());
        totalStepsKm.setText(entity.getTotalStepsKm());
        totalStepsKa.setText(entity.getTotalStepsKa());
        init();
        getStepPlan_steps();


        setting1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Step_Plan_Activity.class);
                        startActivity(intent);
                    }
                }
        );

        record1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Look_steps.class);
                        startActivity(intent);
                    }
                }
        );

        btn_step_run.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Step_Map.class);
                        startActivity(intent);
                    }
                }
        );

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

        left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(finish_steps.getText().toString().equals("已完成步数")){
                            getStepPlan_km();
                        }
                        else if (finish_steps.getText().toString().equals("已消耗卡路里")){
                            getStepPlan_steps();
                        }
                        else {

                        }

                    }
                }
        );

        right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(finish_steps.getText().toString().equals("已完成步数")){
                            getStepPlan_ka();
                        }
                        else if (finish_steps.getText().toString().equals("已行走距离")){
                            getStepPlan_steps();
                        }
                        else {

                        }
                    }
                }
        );

        step_about.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Step_About_Activity.class));
                    }
                }
        );

        return view;
    }


    private void getStepPlan_steps(){
        StepPlan stepPlans=db.getCurDataByUid(u_id);
        int p_step=Integer.valueOf(stepPlans.getP_steps());
        aim.setText("目标"+p_step+"步");
        finish_steps.setText("已完成步数");
        if(step<p_step){
            progressView.setMaxProgress(p_step);
            progressView.setCurrentProgress(step);
        }
        else {
            progressView.setMaxProgress(step);
            progressView.setCurrentProgress(step);
        }

    }

    private void getStepPlan_km(){

        StepPlan stepPlans=db.getCurDataByUid(u_id);
        float p_km=Float.parseFloat(stepPlans.getP_km());

        aim.setText("目标"+p_km+"km");
        finish_steps.setText("已行走距离");
        if(km<p_km){
            progressView.setMaxProgress(p_km);
            progressView.setCurrentProgress(km);
        }
        else {
            progressView.setMaxProgress(km);
            progressView.setCurrentProgress(km);
        }

    }

    private void getStepPlan_ka(){

        StepPlan stepPlans=db.getCurDataByUid(u_id);
        float p_ka=Float.parseFloat(stepPlans.getP_ka());
        aim.setText("目标"+p_ka+"ka");
        finish_steps.setText("已消耗卡路里");
        if(ka<p_ka){
            progressView.setMaxProgress(p_ka);
            progressView.setCurrentProgress(ka);
        }
        else {
            progressView.setMaxProgress(ka);
            progressView.setCurrentProgress(ka);
        }
    }


    private void init(){
        String curDate= TimeUtil.getCurrentDate();
        int i=0;

        xVals=new ArrayList<>();
        yVals=new ArrayList<>();

        //初始化计划
        StepPlan stepPlan=new StepPlan();
        stepPlan.setP_steps("5000");
        stepPlan.setP_km("5");
        stepPlan.setP_ka("500");
        stepPlan.setU_id(1);
        if(db.getCurDataByUid(u_id)==null){

            db.addNewStepPlan(stepPlan);
        }

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
        YAxis leftYAxis = chart.getAxisLeft();
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
        //leftYAxis.setEnabled(false);
        xAxis.setDrawGridLines(true);
        yAxis.setDrawGridLines(true);
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

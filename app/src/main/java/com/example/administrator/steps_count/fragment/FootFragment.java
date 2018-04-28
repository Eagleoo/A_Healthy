package com.example.administrator.steps_count.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.Look_steps;
import com.example.administrator.steps_count.step.MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.StepEntity;
import com.example.administrator.steps_count.step.StepService;
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
import java.util.Timer;
import java.util.TimerTask;

//@SuppressLint("ValidFragment")
public class FootFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout, container, false);

        Intent intent = new Intent();
        //SoilsenerActivity.class为想要跳转的Activity
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);

        return view;
    }

//    @SuppressLint("ValidFragment")
//    public FootFragment(Activity activity){
//        Intent intent=new Intent(activity.getApplicationContext(),StepService.class);
//        activity.getApplicationContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
//        activity.getApplicationContext().startService(intent);
//
//
//    }

}

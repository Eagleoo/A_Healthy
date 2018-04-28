package com.example.administrator.steps_count.step;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Bundle;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
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

public class MainActivity extends Activity {

    private TextView totalStepsTv,look_steps,totalStepsKm,totalStepsKa;
    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private LineChart chart;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private Context mContext=this;
    private DBOpenHelper db;
    private Button btn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        totalStepsTv=(TextView)findViewById(R.id.totalStepsTv);
        totalStepsKm=(TextView)findViewById(R.id.totalStepsKm);
        totalStepsKa=(TextView)findViewById(R.id.totalStepsKa);
        look_steps=(TextView)findViewById(R.id.look_steps);
        btn_add=(Button)findViewById(R.id.btn_add) ;

        init();

        look_steps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, Look_steps.class));
                    }
                }
        );

        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db=new DBOpenHelper(mContext);
                        StepEntity entity1 = new StepEntity();
                        StepEntity entity2 = new StepEntity();
                        StepEntity entity3 = new StepEntity();
                        StepEntity entity4 = new StepEntity();

                        entity1.setCurDate("04-12");
                        entity1.setSteps("3200");
                        db.addNewData(entity1);

                        entity2.setCurDate("04-13");
                        entity2.setSteps("2500");
                        db.addNewData(entity2);

                        entity3.setCurDate("04-14");
                        entity3.setSteps("4500");
                        db.addNewData(entity3);

                        entity4.setCurDate("04-15");
                        entity4.setSteps("2000");
                        db.addNewData(entity4);



                    }
                }
        );

        setupService();

    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);

    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            };

            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                //这里用来获取到Service发来的数据
                case Constant.MSG_FROM_SERVER:

                    DecimalFormat df=new DecimalFormat("#.##");
                    int steps = msg.getData().getInt("steps");
                    //设置的步数
                    totalStepsTv.setText(String.valueOf(steps));
                    totalStepsKm.setText( df.format(steps*0.0007));
                    totalStepsKa.setText(df.format(steps*0.04));
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void init(){
        String curDate= TimeUtil.getCurrentDate();
        int i=0;
        db=new DBOpenHelper(this);
        chart=(LineChart)findViewById(R.id.chart);

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

package com.example.administrator.steps_count.Main_Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.TimeUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Weight_Activity extends AppCompatActivity {
    private LineChart weight_chart;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private LineData data;
    private DBOpenHelper db;
    private ListView weight_list;
    private Button btn_weight_add;
    private ArrayList<Weight> weightArrayList=new ArrayList<>();
    private Context mContext=this;
    private ScrollView sv_weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_layout);

        weight_chart=(LineChart)findViewById(R.id.weight_chart);
        weight_list=(ListView)findViewById(R.id.weight_list);
        btn_weight_add=(Button)findViewById(R.id.btn_weight_add);
        sv_weight=(ScrollView)findViewById(R.id.sv_weight);
        sv_weight.smoothScrollTo(0,0);
        db=new DBOpenHelper(this);
        init();
        weightArrayList=db.getAllWeight();
        Weight_Adapter weight_adapter=new Weight_Adapter(weightArrayList,mContext);
        weight_list.setAdapter(weight_adapter);

        btn_weight_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Weight_Activity.this,Weight_Add_Activity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weight_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(Weight_Activity.this,Weight_Aim_Activity.class));
        finish();
        return true;
    }

    private void init() {
        int i=0;
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("dd");
        String curDate= String.valueOf(TimeUtil.getCurrentDay());
        float w_aim=0;
        Cursor cursor=db.mquery_weight();
        while (cursor.moveToNext()){
            String sDate = cursor.getString(cursor.getColumnIndex("w_date"));

            float weight=Float.parseFloat(cursor.getString(cursor.getColumnIndex("w_weight")));
            if(cursor.getString(cursor.getColumnIndex("w_aim"))==null){
                w_aim=0;
            }
            else {
                w_aim=Float.parseFloat(cursor.getString(cursor.getColumnIndex("w_aim")));
            }
            try {
                Date d1 = sdf.parse(sDate);
                Date d2=sdf.parse(curDate);
                if(Math.abs(((d1.getTime()-d2.getTime())/(24*3600*1000)))<=7){
                    xVals.add(sDate);
                    yVals.add(new Entry(i,weight ));
                    i++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        XAxis xAxis = weight_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYAxis = weight_chart.getAxisRight();
        YAxis leftYAxis = weight_chart.getAxisLeft();
        YAxis yAxis =weight_chart.getAxisLeft();

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
        leftYAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMaxValue(150);
        yAxis.setAxisMinValue(30);
        xAxis.setAxisMinValue(0f);
        yAxis.setAxisMinValue(0f);
        xAxis.setTextSize(15);
        LimitLine yLimitLine = new LimitLine(w_aim,"目标"+w_aim+"kg");
        yLimitLine.setLineColor(Color.parseColor("#009100"));
        yLimitLine.setTextColor(Color.BLACK);
        yLimitLine.setTextSize(10);
        yLimitLine.setLineWidth(1);
        leftYAxis.addLimitLine(yLimitLine);

        weight_chart.setScaleEnabled(false);
        dataSet=new LineDataSet(yVals,"体重变化");
        dataSet.setColor(Color.BLACK);
        dataSet.setCircleRadius(3);
        dataSet.setCircleHoleRadius(3);
        dataSet.setCircleColor(Color.parseColor("#D9B300"));
        data=new LineData(xVals,dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(12);
        weight_chart.setData(data);
        weight_chart.setDescription("历史体重");
        weight_chart.animateX(1000);
        weight_chart.notifyDataSetChanged();
    }


    private class Weight_Adapter extends BaseAdapter {

        private ArrayList<Weight> mData;
        private Context mContext;

        public Weight_Adapter(ArrayList<Weight> mData,Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           ViewHolder holder=null;

            if(view==null) {
                //创建缓冲布局界面，获取界面上的组件
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.weight_adapter, viewGroup, false);
                //  Log.v("AnimalAdapter","改进后调用一次getView方法");
                holder=new ViewHolder();
                holder.adapter_date=(TextView)view.findViewById(R.id.adapter_date);
                holder.adapter_weight=(TextView)view.findViewById(R.id.adapter_weight);
                holder.adapter_text=(TextView)view.findViewById(R.id.adapter_text);

                view.setTag(holder);
            }
            else {
                //用原有组件
                holder=(ViewHolder)view.getTag();
            }
            holder.adapter_date.setText(mData.get(i).getW_date());
            holder.adapter_weight.setText(mData.get(i).getWeight());
            holder.adapter_text.setText(mData.get(i).getW_text());


            return view;
        }
        public  final class ViewHolder {

            TextView adapter_date;
            TextView adapter_weight;
            TextView adapter_text;
        }
    }
}

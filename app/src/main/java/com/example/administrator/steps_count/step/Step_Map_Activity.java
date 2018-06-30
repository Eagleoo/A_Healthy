package com.example.administrator.steps_count.step;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.administrator.steps_count.Activity.CheckPermissionsActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.LatLng_1;
import com.example.administrator.steps_count.step.Map;
import com.example.administrator.steps_count.step.Map_DBHelper;
import com.example.administrator.steps_count.step.Step_Map;
import com.example.administrator.steps_count.step.TimeUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class Step_Map_Activity extends CheckPermissionsActivity implements LocationSource, AMapLocationListener {

    private MapView mMapView = null;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    //定位功能
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private Map_DBHelper db;
    private ImageView map_record,back_map,speed_about;
    private Button start;
    private CalendarView calendar;
    private List<LatLng_1> latLng1List;
    private List<LatLng_1> list;
    private LinearLayout record_data;
    private TextView tv_run_km,tv_run_ka,tv_run_time,tv_run_speed,record_date;
    private DecimalFormat df;
    private String select_date;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_map_layout);
        mMapView = (MapView) findViewById(R.id.step_map);
        map_record=(ImageView)findViewById(R.id.map_record);
        back_map=(ImageView)findViewById(R.id.back_map);
        speed_about=(ImageView)findViewById(R.id.speed_about);
        tv_run_km=(TextView)findViewById(R.id.tv_run_km);
        tv_run_ka=(TextView)findViewById(R.id.tv_run_ka);
        tv_run_time=(TextView)findViewById(R.id.tv_run_time);
        tv_run_speed=(TextView)findViewById(R.id.tv_run_speed);
        record_date=(TextView)findViewById(R.id.record_date);
        start=(Button)findViewById(R.id.start);
        record_data=(LinearLayout)findViewById(R.id.record_data);
        mMapView.onCreate(savedInstanceState);
        db=new Map_DBHelper(this);
        df=new DecimalFormat("#.##");
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();

        }
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        myLocationStyle = new MyLocationStyle();

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        final View view = LayoutInflater.from(Step_Map_Activity.this).inflate(R.layout.m_calendar, null, false);
        calendar=(CalendarView)view.findViewById(R.id.calendar);
        map_record.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        latLng1List=new ArrayList<>();
                        latLng1List=Select_Calendar();
                        new AlertDialog.Builder(Step_Map_Activity.this).setTitle("查看跑步记录")
                                .setIcon(R.drawable.run_1)
                                .setMessage("选择查看的日期")
                                .setView(view)
                                .setPositiveButton("查看", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(Select_Calendar()!=null){
                                            latLng1List=Select_Calendar();
                                        }
                                        else {
                                            latLng1List=db.getCurLatLngByDate(TimeUtil.getCurrentDate());
                                        }
                                        if (latLng1List.size()==0){
                                            Toast.makeText(Step_Map_Activity.this, "这天没有跑步哦！", Toast.LENGTH_SHORT).show();
                                            record_data.setVisibility(View.GONE);
                                            aMap.clear();
                                        }
                                        else {
                                            aMap.clear();
                                            for (int i=0;i<latLng1List.size();i++){
                                                if (i+1<latLng1List.size()){
                                                    LatLng latLng1=new LatLng(Double.valueOf(latLng1List.get(i).getLatitude()),Double.valueOf(latLng1List.get(i).getLongitude()));
                                                    LatLng latLng2=new LatLng(Double.valueOf(latLng1List.get(i+1).getLatitude()),Double.valueOf(latLng1List.get(i+1).getLongitude()));
                                                    setUpMap(latLng1,latLng2);
                                                }
                                            }
                                            record_data.setVisibility(View.VISIBLE);
                                            showData();
                                            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.valueOf(latLng1List.get(latLng1List.size()/2).getLatitude()),Double.valueOf(latLng1List.get(latLng1List.size()/2).getLongitude()))));
                                        }

                                        destoryView(view);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        destoryView(view);
                                    }
                                })
                                .setCancelable(false)
                                .show();

                    }
                }
        );

        start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Step_Map_Activity.this,Run_Timer.class));
                        finish();
                    }
                }
        );

        back_map.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        speed_about.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(Step_Map_Activity.this).setTitle("有关说明")
                                .setIcon(R.drawable.run_1)
                                .setMessage(R.string.speed_about)
                                .setNegativeButton("知道了", null)
                                .show();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //添加图钉
                    //aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }

            }
//            else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError", "location Error, ErrCode:"
//                        + amapLocation.getErrorCode() + ", errInfo:"
//                        + amapLocation.getErrorInfo());
//
//            }
        }
    }

//    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
//    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
//        //设置图钉选项
//        MarkerOptions options = new MarkerOptions();
//        //图标
//        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
//        //位置
//        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//        //标题
//        options.title(buffer.toString());
//        //子标题
//        options.snippet("这里好火");
//        //设置多少帧刷新一次图片资源
//        options.period(60);
//
//        return options;
//
//
//    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
    }


    /**绘制两个坐标点之间的线段,从以前位置到现在位置*/
    private void setUpMap(LatLng oldData,LatLng newData ) {
        if(AMapUtils.calculateLineDistance(oldData, newData)<50){
            // 绘制一个大地曲线
            aMap.addPolyline((new PolylineOptions())
                    .add(oldData, newData)
                    .geodesic(true).color(Color.parseColor("#01B468")));
        }


    }

    private List<LatLng_1> Select_Calendar(){
        calendar.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        list=new ArrayList<>();
                        if(month<10&&dayOfMonth<10){
                            select_date="0"+(month+1)+"-"+"0"+dayOfMonth;
                            list=db.getCurLatLngByDate(select_date);
                        }
                        else if(dayOfMonth>=10&&month<10){
                            select_date="0"+(month+1)+"-"+dayOfMonth;
                            list=db.getCurLatLngByDate(select_date);
                        }
                        else if(month>=10&&dayOfMonth<10){
                            select_date=(month+1)+"-"+"0"+dayOfMonth;
                            list=db.getCurLatLngByDate(select_date);
                        }
                        else if(month>=10&&dayOfMonth>=10){
                            select_date=(month+1)+"-"+dayOfMonth;
                            list=db.getCurLatLngByDate(select_date);

                        }
                    }
                }
        );
        return list;
    }

    private void showData(){
        int time_min=0;//总分
        int time_ss=0;//总秒
        float temp=0;//最大速度
        List<Map> list=new ArrayList<>();
        if(Select_Calendar()==null){
            list=db.getCurUserDateByDate(TimeUtil.getCurrentDate());
            record_date.setText(TimeUtil.getCurrentDate());
        }
       else {
            list=db.getCurUserDateByDate(select_date);
            record_date.setText(select_date);
        }

        if (list.size()!=0){
            temp=Float.valueOf(list.get(0).getSpeed());
            tv_run_km.setText(returnInstance());
            tv_run_ka.setText(df.format(Float.valueOf(returnInstance())*60*1.036*0.001));
            for(int i=0;i<list.size();i++){
                time_min+=Integer.valueOf(list.get(i).getTime().split(":")[0]);
                time_ss+=Integer.valueOf(list.get(i).getTime().split(":")[1]);
                if (time_ss>=60){
                    time_min+=1;
                    time_ss-=60;
                }
                if(list.size()==1){
                    temp=Float.valueOf(list.get(i).getSpeed());
                }
                if(i+1<list.size()){
                    if(temp<Float.valueOf(list.get(i+1).getSpeed())){
                        temp=Float.valueOf(list.get(i+1).getSpeed());
                    }

                }
            }
            String string_time=time_min+":"+time_ss;
            tv_run_time.setText(string_time);
            tv_run_speed.setText(String.valueOf(temp));
        }

    }

    private String returnInstance(){
        float instance=0;
        for(int i=0;i<latLng1List.size();i++){
            if (i+1<latLng1List.size()) {
                LatLng latLng1 = new LatLng(Double.valueOf(latLng1List.get(i).getLatitude()), Double.valueOf(latLng1List.get(i).getLongitude()));
                LatLng latLng2 = new LatLng(Double.valueOf(latLng1List.get(i + 1).getLatitude()), Double.valueOf(latLng1List.get(i + 1).getLongitude()));
                if(AMapUtils.calculateLineDistance(latLng1,latLng2)<50){
                    instance += AMapUtils.calculateLineDistance(latLng1, latLng2);
                }
            }
        }
        return df.format(instance);
    }

    private void destoryView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
    }
}

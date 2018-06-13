package com.example.administrator.steps_count.step;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Step_Map extends CheckPermissionsActivity implements LocationSource, AMapLocationListener{
    private MapView mMapView = null;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    //定位功能
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private boolean isBtn=true;
    //以前的定位点
    private LatLng oldLatLng;
//    private LatLng latLng1=new LatLng(30.67,104.06);
//    private LatLng latLng2=new LatLng(39.9,116.3);
    private TextView run_speed,run_km,run_ka,tv_pause;
    private Chronometer run_time;
    private LinearLayout stop_run,pause_run;
    private Map_DBHelper db;
    private ImageView img_pause;
    private DecimalFormat df1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_map);
        run_speed=(TextView)findViewById(R.id.run_speed);
        run_km=(TextView)findViewById(R.id.run_km);
        run_ka=(TextView)findViewById(R.id.run_ka);
        tv_pause=(TextView)findViewById(R.id.tv_pause);
        img_pause=(ImageView)findViewById(R.id.img_pause);
        mMapView = (MapView) findViewById(R.id.map);
        run_time= (Chronometer) findViewById(R.id.run_time);
        stop_run=(LinearLayout)findViewById(R.id.stop_run);
        pause_run=(LinearLayout)findViewById(R.id.pause_run);
        mMapView.onCreate(savedInstanceState);
        db=new Map_DBHelper(this);

        run_time.start();

        stop_run.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(Step_Map.this).setTitle("结束跑步")
                                .setIcon(R.drawable.run_1)
                                .setMessage("确定结束吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Map map=new Map();
                                        map.setTime(run_time.getText().toString());
                                        map.setSpeed(run_speed.getText().toString());
                                        map.setKm(run_km.getText().toString());
                                        map.setKa(run_ka.getText().toString());
                                        map.setDate(TimeUtil.getCurrentDate());
                                        db.addNewMapData(map);
                                        run_time.setBase(SystemClock.elapsedRealtime());
                                        run_time.stop();
                                        finish();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                }
        );

        pause_run.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isBtn){
                            run_time.stop();
                            tv_pause.setText("继续跑步");
                            img_pause.setBackgroundResource(R.drawable.start);
                            isBtn=false;

                        }
                        else {
                            run_time.setBase(convertStrTimeToLong(run_time.getText().toString()));
                            run_time.start();
                            tv_pause.setText("暂停跑步");
                            img_pause.setBackgroundResource(R.drawable.pause);
                            isBtn=true;
                        }


                    }
                }
        );


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
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
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


                LatLng newLatLng =new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //记录第一次的定位信息
                    oldLatLng = newLatLng;
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //添加图钉
                    aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }

                //位置有变化
                if(oldLatLng != newLatLng){

                    df1=new DecimalFormat();
                    oldLatLng = newLatLng;
                    run_km.setText(returnInstance());
                    run_ka.setText(df1.format(Float.valueOf(returnInstance())*60*1.036*0.001));
                    LatLng_1 latLng_1=new LatLng_1(String.valueOf(newLatLng.latitude),String.valueOf(newLatLng.longitude),TimeUtil.getCurrentDate());
                    run_speed.setText(String.valueOf(amapLocation.getSpeed()));
                    if (amapLocation.getSpeed()==0){
                        run_speed.setText("0.0");

                    }
                    else {
                        setUpMap( oldLatLng , newLatLng );
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                        db=new Map_DBHelper(this);
                        db.addNewLatLngData(latLng_1);
                        db.close();
                    }


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

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.delete));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;


    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
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

        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.parseColor("#01B468")));

    }

    protected long convertStrTimeToLong(String strTime) {
        // TODO Auto-generated method stub
        String []timeArry=strTime.split(":");
        long longTime=0;
        if (timeArry.length==2) {//如果时间是MM:SS格式
            longTime=Integer.parseInt(timeArry[0])*1000*60+Integer.parseInt(timeArry[1])*1000;
        }else if (timeArry.length==3){//如果时间是HH:MM:SS格式
            longTime=Integer.parseInt(timeArry[0])*1000*60*60+Integer.parseInt(timeArry[1])
                    *1000*60+Integer.parseInt(timeArry[0])*1000;
        }
        return SystemClock.elapsedRealtime()-longTime;//减去计时器一直在计时的时间就实现了从上一次开始计时
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(this, "跑步中，不能退出，请先暂停跑步！", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private String returnInstance(){
        float instance=0;//两点间距离
        DecimalFormat df=new DecimalFormat("#.##");
        List<LatLng_1> latLng_1List=new ArrayList<>();
        latLng_1List=db.getCurLatLngByDate(TimeUtil.getCurrentDate());
        for(int i=0;i<latLng_1List.size();i++){
            if (i+1<latLng_1List.size()) {
                LatLng latLng1 = new LatLng(Double.valueOf(latLng_1List.get(i).getLatitude()), Double.valueOf(latLng_1List.get(i).getLongitude()));
                LatLng latLng2 = new LatLng(Double.valueOf(latLng_1List.get(i + 1).getLatitude()), Double.valueOf(latLng_1List.get(i + 1).getLongitude()));
                instance += AMapUtils.calculateLineDistance(latLng1, latLng2);
            }
        }
        return df.format(instance);
    }
}

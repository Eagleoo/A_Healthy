package com.example.administrator.steps_count.step;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Bundle;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.administrator.steps_count.Activity.CheckPermissionsActivity;
import com.example.administrator.steps_count.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Step_MainActivity extends CheckPermissionsActivity implements LocationSource, AMapLocationListener {

    private TextView totalStepsTv,look_steps,totalStepsKm,totalStepsKa;
    private ImageView img_step;
    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private Button btn_run;
    private MapView mMapView = null;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    //private final static LatLng_1 CHENGDU = new LatLng_1(30.679879, 104.064855);
    //定位功能
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private DBOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        totalStepsTv=(TextView)findViewById(R.id.totalStepsTv);
        totalStepsKm=(TextView)findViewById(R.id.totalStepsKm);
        totalStepsKa=(TextView)findViewById(R.id.totalStepsKa);
        look_steps=(TextView)findViewById(R.id.look_steps);
        img_step=(ImageView) findViewById(R.id.img_step) ;
        btn_run=(Button)findViewById(R.id.btn_run);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        db=new DBOpenHelper(this);


        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
            //aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CHENGDU, 14));

//            //修改地图的中心点位置
//            CameraPosition cp = aMap.getCameraPosition();
//            CameraPosition cpNew = CameraPosition.fromLatLngZoom(CHENGDU, cp.zoom);
//            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cpNew);
//            aMap.moveCamera(cu);

            //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CHENGDU, 14));
            //aMap.invalidate();// 刷新地图
        }

        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.delete));
//        myLocationStyle.strokeColor(Color.BLACK);
//        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
//        myLocationStyle.strokeWidth(1.0f);

        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        initLoc();

        look_steps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Step_MainActivity.this, Look_steps.class));
                    }
                }
        );

        btn_run.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Step_MainActivity.this, Step_Map.class));
                    }
                }
        );

//        img_step.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        StepEntity entity1 = new StepEntity();
//                        StepEntity entity2 = new StepEntity();
//                        StepEntity entity3 = new StepEntity();
//                        StepEntity entity4 = new StepEntity();
//                        StepEntity entity5 = new StepEntity();
//                        StepEntity entity6 = new StepEntity();
//                        StepEntity entity7 = new StepEntity();
//                        StepEntity entity8 = new StepEntity();
//
//                        entity1.setCurDate("05-17");
//                        entity1.setSteps("3200");
//                        entity1.setTotalStepsKm("20");
//                        entity1.setTotalStepsKa("500");
//                        db.addNewData(entity1);
//
//                        entity2.setCurDate("05-18");
//                        entity2.setSteps("2500");
//                        entity2.setTotalStepsKm("20");
//                        entity2.setTotalStepsKa("500");
//                        db.addNewData(entity2);
//
//                        entity3.setCurDate("05-19");
//                        entity3.setSteps("4500");
//                        entity3.setTotalStepsKm("20");
//                        entity3.setTotalStepsKa("500");
//                        db.addNewData(entity3);
//
//                        entity4.setCurDate("05-20");
//                        entity4.setSteps("2000");
//                        entity4.setTotalStepsKm("20");
//                        entity4.setTotalStepsKa("500");
//                        db.addNewData(entity4);
//
//                        entity5.setCurDate("05-21");
//                        entity5.setSteps("3200");
//                        entity5.setTotalStepsKm("20");
//                        entity5.setTotalStepsKa("500");
//                        db.addNewData(entity5);
//
//                        entity6.setCurDate("05-22");
//                        entity6.setSteps("9000");
//                        entity6.setTotalStepsKm("20");
//                        entity6.setTotalStepsKa("500");
//                        db.addNewData(entity6);
//
//                        entity7.setCurDate("05-23");
//                        entity7.setSteps("4500");
//                        entity7.setTotalStepsKm("20");
//                        entity7.setTotalStepsKa("500");
//                        db.addNewData(entity7);
//
//                        entity8.setCurDate("05-24");
//                        entity8.setSteps("5600");
//                        entity8.setTotalStepsKm("20");
//                        entity8.setTotalStepsKa("500");
//                        db.addNewData(entity8);
//
//
//
//                    }
//                }
//        );

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
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    public void initLoc(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener((AMapLocationListener) this);
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
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }


}

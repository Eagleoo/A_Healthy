package com.example.administrator.steps_count.Activity;


import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.model.Dynamics;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.tools.Json_Tools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Publishdy extends AppCompatActivity {

    public static final int TAKE_PHOTO = 111;
    private ImageView dy_img;
    private static Uri imagurl;
    private EditText dynamic_content;
    private TextView dynamic_cancle,location,dynamic_location;
    private File outputImage,filee;
    private Button btn_dynamic;
    private long t;
    private String time,times;
    private StringBuffer buffer;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    private Bitmap bitmap = null;
    private Bitmap bmp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishdy);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dy_img=(ImageView)findViewById(R.id.dy_img);
        dynamic_content=(EditText)findViewById(R.id.dynamic_content);
        btn_dynamic=(Button)findViewById(R.id.btn_dynamic);
        dynamic_cancle=(TextView)findViewById(R.id.dynamic_cancle);
        dynamic_location=(TextView)findViewById(R.id.dynamic_location);
        location=(TextView)findViewById(R.id.locat);

        getCurrentLocationLatLng();
        //获取时间
        t=System.currentTimeMillis();
        Date date=new Date(t);
        SimpleDateFormat format=new SimpleDateFormat("MM-dd  HH:mm:ss");
        SimpleDateFormat format1=new SimpleDateFormat("MMddHHmmssSSS");
        time=format.format(date);
        times=format1.format(date);
        dy_img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        outputImage = new File(getExternalCacheDir(), Frag_MainActivity.user.getUser_id() + "output_image.jpg");
                        try {
                            if (outputImage.exists()){
                            outputImage.delete();
                        }
                            outputImage.createNewFile();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(Build.VERSION.SDK_INT>=24){
                            imagurl=FileProvider.getUriForFile(Publishdy.this, "com.example.administrator.steps_count", outputImage);
                        }
                        else {
                            imagurl=Uri.fromFile(outputImage);
                        }


                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imagurl);
                        startActivityForResult(intent,TAKE_PHOTO);

                    }
                }
        );

        btn_dynamic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Insert_Dynamic();
                        Toast.makeText(Publishdy.this, "发表成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );

        dynamic_cancle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamic_location.setText(buffer);
                    }
                }
        );

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {


            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imagurl));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
                dy_img.setImageBitmap(bitmap);

            bmp = compressImage(bitmap);
            filee =  saveBitmapFile(bmp);
            }

    }



    public void Insert_Dynamic(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();
                    FormBody.Builder requestbuilder = new FormBody.Builder();
                    RequestBody builder = requestbuilder
                            .add("id",String.valueOf(Frag_MainActivity.user.getUser_id()))
                            .add("author", Frag_MainActivity.user.getUsername())
                            .add("time",time.trim())
                            .add("content",dynamic_content.getText().toString().trim())
                            .add("dynamicLocation",dynamic_location.getText().toString())
                            .add("img", MeFragment.string)
                            .add("img_content",Constant.CONNECTURL+"circle_img/"+times+outputImage.getName())
                            .build();


                    Request request = new Request.Builder()
                            .url(Constant.CONNECTURL + "circle/servlet/dynamicDao_insert").post(builder)
                            .build();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();

                            RequestBody requestBody = null;
                            try {
                                requestBody = RequestBody.create(MediaType.parse("application/octet0stream"),filee);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Request request = new Request.Builder()
                                    .url(Constant.CONNECTURL + "circle/servlet/questionDao_file").post(requestBody).addHeader("imagename",times+outputImage.getName())
                                    .build();

                            try {
                                client.newCall(request).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getCurrentLocationLatLng(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(5000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        buffer = new StringBuffer();
                        buffer.append(amapLocation.getProvince() + "" + amapLocation.getCity() +
                                "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() +
                                "" + amapLocation.getStreetNum());
                    }
                }

        }
    };

    public static void MyRecycle(Bitmap bmp){
        if(!bmp.isRecycled() && null!=bmp){
            bmp=null;
        }
    }


    @Override
    protected void onDestroy() {
        dy_img.setImageBitmap(null);
        if(bitmap!=null){
            Publishdy.MyRecycle(bitmap);
        }

        super.onDestroy();
        if(mLocationClient!=null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mLocationClient!=null) {
            mLocationClient.startLocation(); // 启动定位
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationClient!=null) {
            mLocationClient.stopLocation();//停止定位
        }
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public File saveBitmapFile(Bitmap bitmap){
        File file=new File(getExternalCacheDir(),"output_image.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}

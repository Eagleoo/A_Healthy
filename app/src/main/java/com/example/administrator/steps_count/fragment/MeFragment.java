package com.example.administrator.steps_count.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.LoginActivity;

import com.example.administrator.steps_count.Activity.PerMassageActivity;
import com.example.administrator.steps_count.Activity.Publishdy;
import com.example.administrator.steps_count.Activity.SettingActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.Mall_collect_Activity;
import com.example.administrator.steps_count.mall.Order_Management_Activity;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;

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

import static com.example.administrator.steps_count.Activity.Publishdy.TAKE_PHOTO;
import static com.example.administrator.steps_count.Activity.Publishdy.compressImage;

public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView txt_login;
    private TextView per_message;
    private ImageView head;
    private TextView setting;

    private RadioButton radio_all;
    private RadioButton radio_pay;
    private RadioButton radio_send;
    private RadioButton radio_recevive;

    private TextView collect;
    private TextView insertdy;
    private static Uri imagurl;
    public static final int TAKE_PHONTO = 1;
    public static final int CHOOSE_PHONTO = 2;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog;
    private String[] address = new String[]{"相册", "拍照"};
    private File outputImage,filee;
    private long t;
    private String time,times;
    public static String string;
    private String json;
    private Bitmap bmp = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //StricMode:严苛模式，用以检测策略违例，若代码中有违例现象，则可以发出警告
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //文件uri暴露 使用detectFileUriExposure()
        builder.detectFileUriExposure();
        View view = inflater.inflate(R.layout.person_layout, container, false);
        txt_login = (TextView) view.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
        per_message = (TextView) view.findViewById(R.id.per_message);
        per_message.setOnClickListener(this);
        head = (ImageView) view.findViewById(R.id.head);
        head.setOnClickListener(this);
        setting = (TextView) view.findViewById(R.id.setting);
        setting.setOnClickListener(this);
        radio_recevive= (RadioButton) view.findViewById(R.id.radio_recevive);
        radio_all= (RadioButton) view.findViewById(R.id.radio_all);
        radio_pay= (RadioButton) view.findViewById(R.id.radio_pay);
        radio_send= (RadioButton) view.findViewById(R.id.radio_send);

        radio_send.setOnClickListener(this);
        radio_recevive.setOnClickListener(this);
        radio_all.setOnClickListener(this);
        radio_pay.setOnClickListener(this);
        collect = (TextView) view.findViewById(R.id.collect);
        collect.setOnClickListener(this);
        insertdy = (TextView) view.findViewById(R.id.insertdy);
        insertdy.setOnClickListener(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);

        //获取时间
        t=System.currentTimeMillis();
        Date date=new Date(t);
        SimpleDateFormat format1=new SimpleDateFormat("MMddHHmmssSSS");
        times=format1.format(date);

        if (Frag_MainActivity.user != null) {
            txt_login.setText(Frag_MainActivity.user.getUsername());
            txt_login.setEnabled(false);
            Load_Portrait();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head:
                if (Frag_MainActivity.user == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                    alertDialog = builder.setIcon(R.drawable.baidu).
                            setTitle("系统提示")
                            .setItems(address, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    if (address[which] == "拍照") {
                                        outputImage = new File(getActivity().getExternalCacheDir(), Frag_MainActivity.user.getUser_id()+ "output_image.jpg");
                                        try {
                                            if (outputImage.exists()){
                                                outputImage.delete();
                                            }
                                            outputImage.createNewFile();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if(Build.VERSION.SDK_INT>=24){
                                            imagurl= FileProvider.getUriForFile(getActivity(), "com.example.administrator.steps_count", outputImage);
                                        }
                                        else {
                                            imagurl=Uri.fromFile(outputImage);
                                        }


                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imagurl);
                                        startActivityForResult(intent,TAKE_PHOTO);
                                    } else if (address[which] == "相册") {
                                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                                        } else {
                                            openAlnum();
                                        }
                                    }
                                }

                            })

                            .setNegativeButton("取消", null)
                            .create();
                    alertDialog.show();
                }


                break;
            case R.id.txt_login:
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
                break;
            case R.id.per_message:

                if (Frag_MainActivity.user != null) {
                    Intent message = new Intent(getActivity(), PerMassageActivity.class);
                    startActivity(message);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.radio_all:
                if (Frag_MainActivity.user == null)
                {
                    Toast.makeText(getContext(),"您还未登录，请先登录再试！",Toast.LENGTH_LONG).show();
                }else {
                Intent intent = new Intent(getContext(), Order_Management_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("0x0", "0");
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.radio_pay:
                if (Frag_MainActivity.user == null)
                {
                    Toast.makeText(getContext(),"您还未登录，请先登录再试！",Toast.LENGTH_LONG).show();
                }else {                Intent intent2 = new Intent(getContext(), Order_Management_Activity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("0x0", "1");
                intent2.putExtras(bundle2);
                startActivity(intent2);
                }
                break;
            case R.id.radio_send:
                if (Frag_MainActivity.user == null)
                {
                    Toast.makeText(getContext(),"您还未登录，请先登录再试！",Toast.LENGTH_LONG).show();
                }else {
                Intent intent3=new Intent(getContext(),Order_Management_Activity.class);
                Bundle bundle3=new Bundle();
                bundle3.putString("0x0","2");
                intent3.putExtras(bundle3);
                startActivity(intent3);
                }
                break;
            case R.id.radio_recevive:
                if (Frag_MainActivity.user == null)
                {
                    Toast.makeText(getContext(),"您还未登录，请先登录再试！",Toast.LENGTH_LONG).show();
                }else {
                Intent intent4 = new Intent(getContext(), Order_Management_Activity.class);
                Bundle bundle4 = new Bundle();
                bundle4.putString("0x0", "3");
                intent4.putExtras(bundle4);
                startActivity(intent4);
                }
                break;
            case R.id.collect:
                if (Frag_MainActivity.user != null) {
                    Intent intent5 = new Intent(getActivity(), Mall_collect_Activity.class);
                    startActivity(intent5);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.setting:
                Intent setting = new Intent(getActivity(), SettingActivity.class);
                startActivity(setting);
                break;
            case R.id.insertdy:
                if (Frag_MainActivity.user != null) {
                    Intent inserdy = new Intent(getActivity(), Publishdy.class);
                    startActivity(inserdy);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
        }

    }

    private void openAlnum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHONTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlnum();
                } else {
                    Toast.makeText(getActivity(), "您权限被限制", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imagurl));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            head.setImageBitmap(bitmap);
            bmp = compressImage(bitmap);
            filee =  saveBitmapFile(bmp);
            Insert_Dynamic();
        }
        switch (requestCode) {

            case CHOOSE_PHONTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageInKieKat(data);
                    } else {

                        handleImageBeforeKitkat(data);
                    }
                }
                break;
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
                            .add("username", Frag_MainActivity.user.getUsername())
                            .add("portrait",Constant.CONNECTURL+"portrait_img/"+times+outputImage.getName())
                            .build();


                    Request request = new Request.Builder()
                            .url(Constant.CONNECTURL + "circle/servlet/Update_Portrait_info").post(builder)
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
                                    .url(Constant.CONNECTURL + "circle/servlet/Update_Portrait").post(requestBody).addHeader("imagename",times+outputImage.getName())
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

    private void Load_Portrait(){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("username", Frag_MainActivity.user.getUsername()).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Show_UserInfo")
                .post(requestBody)//传递请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    json = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Json_Tools json_tools=new Json_Tools();
                            try {
                                string=json_tools.Json_To_String(json);
                                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
                                ImageLoader.getInstance().init(configuration);
                                ImageLoader.getInstance().displayImage(string,head);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public File saveBitmapFile(Bitmap bitmap){
        File file=new File(getActivity().getExternalCacheDir(),"output_image.jpg");//将要保存图片的路径
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

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imgepath = getImagePath(uri, null);
        displayImage(imgepath);
    }

    private void handleImageInKieKat(Intent data) {
        String imagepath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String docid = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docid.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagepath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docid));
                imagepath = getImagePath(contenturi, null);

            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagepath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagepath = uri.getPath();
            }
            displayImage(imagepath);

        }
    }

    private void displayImage(String imagepath) {
        if (imagepath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
            head.setImageBitmap(bitmap);
            String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/ClientUpdate?function=updateimag"
                    + "&url=" + imagepath + "&username=" + Frag_MainActivity.user.getUsername();
            ReadURL(url);


        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(externalContentUri, null, selection, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }

        return path;
    }



    public void ReadURL(final String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resultCode = connection.getResponseCode();
                    StringBuffer response = null;
                    if (HttpURLConnection.HTTP_OK == resultCode) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        response = new StringBuffer();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }

                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "1";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {

            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);

    }

    public void SeReadURL(final String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resultCode = connection.getResponseCode();
                    StringBuffer response = null;
                    if (HttpURLConnection.HTTP_OK == resultCode) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        response = new StringBuffer();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }

                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "1";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {

                Bitmap bitmap = BitmapFactory.decodeFile(s);
                head.setImageBitmap(bitmap);


            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);

    }
    public void URL(final String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resultCode = connection.getResponseCode();
                    StringBuffer response = null;
                    if (HttpURLConnection.HTTP_OK == resultCode) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        response = new StringBuffer();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }

                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "1";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                Uri uri = Uri.parse((String) s);
                try {
                    Bitmap  bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                    head.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);

    }
}

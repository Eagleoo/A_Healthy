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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.AllInformation;
import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.LoginActivity;
import com.example.administrator.steps_count.Activity.PerCollect;
import com.example.administrator.steps_count.Activity.PerMassageActivity;
import com.example.administrator.steps_count.Activity.Publishdy;
import com.example.administrator.steps_count.Activity.SettingActivity;
import com.example.administrator.steps_count.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView txt_login;
    private TextView per_message;
    private ImageView head;
    private TextView setting;
    private ImageView order;
    private ImageView isreceive;
    private ImageView payment;
    private ImageView issend;
    private TextView collect;
    private TextView insertdy;
    private Uri imagurl;
    public static final int TAKE_PHONTO = 1;
    public static final int CHOOSE_PHONTO = 2;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog;
    private String[] address = new String[]{"相册", "拍照"};

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
        order = (ImageView) view.findViewById(R.id.order);
        order.setOnClickListener(this);
        isreceive = (ImageView) view.findViewById(R.id.isreceive);
        isreceive.setOnClickListener(this);
        payment = (ImageView) view.findViewById(R.id.payment);
        payment.setOnClickListener(this);
        issend = (ImageView) view.findViewById(R.id.issend);
        issend.setOnClickListener(this);
        collect = (TextView) view.findViewById(R.id.collect);
        collect.setOnClickListener(this);
        insertdy = (TextView) view.findViewById(R.id.insertdy);
        insertdy.setOnClickListener(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);

        if (Frag_MainActivity.user != null) {
            txt_login.setText(Frag_MainActivity.user.getUsername());
            txt_login.setEnabled(false);
            if (Frag_MainActivity.user.getPortrait().startsWith("/storage")) {

                String url = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/ClientUpdate?function=imag&username=" + Frag_MainActivity.user.getUsername();
                SeReadURL(url);
            } else {
                ImageLoader.getInstance().displayImage(Frag_MainActivity.user.getPortrait().trim(), head);
            }
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
                                        File outputImage = new File(getActivity().getExternalCacheDir(), Frag_MainActivity.user.getUsername() + "output_image.jpg");
                                        outputImage.delete();

                                        try {
                                            outputImage.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        imagurl = Uri.fromFile(outputImage);
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagurl);
                                        startActivityForResult(intent, TAKE_PHONTO);
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
            case R.id.order:
                if (Frag_MainActivity.user != null) {
                    Intent intent1 = new Intent(getActivity(), AllInformation.class);
                    intent1.putExtra("item","0");
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.issend:
                if (Frag_MainActivity.user != null) {
                    Intent intent1 = new Intent(getActivity(), AllInformation.class);
                    intent1.putExtra("item","2");
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.isreceive:
                if (Frag_MainActivity.user != null) {
                    Intent intent1 = new Intent(getActivity(), AllInformation.class);
                    intent1.putExtra("item","1");
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.payment:
                if (Frag_MainActivity.user != null) {
                    Intent intent1 = new Intent(getActivity(), AllInformation.class);
                    intent1.putExtra("item","3");
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.collect:
                if (Frag_MainActivity.user != null) {
                    Intent intent5 = new Intent(getActivity(), PerCollect.class);
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
        switch (requestCode) {
            case TAKE_PHONTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imagurl));
                        head.setImageBitmap(bitmap);
                        String url = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/ClientUpdate?function=updateimag"
                                + "&url=" + imagurl.toString() + "&username=" + Frag_MainActivity.user.getUsername();
                        ReadURL(url);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
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
            String url = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/ClientUpdate?function=updateimag"
                    + "&url=" + imagepath + "&username=" + Frag_MainActivity.user.getUsername();
            ReadURL(url);
            head.setImageBitmap(bitmap);


        } else {
            Toast.makeText(getActivity(), "获取图片失败", Toast.LENGTH_LONG).show();
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
}

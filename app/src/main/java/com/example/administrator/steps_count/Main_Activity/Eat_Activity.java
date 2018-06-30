package com.example.administrator.steps_count.Main_Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.Frg_Food1;
import com.example.administrator.steps_count.fragment.MainFragment;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.ProgressView;
import com.example.administrator.steps_count.step.StepEntity;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.step.User_DBOpenHelper;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Eat_Activity extends AppCompatActivity implements View.OnClickListener{

    private RadioButton rd_breakfast,rd_lunch,rd_dinner,rd_add;
    private ListView breakfast_list,lunch_list,dinner_list,add_list;
    private TextView breakfast_ka,lunch_ka,dinner_ka,add_ka,total_ka,use_ka,tv_aim_ka;
    private Eat_Adapter break_eat_adapter,lunch_eat_adapter,dinner_eat_adapter,add_eat_adapter;
    private ImageView eat_back;
    private List<U_Food> UFoodList=new ArrayList<U_Food>();
    private  List<U_Food> list1,list2,list3,list4;
    private ScrollView sv;
    private double breakfast_double,lunch_double,dinner_double,add_double;
    private StepEntity stepEntity;
    private DBOpenHelper db;
    private ProgressView progressView;
    private float temp_cur_ka=0;
    private User_DBOpenHelper u_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_layout);

        rd_breakfast= (RadioButton) findViewById(R.id.rd_breakfast);
        rd_lunch= (RadioButton) findViewById(R.id.rd_lunch);
        rd_dinner= (RadioButton) findViewById(R.id.rd_dinner);
        rd_add= (RadioButton) findViewById(R.id.rd_add);
        breakfast_list=(ListView)findViewById(R.id.breakfast_list);
        lunch_list=(ListView)findViewById(R.id.lunch_list);
        dinner_list=(ListView)findViewById(R.id.dinner_list);
        add_list=(ListView)findViewById(R.id.add_list);
        breakfast_ka=(TextView)findViewById(R.id.breakfast_ka);
        lunch_ka=(TextView)findViewById(R.id.lunch_ka);
        dinner_ka=(TextView)findViewById(R.id.dinner_ka);
        add_ka=(TextView)findViewById(R.id.add_ka);
        total_ka=(TextView)findViewById(R.id.total_ka);
        use_ka=(TextView)findViewById(R.id.use_ka);
        tv_aim_ka=(TextView)findViewById(R.id.tv_aim_ka);
        eat_back=(ImageView)findViewById(R.id.eat_back);
        sv=(ScrollView)findViewById(R.id.sv);
        progressView=(ProgressView)findViewById(R.id.progressView);
        progressView.setColor1();
        sv.smoothScrollTo(0,0);

        db=new DBOpenHelper(this);
        u_db=new User_DBOpenHelper(this);
        stepEntity=db.getCurDataByDate(TimeUtil.getCurrentDate());
        use_ka.setText(stepEntity.getTotalStepsKa());
        list1=new ArrayList<U_Food>();
        list2=new ArrayList<U_Food>();
        list3=new ArrayList<U_Food>();
        list4=new ArrayList<U_Food>();
        if(u_db.getAimKa()!=null){
            tv_aim_ka.setText(u_db.getAimKa());
            Log.e("sssss",u_db.getAimKa());
        }

        LoadFoodData();
        SavaData();


        break_eat_adapter = new Eat_Adapter(list1,Eat_Activity.this);
        lunch_eat_adapter = new Eat_Adapter(list2,Eat_Activity.this);
        dinner_eat_adapter = new Eat_Adapter(list3,Eat_Activity.this);
        add_eat_adapter = new Eat_Adapter(list4,Eat_Activity.this);
        breakfast_list.setAdapter(break_eat_adapter);
        lunch_list.setAdapter(lunch_eat_adapter);
        dinner_list.setAdapter(dinner_eat_adapter);
        add_list.setAdapter(add_eat_adapter);



        rd_breakfast.setOnClickListener(this);
        rd_lunch.setOnClickListener(this);
        rd_dinner.setOnClickListener(this);
        rd_add.setOnClickListener(this);
        eat_back.setOnClickListener(this);

        tv_aim_ka.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Eat_Activity.this,Eat_Aim_Activity.class));
                    }
                }
        );

        breakfast_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        new AlertDialog.Builder(Eat_Activity.this).setTitle("删除")
                                                .setIcon(R.drawable.food_icon)
                                                .setMessage("确定删除吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        U_Food uFood=new U_Food();
                                                        uFood.setU_id(list1.get(position).getU_id());
                                                        uFood.setF_name(list1.get(position).getF_name());
                                                        uFood.setF_time(list1.get(position).getF_time());
                                                        uFood.setF_date(list1.get(position).getF_date());
                                                        Json_Tools json_tools=new Json_Tools();
                                                        try {
                                                            String jsonString = json_tools.U_Food_ToJson(uFood);
                                                            OkHttpClient okHttpClient = new OkHttpClient();
                                                            RequestBody requestBody = new FormBody.Builder()
                                                                    .add("u_food", jsonString).build();
                                                            //创建一个Request
                                                            final Request request = new Request.Builder()
                                                                    .url(Constant.CONNECTURL + "UFood_Del_Servlet")
                                                                    .post(requestBody)//传递请求体
                                                                    .build();
                                                            Response response = okHttpClient.newCall(request).execute();

                                                            if (response.isSuccessful()) {

                                                                //打印服务端返回结果
                                                                Toast.makeText(Eat_Activity.this, "删除成功！", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(Eat_Activity.this, Eat_Activity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                        Looper.loop();
                                    }
                                }
                        ).start();
                    }
                }
        );

        lunch_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        new AlertDialog.Builder(Eat_Activity.this).setTitle("删除")
                                                .setIcon(R.drawable.food_icon)
                                                .setMessage("确定删除吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        U_Food uFood=new U_Food();
                                                        uFood.setU_id(list2.get(position).getU_id());
                                                        uFood.setF_name(list2.get(position).getF_name());
                                                        uFood.setF_time(list2.get(position).getF_time());
                                                        uFood.setF_date(list2.get(position).getF_date());
                                                        Json_Tools json_tools=new Json_Tools();
                                                        try {
                                                            String jsonString = json_tools.U_Food_ToJson(uFood);
                                                            OkHttpClient okHttpClient = new OkHttpClient();
                                                            RequestBody requestBody = new FormBody.Builder()
                                                                    .add("u_food", jsonString).build();
                                                            //创建一个Request
                                                            final Request request = new Request.Builder()
                                                                    .url(Constant.CONNECTURL + "UFood_Del_Servlet")
                                                                    .post(requestBody)//传递请求体
                                                                    .build();
                                                            Response response = okHttpClient.newCall(request).execute();

                                                            if (response.isSuccessful()) {
                                                                //打印服务端返回结果
                                                                Toast.makeText(Eat_Activity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(Eat_Activity.this, Eat_Activity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                        Looper.loop();
                                    }

                                }
                        ).start();
                    }
                }
        );

        dinner_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        new AlertDialog.Builder(Eat_Activity.this).setTitle("删除")
                                                .setIcon(R.drawable.food_icon)
                                                .setMessage("确定删除吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        U_Food uFood=new U_Food();
                                                        uFood.setU_id(list3.get(position).getU_id());
                                                        uFood.setF_name(list3.get(position).getF_name());
                                                        uFood.setF_time(list3.get(position).getF_time());
                                                        uFood.setF_date(list3.get(position).getF_date());
                                                        Json_Tools json_tools=new Json_Tools();
                                                        try {
                                                            String jsonString = json_tools.U_Food_ToJson(uFood);
                                                            OkHttpClient okHttpClient = new OkHttpClient();
                                                            RequestBody requestBody = new FormBody.Builder()
                                                                    .add("u_food", jsonString).build();
                                                            //创建一个Request
                                                            final Request request = new Request.Builder()
                                                                    .url(Constant.CONNECTURL + "UFood_Del_Servlet")
                                                                    .post(requestBody)//传递请求体
                                                                    .build();
                                                            Response response = okHttpClient.newCall(request).execute();

                                                            if (response.isSuccessful()) {
                                                                //打印服务端返回结果
                                                                Toast.makeText(Eat_Activity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(Eat_Activity.this, Eat_Activity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                        Looper.loop();
                                    }

                                }
                        ).start();
                    }
                }
        );

        add_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        new AlertDialog.Builder(Eat_Activity.this).setTitle("删除")
                                                .setIcon(R.drawable.food_icon)
                                                .setMessage("确定删除吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        U_Food uFood=new U_Food();
                                                        uFood.setU_id(list4.get(position).getU_id());
                                                        uFood.setF_name(list4.get(position).getF_name());
                                                        uFood.setF_time(list4.get(position).getF_time());
                                                        uFood.setF_date(list4.get(position).getF_date());
                                                        Json_Tools json_tools=new Json_Tools();
                                                        try {
                                                            String jsonString = json_tools.U_Food_ToJson(uFood);
                                                            OkHttpClient okHttpClient = new OkHttpClient();
                                                            RequestBody requestBody = new FormBody.Builder()
                                                                    .add("u_food", jsonString).build();
                                                            //创建一个Request
                                                            final Request request = new Request.Builder()
                                                                    .url(Constant.CONNECTURL + "UFood_Del_Servlet")
                                                                    .post(requestBody)//传递请求体
                                                                    .build();
                                                            Response response = okHttpClient.newCall(request).execute();

                                                            if (response.isSuccessful()) {
                                                                //打印服务端返回结果
                                                                Toast.makeText(Eat_Activity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(Eat_Activity.this, Eat_Activity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                        Looper.loop();
                                    }

                                }
                        ).start();
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rd_breakfast:
                Intent intent=new Intent();
                intent.putExtra("f_time","早餐");
                intent.putExtra("cur_ka",total_ka.getText().toString());
                intent.setClass(Eat_Activity.this,Eat_Add_Activity.class);
                startActivity(intent);
                break;
            case R.id.rd_lunch:
                Intent intent1=new Intent();
                intent1.putExtra("f_time","午餐");
                intent1.putExtra("cur_ka",total_ka.getText().toString());
                intent1.setClass(Eat_Activity.this,Eat_Add_Activity.class);
                startActivity(intent1);
                break;
            case R.id.rd_dinner:
                Intent intent2=new Intent();
                intent2.putExtra("f_time","晚餐");
                intent2.putExtra("cur_ka",total_ka.getText().toString());
                intent2.setClass(Eat_Activity.this,Eat_Add_Activity.class);
                startActivity(intent2);
                break;
            case R.id.rd_add:
                Intent intent3=new Intent();
                intent3.putExtra("f_time","加餐");
                intent3.putExtra("cur_ka",total_ka.getText().toString());
                intent3.setClass(Eat_Activity.this,Eat_Add_Activity.class);
                startActivity(intent3);
                break;
            case R.id.eat_back:finish();break;
        }
    }

    public void LoadFoodData(){
        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"UFood_Servlet")
                .get()
                .build();
        //封装成可执行的call对象
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                final Json_Tools json_tools=new Json_Tools();
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                UFoodList=json_tools.Json_ToUFood("u_food",json);

                                for (int i=0;i<UFoodList.size();i++){
                                    if( UFoodList.get(i).getU_id()==Constant.UID&&UFoodList.get(i).getF_time().equals("早餐")&&UFoodList.get(i).getF_date().equals(TimeUtil.getCurrentDate())){
                                        U_Food food=new U_Food();
                                        food.setU_id(UFoodList.get(i).getU_id());
                                        food.setF_name(UFoodList.get(i).getF_name());
                                        food.setF_ke(UFoodList.get(i).getF_ke());
                                        food.setF_ka(UFoodList.get(i).getF_ka());
                                        food.setF_time(UFoodList.get(i).getF_time());
                                        food.setF_date(UFoodList.get(i).getF_date());
                                        list1.add(food);

                                    }
                                    else if (UFoodList.get(i).getU_id()==Constant.UID&&UFoodList.get(i).getF_time().equals("午餐")&&UFoodList.get(i).getF_date().equals(TimeUtil.getCurrentDate())){
                                        U_Food food=new U_Food();
                                        food.setU_id(UFoodList.get(i).getU_id());
                                        food.setF_name(UFoodList.get(i).getF_name());
                                        food.setF_ke(UFoodList.get(i).getF_ke());
                                        food.setF_ka(UFoodList.get(i).getF_ka());
                                        food.setF_time(UFoodList.get(i).getF_time());
                                        food.setF_date(UFoodList.get(i).getF_date());
                                        list2.add(food);
                                    }
                                    else if(UFoodList.get(i).getU_id()==Constant.UID&&UFoodList.get(i).getF_time().equals("晚餐")&&UFoodList.get(i).getF_date().equals(TimeUtil.getCurrentDate())){
                                        U_Food food=new U_Food();
                                        food.setU_id(UFoodList.get(i).getU_id());
                                        food.setF_name(UFoodList.get(i).getF_name());
                                        food.setF_ke(UFoodList.get(i).getF_ke());
                                        food.setF_ka(UFoodList.get(i).getF_ka());
                                        food.setF_time(UFoodList.get(i).getF_time());
                                        food.setF_date(UFoodList.get(i).getF_date());
                                        list3.add(food);
                                    }
                                    else if(UFoodList.get(i).getU_id()==Constant.UID&&UFoodList.get(i).getF_time().equals("加餐")&&UFoodList.get(i).getF_date().equals(TimeUtil.getCurrentDate())){
                                        U_Food food=new U_Food();
                                        food.setU_id(UFoodList.get(i).getU_id());
                                        food.setF_name(UFoodList.get(i).getF_name());
                                        food.setF_ke(UFoodList.get(i).getF_ke());
                                        food.setF_ka(UFoodList.get(i).getF_ka());
                                        food.setF_time(UFoodList.get(i).getF_time());
                                        food.setF_date(UFoodList.get(i).getF_date());
                                        list4.add(food);
                                    }
                                }

                                DecimalFormat df=new DecimalFormat("#.#");
                                break_eat_adapter = new Eat_Adapter(list1,Eat_Activity.this);
                                lunch_eat_adapter = new Eat_Adapter(list2,Eat_Activity.this);
                                dinner_eat_adapter = new Eat_Adapter(list3,Eat_Activity.this);
                                add_eat_adapter = new Eat_Adapter(list4,Eat_Activity.this);
                                breakfast_list.setAdapter(break_eat_adapter);
                                lunch_list.setAdapter(lunch_eat_adapter);
                                dinner_list.setAdapter(dinner_eat_adapter);
                                add_list.setAdapter(add_eat_adapter);
                                for (int i=0;i<list1.size();i++){
                                    breakfast_double+=Double.valueOf(list1.get(i).getF_ka());
                                }
                                for (int i=0;i<list2.size();i++){
                                    lunch_double+=Double.valueOf(list2.get(i).getF_ka());
                                }
                                for (int i=0;i<list3.size();i++){
                                    dinner_double+=Double.valueOf(list3.get(i).getF_ka());
                                }
                                for (int i=0;i<list4.size();i++){
                                    add_double+=Double.valueOf(list4.get(i).getF_ka());
                                }
                                breakfast_ka.setText(String.valueOf(df.format(breakfast_double)));
                                lunch_ka.setText(String.valueOf(df.format(lunch_double)));
                                dinner_ka.setText(String.valueOf(df.format(dinner_double)));
                                add_ka.setText(String.valueOf(df.format(add_double)));
                                total_ka.setText(String.valueOf(df.format(breakfast_double+lunch_double+dinner_double+add_double)));
                                float temp_aim_ka=Float.valueOf(tv_aim_ka.getText().toString());
                                temp_cur_ka=Float.valueOf(total_ka.getText().toString())-Float.valueOf(use_ka.getText().toString());
                                if(temp_cur_ka<temp_aim_ka){
                                    progressView.setMaxProgress(temp_aim_ka);
                                    progressView.setCurrentProgress(temp_cur_ka);
                                }
                                else {
                                    progressView.setMaxProgress(temp_cur_ka);
                                    progressView.setCurrentProgress(temp_cur_ka);
                                    progressView.setColor2();
                                }

                                if(temp_cur_ka<0){
                                    progressView.setMaxProgress(temp_aim_ka);
                                    progressView.setCurrentProgress(0);
                                }

                                SavaData();

                            }
                        }
                );


            }
        });
    }

    private void SavaData(){

        User_Data user_data=new User_Data();
        User_Data user_data1;
        if(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate())!=null){
            user_data1=u_db.getCurUserDateByDate(TimeUtil.getCurrentDate());
            user_data.setUser_ka(total_ka.getText().toString());
            user_data.setUser_date(TimeUtil.getCurrentDate());
            user_data.setUser_drink(user_data1.getUser_drink());
            user_data.setUser_coffee(user_data1.getUser_coffee());
            u_db.addNewUserData(user_data);
        }
      else{
            user_data.setUser_ka("0.00");
            user_data.setUser_drink("0");
            user_data.setUser_coffee("0");
            user_data.setUser_date(TimeUtil.getCurrentDate());
            u_db.addNewUserData(user_data);
        }
    }



    public class Eat_Adapter extends BaseAdapter {
        private Context mContext;
        private List<U_Food> mList;

        public Eat_Adapter(List<U_Food> mList,Context mContext) {
            this.mList=mList;
            this.mContext = mContext;

        }

        @Override
        public int getCount() {
            return mList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder=null;

            if(view==null) {
                //创建缓冲布局界面，获取界面上的组件
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.eat_layout_adapter, viewGroup, false);
                //  Log.v("AnimalAdapter","改进后调用一次getView方法");
                holder=new ViewHolder();
                holder.f_name=(TextView)view.findViewById(R.id.f_name);
                holder.f_ke=(TextView)view.findViewById(R.id.f_ke);
                holder.f_ka=(TextView) view.findViewById(R.id.tv_f_ka);
                view.setTag(holder);
            }
            else {
                //用原有组件
                holder=(ViewHolder)view.getTag();
            }
            holder.f_name.setText(mList.get(i).getF_name());
            holder.f_ke.setText(mList.get(i).getF_ke());
            holder.f_ka.setText(mList.get(i).getF_ka());

            return view;
        }

        //提取出来方便点
        public  class ViewHolder {
            public TextView f_name;
            public TextView f_ke;
            public TextView f_ka;
        }

    }

}

package com.example.administrator.steps_count.Main_Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.Frg_Plan1;
import com.example.administrator.steps_count.fragment.MainFragment;
import com.example.administrator.steps_count.mall.User;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Plan_Activity extends AppCompatActivity {
    private Button btn_add_plan;
    public static User user=new User();
    private List<Plan> PlanList=new ArrayList<Plan>();
    private  List<Plan_Btn> list;
    private Plan_Adapter adapter;
    private ListView list_plan;
    private ImageView back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_layout);

        btn_add_plan=(Button)findViewById(R.id.btn_add_plan);
        list_plan=(ListView)findViewById(R.id.list_plan);
        back1=(ImageView)findViewById(R.id.back1);

        list=new ArrayList<>();
        LoadData();

        adapter = new Plan_Adapter(list,Plan_Activity.this);
        list_plan.setAdapter(adapter);

        back1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent();
                        intent.setClass(Plan_Activity.this, Frag_MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        btn_add_plan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Plan_Activity.this, Plan_Add_Activity.class));
                        finish();
                    }
                }
        );


    }

    public class Plan_Adapter extends BaseAdapter {
        private Context mContext;
        private List<Plan_Btn> mList;
        public Plan_Adapter(List<Plan_Btn> mList,Context mContext) {
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
            Plan_Adapter.ViewHolder holder=null;

            if(view==null) {
                //创建缓冲布局界面，获取界面上的组件
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.plan_layout_adpter, viewGroup, false);
                //  Log.v("AnimalAdapter","改进后调用一次getView方法");
                holder=new Plan_Adapter.ViewHolder();
                holder.tv_plan=(TextView)view.findViewById(R.id.tv_plan);
                holder.plan_finish=(CheckBox) view.findViewById(R.id.plan_finish);
                view.setTag(holder);
            }
            else {
                //用原有组件
                holder=(Plan_Adapter.ViewHolder)view.getTag();
            }
            holder.tv_plan.setText(mList.get(i).getName());
            holder.plan_finish.setTag(i);

            holder.plan_finish.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteCell(v, i);
                    final Plan plan=new Plan();
                    plan.setP_select(0);
                    plan.setP_name(mList.get(i).getName());

                    Json_Tools json_tools=new Json_Tools();
                    if(json_tools.isNetworkAvailable(mContext)){
                        new Thread(new Runnable(){
                            @Override
                            public void run() {

                                Json_Tools jst=new Json_Tools();
                                try {
                                    String str_json=jst.Plan_ToJson2(plan);
                                    OkHttpClient okHttpClient=new OkHttpClient();

                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("Plan",str_json).build();
                                    //创建一个Request
                                    final Request request = new Request.Builder()
                                            .url(Constant.CONNECTURL+"Plan_Change_Servlet2")
                                            .post(requestBody)//传递请求体
                                            .build();
                                    Response response = okHttpClient.newCall(request).execute();

                                    //判断请求是否成功
                                    if(response.isSuccessful()){
                                        Looper.prepare();
                                        //打印服务端返回结果
                                        if(mList.size()!=0){
                                            Toast.makeText(mContext, mList.get(i).getName()+"任务完成！", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(mContext, "今日所有任务完成！", Toast.LENGTH_SHORT).show();
                                        }

                                        Looper.loop();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else {

                        Toast.makeText(mContext, "上传失败，请检查网络！", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            return view;
        }

        private void deleteCell(final View v, final int index) {
            Animation.AnimationListener al = new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    mList.remove(index);
                    adapter.notifyDataSetChanged();
                    list_plan.setAdapter(adapter);
                }
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override public void onAnimationStart(Animation animation) {}
            };

            collapse(v, al);
        }

        private void collapse(final View v, Animation.AnimationListener al) {
            final int initialHeight = v.getMeasuredHeight();

            Animation anim = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                    }
                    else {
                        v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            if (al!=null) {
                anim.setAnimationListener(al);
            }
            anim.setDuration(200);
            v.startAnimation(anim);
        }


        //提取出来方便点
        public final class ViewHolder {
            public CheckBox plan_finish;
            public TextView tv_plan;

        }

    }

    public void LoadData(){

        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Plan_Servlet")
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
                                PlanList=json_tools.Json_ToPlan("plan",json);
                                for (int i=0;i<PlanList.size();i++){
                                    if( PlanList.get(i).getP_select()==1){
                                        Plan_Btn plan_btn=new Plan_Btn();
                                        plan_btn.setName(PlanList.get(i).getP_name());
                                        plan_btn.setBtn(PlanList.get(i).getP_select());
                                        list.add(plan_btn);

                                    }
                                }
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter = new Plan_Adapter(list,Plan_Activity.this);
                                                list_plan.setAdapter(adapter);
                                            }
                                        }
                                );
                            }
                        }
                );


            }
        });


    }

}

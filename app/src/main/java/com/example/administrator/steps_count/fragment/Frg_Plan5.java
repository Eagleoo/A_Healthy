package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Main_Activity.Plan;
import com.example.administrator.steps_count.Main_Activity.Plan_Btn;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.User;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Frg_Plan5 extends Fragment {
    public static User user=new User();
    private List<Plan> PlanList=new ArrayList<Plan>();
    private List< Plan_Btn> list;
    private ListView other_listView;
    private Plan_Adapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
        LoadData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_5,container, false);
        other_listView=(ListView)view.findViewById(R.id.other_listView);
        adapter = new Plan_Adapter(list,getActivity());
       other_listView.setAdapter(adapter);

        return view;
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
                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                PlanList=json_tools.Json_ToPlan("plan",json);

                                for (int i=0;i<PlanList.size();i++){
                                    if( PlanList.get(i).getP_type().equals("其他")){
                                        Plan_Btn plan_btn=new Plan_Btn();
                                        plan_btn.setName(PlanList.get(i).getP_name());
                                        plan_btn.setBtn(PlanList.get(i).getP_select());
                                        list.add(plan_btn);
                                    }
                                }
                                getActivity().runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter = new Plan_Adapter(list,getActivity());
                                                other_listView.setAdapter(adapter);
                                            }
                                        }
                                );

                            }
                        }
                );
            }
        });



    }

    public class Plan_Adapter extends BaseAdapter {
        private Context mContext;
        private List< Plan_Btn> mList;
        public Plan_Adapter(List< Plan_Btn> mList,Context mContext) {
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
                        R.layout.plan_add_adapter, viewGroup, false);
                //  Log.v("AnimalAdapter","改进后调用一次getView方法");
                holder=new Plan_Adapter.ViewHolder();
                holder.tv_plan=(TextView)view.findViewById(R.id.tv_plan);
                holder.addBtn=(Button) view.findViewById(R.id.addBtn);
                view.setTag(holder);
            }
            else {
                //用原有组件
                holder=(Plan_Adapter.ViewHolder)view.getTag();
            }
            holder.tv_plan.setText(mList.get(i).getName());
            holder.addBtn.setTag(i);

            if(mList.get(i).getBtn()==1){
                holder.addBtn.setText("已添加");
            }
            else {
                holder.addBtn.setText("添加");
            }

            //给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
            holder.addBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Button btn=(Button)v.findViewById(R.id.addBtn);
                    btn.setText("已添加");
                    final Plan plan=new Plan();
                    plan.setP_select(1);
                    plan.setP_name(mList.get(i).getName());
                    plan.setP_type("其他");

                    Json_Tools json_tools=new Json_Tools();
                    if(json_tools.isNetworkAvailable(mContext)){
                        new Thread(new Runnable(){
                            @Override
                            public void run() {

                                Json_Tools jst=new Json_Tools();
                                try {
                                    String str_json=jst.Plan_ToJson(plan);
                                    OkHttpClient okHttpClient=new OkHttpClient();

                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("Plan",str_json).build();
                                    //创建一个Request
                                    final Request request = new Request.Builder()
                                            .url(Constant.CONNECTURL+"Plan_Change_Servlet")
                                            .post(requestBody)//传递请求体
                                            .build();
                                    Response response = okHttpClient.newCall(request).execute();

                                    //判断请求是否成功
                                    if(response.isSuccessful()){
                                        Looper.prepare();
                                        //打印服务端返回结果
                                        Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
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
        //提取出来方便点
        public final class ViewHolder {
            public TextView tv_plan;
            public Button addBtn;
        }

    }
}

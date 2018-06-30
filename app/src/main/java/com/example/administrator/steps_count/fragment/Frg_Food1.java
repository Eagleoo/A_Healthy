package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Main_Activity.Food;
import com.example.administrator.steps_count.Main_Activity.Food_select_Activity;
import com.example.administrator.steps_count.Main_Activity.Plan;
import com.example.administrator.steps_count.Main_Activity.Plan_Btn;
import com.example.administrator.steps_count.R;
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

public class Frg_Food1 extends Fragment {
    private ListView food1_listView;
    private List<Food> FoodList=new ArrayList<Food>();
    private  List<Food> list;
    private Plan_Adapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_food1, container, false);
        food1_listView=(ListView)view.findViewById(R.id.food1_listView);
        list=new ArrayList<Food>();
        LoadData();
        adapter = new Plan_Adapter(list,getActivity());
        food1_listView.setAdapter(adapter);

        Intent frg_intent=getActivity().getIntent();
        final String str=frg_intent.getStringExtra("f_time");
        final String str1=frg_intent.getStringExtra("cur_ka");
        food1_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String f_ka=list.get(position).getF_ka();
                        String f_name=list.get(position).getF_name();
                        Intent intent=new Intent();
                        intent.putExtra("f_ka",f_ka);
                        intent.putExtra("f_name",f_name);
                        intent.putExtra("f_time",str);
                        intent.putExtra("cur_ka",str1);
                        intent.setClass(getActivity(), Food_select_Activity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
        );

        return view;
    }


    public void LoadData(){

        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Eat_Servlet")
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
                                FoodList=json_tools.Json_ToFood("food",json);

                                for (int i=0;i<FoodList.size();i++){
                                    if( FoodList.get(i).getF_type().equals("常见")){
                                        Food food=new Food();
                                        food.setF_name(FoodList.get(i).getF_name());
                                        food.setF_ka(FoodList.get(i).getF_ka());
                                        list.add(food);

                                    }
                                }

                                adapter = new Plan_Adapter(list,getActivity());
                                food1_listView.setAdapter(adapter);
                            }
                        }
                );


            }
        });

    }


    public static class Plan_Adapter extends BaseAdapter {
        private Context mContext;
        private List<Food> mList;

        public Plan_Adapter(List<Food> mList,Context mContext) {
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
                        R.layout.eat_add_adapter, viewGroup, false);
                //  Log.v("AnimalAdapter","改进后调用一次getView方法");
                holder=new ViewHolder();
                holder.f_name=(TextView)view.findViewById(R.id.f_name);
                holder.f_ka=(TextView) view.findViewById(R.id.f_ka);
                view.setTag(holder);
            }
            else {
                //用原有组件
                holder=(ViewHolder)view.getTag();
            }
            holder.f_name.setText(mList.get(i).getF_name());
            holder.f_ka.setText(mList.get(i).getF_ka());

            return view;
        }

        //提取出来方便点
        public static class ViewHolder {
            public TextView f_name;
            public TextView f_ka;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();


        }
    }


}

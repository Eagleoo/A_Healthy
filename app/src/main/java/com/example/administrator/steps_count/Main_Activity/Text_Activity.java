package com.example.administrator.steps_count.Main_Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Json_Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Text_Activity extends AppCompatActivity {
    private ListView text_list;
    private ImageView text_back;
    private int[] data={R.drawable.text7,R.drawable.text4,R.drawable.text2,R.drawable.text3,R.drawable.text6,R.drawable.text5,
            R.drawable.text1,R.drawable.text8};
    private Text_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout);

        adapter=new Text_Adapter(this,data);
        text_list=(ListView)findViewById(R.id.text_list);
        text_back=(ImageView)findViewById(R.id.text_back);
        text_list.setAdapter(adapter);

        text_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Json_Tools json_tools=new Json_Tools();
                        if(!json_tools.isNetworkAvailable(Text_Activity.this)){
                            Toast.makeText(Text_Activity.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent=new Intent();
                            if(adapter.getItemId(position)==0){
                                intent.putExtra("type","生活");
                                intent.setClass(Text_Activity.this, Text_Do_Activity.class);
                                startActivity(intent);
                            }
                            else if(adapter.getItemId(position)==1){
                                intent.putExtra("type","女性");
                                intent.setClass(Text_Activity.this, Text_Do_Activity.class);
                                startActivity(intent);
                            }
                            else if(adapter.getItemId(position)==2){
                                intent.putExtra("type","烟瘾");
                                intent.setClass(Text_Activity.this, Text_Do_Activity.class);
                                startActivity(intent);
                            }
                            else if(adapter.getItemId(position)==3){
                                intent.putExtra("type","弱点");
                                intent.setClass(Text_Activity.this, Text_Do_Activity.class);
                                startActivity(intent);
                            }
                            else if(adapter.getItemId(position)==4){
                                Toast.makeText(Text_Activity.this, "敬请期待···", Toast.LENGTH_SHORT).show();
                            }
                            else if(adapter.getItemId(position)==5){
                                intent.putExtra("type","短信");
                                intent.setClass(Text_Activity.this, Text_Do_Activity.class);
                                startActivity(intent);
                            }
                            else if(adapter.getItemId(position)==6){
                                intent.putExtra("type","手机");
                                intent.setClass(Text_Activity.this, Text_Do_Activity.class);
                                startActivity(intent);
                            }
                            else if(adapter.getItemId(position)==7){
                                Toast.makeText(Text_Activity.this, "敬请期待···", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                }
        );

        text_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Text_Activity.this, Frag_MainActivity.class));
                    }
                }
        );


    }
    public class Text_Adapter extends BaseAdapter {
        private int[] data;
        private Context mContext;
        public Text_Adapter(Context mContext,int[] data) {
            this.data=data;
            this.mContext = mContext;
    }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view==null)
            {
                holder=new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.text_layout_adpter, viewGroup, false);
                holder.img_text= (ImageView) view.findViewById(R.id.img_text);
                view.setTag(holder);
            }
            else
            {
                holder= (ViewHolder) view.getTag();
            }

            holder.img_text.setBackgroundResource(data[i]);



            return view;
        }
        public class ViewHolder
        {
            ImageView img_text;
        }
    }


}



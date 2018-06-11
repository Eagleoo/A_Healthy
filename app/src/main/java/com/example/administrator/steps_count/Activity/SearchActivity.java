package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Circle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
private BaseAdapter baseAdapter;
private List<Circle> list=new LinkedList<Circle>();
private Context context;
private ListView listView;
private TextView tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView= (ListView) findViewById(R.id.list_search);
        tip= (TextView) findViewById(R.id.tip);
        context=SearchActivity.this;
        final Intent intent=getIntent();
        list= (List<Circle>) intent.getSerializableExtra("list");
        Toast.makeText(SearchActivity.this,list+"",Toast.LENGTH_LONG).show();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent1=new Intent(SearchActivity.this,CotentActivity.class);
                    intent1.putExtra("title",list.get(i).getTitle());
                    intent1.putExtra("content",list.get(i).getContent());
                    intent1.putExtra("id",list.get(i).getId());
                    startActivity(intent1);
                }
            });


        baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder holder=null;
                if(view==null)
                {
                    holder=new ViewHolder();
                    view= LayoutInflater.from(context).inflate(R.layout.searchadapter,viewGroup,false);
                    holder.imag= (ImageView) view.findViewById(R.id.searchimg);
                    holder.txt= (TextView) view.findViewById(R.id.title);
                    view.setTag(holder);
                }else
                {
                    holder= (ViewHolder) view.getTag();
                }
                holder.txt.setText(list.get(i).getTitle());
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(list.get(i).getImag(), holder.imag);
                return view;
            }
        };
        listView.setAdapter(baseAdapter);
    }
    static class ViewHolder
    {
        ImageView imag;
        TextView txt;
    }
}

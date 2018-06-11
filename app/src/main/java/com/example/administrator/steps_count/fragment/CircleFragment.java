package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.administrator.steps_count.Activity.CotentActivity;
import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.NewConsult;
import com.example.administrator.steps_count.Activity.NewDynamic;
import com.example.administrator.steps_count.Activity.NewMall;
import com.example.administrator.steps_count.Activity.SearchActivity;
import com.example.administrator.steps_count.Activity.ShowDynamic;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.ConsultAdapter;
import com.example.administrator.steps_count.model.Circle;
import com.example.administrator.steps_count.model.Dynamics;
import com.example.administrator.steps_count.tools.Text;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CircleFragment extends Fragment {
    private List<View> list = new ArrayList<View>();
    private List<String> listtitle = new ArrayList<String>();
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private SliderLayout sliderLayout;
    private PagerIndicator indicator;
    private List<Circle> circlelist = new LinkedList<Circle>();
    private List<Circle> searchlist = new LinkedList<Circle>();
    private ListView circlelistview;
    private Context context;
    private ConsultAdapter consultAdapter;
    private Button search;
    private EditText edt_search;
    private BaseAdapter baseAdapter;
    private ListView dlistview;
    private List<Dynamics> dynamicsList=new LinkedList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Circle circle = new Circle();
                    circle.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    circle.setTitle(jsonObject.get("title").toString());
                    circle.setContent(jsonObject.get("content").toString());
                    circle.setImag(jsonObject.get("imag").toString());
                    circlelist.add(circle);


                }
                context = getActivity();
                consultAdapter = new ConsultAdapter((LinkedList<Circle>) circlelist, context);
                circlelistview.setAdapter(consultAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Handler mhandlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Dynamics dynamics=new Dynamics();
                    dynamics.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    dynamics.setTitle(jsonObject.get("title").toString());
                    dynamics.setContent(jsonObject.get("content").toString());
                   dynamics.setAuthor(jsonObject.get("author").toString());
                   dynamics.setDescribe(jsonObject.getString("describe").toString());
                   Toast.makeText(getActivity(),jsonArray.length()+"",Toast.LENGTH_LONG).show();
                    dynamicsList.add(dynamics);

                }
                context = getActivity();
                dlistview.setAdapter(baseAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Circle circle = new Circle();
                    circle.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    circle.setTitle(jsonObject.get("title").toString());
                    circle.setContent(jsonObject.get("content").toString());
                    circle.setImag(jsonObject.get("imag").toString());
                    searchlist.add(circle);

                }

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("list", (Serializable) searchlist);
                startActivity(intent);
                searchlist.removeAll(searchlist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.circle_layout, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip) view.findViewById(R.id.tabstrip);
        tabStrip.setTextSize(1, 20);
        tabStrip.setPadding(50, 50, 50, 50);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.rgb(244,164,96));
        tabStrip.setNonPrimaryAlpha(0.5f);
        tabStrip.setTextColor(Color.rgb(244,164,96));
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        indicator = (PagerIndicator) view.findViewById(R.id.indicator);
        search = (Button) view.findViewById(R.id.btn_search);
        edt_search = (EditText) view.findViewById(R.id.edt_source);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/Search?title=" + edt_search.getText().toString().trim();
                SearchReadURL(url);
            }
        });

        listtitle = new ArrayList<String>();
        listtitle.add("资讯推荐");
        listtitle.add("动态");
        View dynamic_pager = inflater.inflate(R.layout.dynamicadapter, null);
        View consult_pager = inflater.inflate(R.layout.consultlistview, null);
        circlelistview = (ListView) consult_pager.findViewById(R.id.cusultlist);
        dlistview= (ListView) dynamic_pager.findViewById(R.id.dynamiclist);
        circlelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CotentActivity.class);
                intent.putExtra("id", String.valueOf(circlelist.get(i).getId()));
                intent.putExtra("title", circlelist.get(i).getTitle());
                intent.putExtra("content", circlelist.get(i).getContent());
                startActivity(intent);

            }
        });
        dlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), ShowDynamic.class);
                intent.putExtra("dyname",dynamicsList.get(i).getTitle());
                intent.putExtra("dycontent",dynamicsList.get(i).getContent());
                intent.putExtra("dyid",String.valueOf(dynamicsList.get(i).getId()));

                startActivity(intent);
            }
        });
        list.add(consult_pager);
        list.add(dynamic_pager);
        initImageSlider();
        MyPagerAdapter adapter = new MyPagerAdapter(list);
        viewPager.setAdapter(adapter);
        String url = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/SelectMessage";
        ReadURL(url);
        String address = "http://" + Frag_MainActivity.localhost + ":8080/Health/servlet/Dynamic?function=show";
        DynamicReadURL(address);
        baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return  dynamicsList.size();
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
                ViewHolder viewHolder=null;
                if(view==null)
                {
                    view=LayoutInflater.from(context).inflate(R.layout.dynamic_layout,viewGroup,false);
                    viewHolder=new ViewHolder();
                    viewHolder.title= (TextView) view.findViewById(R.id.dynamicname);
                    viewHolder.describle= (TextView) view.findViewById(R.id.describle);
                    viewHolder.author= (TextView) view.findViewById(R.id.author);
                    view.setTag(viewHolder);
                }else {
                    viewHolder= (ViewHolder) view.getTag();
                }
                viewHolder.title.setText(dynamicsList.get(i).getTitle());
               viewHolder.describle.setText(dynamicsList.get(i).getDescribe());
               viewHolder.author.setText(dynamicsList.get(i).getAuthor());
                return view;
            }
        };
        return view;
    }
static  class ViewHolder
{
    TextView title;
    TextView describle;
    TextView author;
}

    public class MyPagerAdapter extends PagerAdapter {
        //定义容纳带显示页面的集合对象
        private List<View> list;

        public MyPagerAdapter(List<View> list) {
            this.list = list;
        }

        public MyPagerAdapter() {
        }

        //viewpager组件上显示页面的总数
        @Override
        public int getCount() {
            return list.size();
        }

        //初始化，创建指定位置的页面视图
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //将list中对应该显示的页面依次添加到viewgroup中
            container.addView(list.get(position));
            return list.get(position);
        }

        //判断哪一个带显示的页面显示在viewpage中
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //移除一个给定位置的页面，滑出去的页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        //viewpager的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return listtitle.get(position);
        }


    }

    private View initImageSlider() {


        //准备好要显示的数据
        List<String> imageUrls = new ArrayList<>();
        final List<String> descriptions = new ArrayList<>();
        imageUrls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=725404914,1689523057&fm=200&gp=0.jpg");
        imageUrls.add("https://f10.baidu.com/it/u=358462840,528327407&fm=72");
        imageUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523197262040&di=9892fe71cf1c3758bd722937c46b1a39&imgtype=0&src=http%3A%2F%2Fimgs.soufun.com%2Fnews%2F2015_03%2F13%2Fhouse%2F1426238427178_000.gif");
        descriptions.add("最新资讯");
        descriptions.add("商品推荐");
        descriptions.add("最新动态");
        for (int i = 0; i < imageUrls.size(); i++) {
            //新建三个展示View，并且添加到SliderLayout
            final TextSliderView tsv = new TextSliderView(getActivity());
            tsv.image(imageUrls.get(i)).description(descriptions.get(i));

            final int finalI = i;
            tsv.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                             @Override
                                             public void onSliderClick(BaseSliderView slider) {
                                                 if (tsv.getDescription() == "最新资讯") {
                                                     Intent intent = new Intent(getActivity(), NewConsult.class);
                                                     startActivity(intent);
                                                 } else if (tsv.getDescription() == "商品推荐") {
                                                     Intent intent = new Intent(getActivity(), NewMall.class);
                                                     startActivity(intent);
                                                 } else {
                                                     Intent intent = new Intent(getActivity(), NewDynamic.class);
                                                     startActivity(intent);
                                                 }
                                             }
                                         }
            );

            sliderLayout.addSlider(tsv);
        }
        //对SliderLayout进行一些自定义的配置
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setDuration(3000);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomIndicator(indicator);

        return sliderLayout;
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
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);
                super.onPostExecute(s);
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
    public void DynamicReadURL(final String url) {
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
                Message messages = new Message();
                messages.obj = s;
                mhandlers.sendMessage(messages);
                super.onPostExecute(s);
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

    public void SearchReadURL(final String url) {
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
                Message messages = new Message();
                messages.obj = s;
                handlers.sendMessage(messages);
                super.onPostExecute(s);
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



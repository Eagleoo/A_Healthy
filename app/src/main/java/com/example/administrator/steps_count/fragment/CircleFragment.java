package com.example.administrator.steps_count.fragment;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.example.administrator.steps_count.Activity.Publishdy;
import com.example.administrator.steps_count.Activity.SearchActivity;
import com.example.administrator.steps_count.Activity.ShowDynamic;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.ConsultAdapter;
import com.example.administrator.steps_count.adapter.Mall_adapter;
import com.example.administrator.steps_count.model.Circle;
import com.example.administrator.steps_count.model.Dynamics;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.Text;
import com.getbase.floatingactionbutton.FloatingActionButton;
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
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private Context context=getActivity();
    private ConsultAdapter consultAdapter;
    private LinearLayout change_news;
    private BaseAdapter baseAdapter;
    private ListView dlistview;
    private FloatingActionButton action_grid,action_change;
    private ScrollView scroll;
    private List<Dynamics> dynamicsList=new LinkedList<>();
    private List<Dynamics> change_dynamicsList;
    private boolean flag=false;
    private List<Dynamics> MallList=new ArrayList<Dynamics>();
    @SuppressLint("HandlerLeak")
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

                List<Circle> list = new LinkedList<Circle>();
                if(circlelist.size()!=0){
                    while (list.size()<8){
                        int number = new Random().nextInt(circlelist.size()) + 0;
                        if (!list.contains(circlelist.get(number))){
                            list.add(circlelist.get(number));
                        }
                    }
                }

                context = getActivity();
                if(context!=null){
                    consultAdapter = new ConsultAdapter((LinkedList<Circle>) list, context);
                    circlelistview.setAdapter(consultAdapter);
                }

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
        scroll=(ScrollView)view.findViewById(R.id.scroll);
        scroll.smoothScrollTo(0,0);
        tabStrip.setTextSize(1, 20);
        tabStrip.setPadding(50, 50, 50, 50);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.rgb(244,164,96));
        tabStrip.setNonPrimaryAlpha(0.5f);
        tabStrip.setTextColor(Color.parseColor("#2894FF"));
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        indicator = (PagerIndicator) view.findViewById(R.id.indicator);

        listtitle = new ArrayList<String>();
        listtitle.add("动态");
        listtitle.add("资讯推荐");
        View dynamic_pager = inflater.inflate(R.layout.dynamicadapter, null);
        View consult_pager = inflater.inflate(R.layout.consultlistview, null);
        change_news=(LinearLayout)consult_pager.findViewById(R.id.change_news);
        circlelistview = (ListView) consult_pager.findViewById(R.id.cusultlist);
        dlistview= (ListView) dynamic_pager.findViewById(R.id.dynamiclist);
        action_change=(FloatingActionButton)dynamic_pager.findViewById(R.id.action_change);
        action_grid=(FloatingActionButton)dynamic_pager.findViewById(R.id.action_grid);
        action_change.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change_dynamicsList= new LinkedList<Dynamics>();
                            while (change_dynamicsList.size()<3){
                                int number = new Random().nextInt(MallList.size()) + 0;
                                if (!change_dynamicsList.contains(MallList.get(number))){
                                    change_dynamicsList.add(MallList.get(number));
                                }
                            context = getActivity();
                            Dynamic_Adapter dynamic_adapter=new Dynamic_Adapter(change_dynamicsList,context);
                            dlistview.setAdapter(dynamic_adapter);
                            Toast.makeText(getActivity(), "刷新成功！！", Toast.LENGTH_SHORT).show();
                        }
//                        else {
//                            Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
        );
        action_grid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Frag_MainActivity.user!=null){
                            startActivity(new Intent(getActivity(),Publishdy.class));
                        }
                        else {
                            Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
        change_news.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<Circle> list = new LinkedList<Circle>();
                        if(circlelist.size()!=0){
                            while (list.size()<8){
                                int number = new Random().nextInt(circlelist.size()) + 0;
                                if (!list.contains(circlelist.get(number))){
                                    list.add(circlelist.get(number));
                                }
                            }
                            if(context!=null&&list.size()!=0){
                                context = getActivity();
                                consultAdapter = new ConsultAdapter((LinkedList<Circle>) list, context);
                                circlelistview.setAdapter(consultAdapter);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "刷新失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
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
                if(change_dynamicsList!=null){
                    Intent intent=new Intent(getActivity(), ShowDynamic.class);
                    intent.putExtra("id",change_dynamicsList.get(i).getId());
                    intent.putExtra("author",change_dynamicsList.get(i).getAuthor());
                    intent.putExtra("curtime",change_dynamicsList.get(i).getTime());
                    intent.putExtra("content",change_dynamicsList.get(i).getContent());
                    intent.putExtra("id",String.valueOf(change_dynamicsList.get(i).getId()));
                    intent.putExtra("img",change_dynamicsList.get(i).getImg());
                    intent.putExtra("img_content",change_dynamicsList.get(i).getImg_content());
                    intent.putExtra("like_num",String.valueOf(change_dynamicsList.get(i).getLike_num()));
                    intent.putExtra("review_num",String.valueOf(change_dynamicsList.get(i).getReview_num()));
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getActivity(), ShowDynamic.class);
                    intent.putExtra("id",MallList.get(i).getId());
                    intent.putExtra("author",MallList.get(i).getAuthor());
                    intent.putExtra("curtime",MallList.get(i).getTime());
                    intent.putExtra("content",MallList.get(i).getContent());
                    intent.putExtra("id",String.valueOf(MallList.get(i).getId()));
                    intent.putExtra("img",MallList.get(i).getImg());
                    intent.putExtra("img_content",MallList.get(i).getImg_content());
                    intent.putExtra("like_num",String.valueOf(MallList.get(i).getLike_num()));
                    intent.putExtra("review_num",String.valueOf(MallList.get(i).getReview_num()));
                    startActivity(intent);
                }
            }
        });

        list.add(dynamic_pager);
        list.add(consult_pager);

        initImageSlider();
        MyPagerAdapter adapter = new MyPagerAdapter(list);
        viewPager.setAdapter(adapter);
        String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/SelectMessage";
        ReadURL(url);
        Load_Dynamic();

        return view;
    }
static  class ViewHolder
{
    ImageView circle_img;
    TextView author;
    TextView curtime;
    TextView content;
    ImageView img_content;
    TextView like_num;
    TextView review_num;
    TextView dynamicLocation;
    //ImageView location_img;
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

    private void Load_Dynamic(){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "all").build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Show_Dynamic")
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
                    final String json = response.body().string();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MallList=getMall("mall",json);
                                Dynamic_Adapter dynamic_adapter=new Dynamic_Adapter(MallList,getContext());
                                dlistview.setAdapter(dynamic_adapter);
                            }
                        });
                    }

                }
            }
        });
    }

    private static List<Dynamics> getMall(String key, String jsonString) {
        List<Dynamics> list = new ArrayList<Dynamics>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                Dynamics dynamics=new Dynamics();
                dynamics.setId(Integer.parseInt(jsonObject.get("id").toString()));
                dynamics.setImg(jsonObject.get("img").toString());
                dynamics.setImg_content(jsonObject.get("img_content").toString());
                dynamics.setContent(jsonObject.get("content").toString());
                dynamics.setAuthor(jsonObject.get("author").toString());
                dynamics.setTime(jsonObject.get("time").toString());
                dynamics.setDynamicLocation(jsonObject.get("dynamicLocation").toString());
                dynamics.setLike_num(Integer.valueOf(jsonObject.get("like_num").toString()));
                dynamics.setReview_num(Integer.valueOf(jsonObject.get("review_num").toString()));
                list.add(dynamics);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public class Dynamic_Adapter extends BaseAdapter{
        public Context mContext;
        public List<Dynamics> mList;
        public Dynamic_Adapter(List<Dynamics> mList,Context mContext) {
            this.mList=mList;
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return  mList.size();
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
                view=LayoutInflater.from(mContext).inflate(R.layout.dynamic_layout,viewGroup,false);
                viewHolder=new ViewHolder();
                viewHolder.author= (TextView) view.findViewById(R.id.dynamicname);
                viewHolder.curtime= (TextView) view.findViewById(R.id.curtime);
                viewHolder.content= (TextView) view.findViewById(R.id.describle);
                viewHolder.dynamicLocation= (TextView) view.findViewById(R.id.dynamicLocation);
                viewHolder.like_num= (TextView) view.findViewById(R.id.like_num);
                viewHolder.review_num= (TextView) view.findViewById(R.id.review_num);
                viewHolder.circle_img=(ImageView)view.findViewById(R.id.circle_img) ;
                viewHolder.img_content=(ImageView)view.findViewById(R.id.img_content) ;
                // viewHolder.location_img=(ImageView)view.findViewById(R.id.location_img);
                view.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.author.setText(mList.get(i).getAuthor());
            viewHolder.curtime.setText(mList.get(i).getTime());
            viewHolder.content.setText(mList.get(i).getContent());
            viewHolder.like_num.setText(String.valueOf(mList.get(i).getLike_num()));
            viewHolder.review_num.setText(String.valueOf(mList.get(i).getReview_num()));
            viewHolder.dynamicLocation.setText(mList.get(i).getDynamicLocation());

            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
            ImageLoader.getInstance().init(configuration);
            ImageLoader.getInstance().displayImage(mList.get(i).getImg_content(), viewHolder.img_content);
            ImageLoader.getInstance().displayImage(mList.get(i).getImg(), viewHolder.circle_img);
            return view;
        }
    }
}



package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.OrderAdapter;
import com.example.administrator.steps_count.adapter.PaymentAdapter;
import com.example.administrator.steps_count.fragment.CircleFragment;
import com.example.administrator.steps_count.model.Order;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AllInformation extends AppCompatActivity {
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private Context context = AllInformation.this;

    private List<String> listtitle = new ArrayList<String>();
    private List<View> list = new ArrayList<View>();
    private List<Order> orderlist = new LinkedList<Order>();
    private OrderAdapter orderAdapter;

    private ListView OrderlistView;
    private List<Order> Receivelist = new LinkedList<Order>();

    private ListView ReceivelistView;
    private BaseAdapter baseAdapter;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog;

    private ListView SendlistView;
    private List<Order> Sendlist = new LinkedList<Order>();
    private BaseAdapter sendadapter;

    private List<Order> paylist = new LinkedList<Order>();
    private PaymentAdapter Payadapter;
    private ListView PaylistView;

    Handler Orderhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Order order = new Order();
                    order.setMall_img(jsonObject.get("mall_img").toString());
                    order.setMall_name(jsonObject.get("mall_name").toString());
                    order.setMall_describe(jsonObject.get("mall_describe").toString());
                    order.setMall_price(jsonObject.get("mall_price").toString());
                    order.setOrder_id(Integer.parseInt(jsonObject.get("order_id").toString()));
                    orderlist.add(order);

                }

                orderAdapter = new OrderAdapter((LinkedList<Order>) orderlist, context);
                OrderlistView.setAdapter(orderAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    Handler Receivehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Order order = new Order();
                    order.setMall_img(jsonObject.get("mall_img").toString());
                    order.setMall_name(jsonObject.get("mall_name").toString());
                    order.setMall_describe(jsonObject.get("mall_describe").toString());
                    order.setMall_price(jsonObject.get("mall_price").toString());
                    Receivelist.add(order);
                }


                ReceivelistView.setAdapter(baseAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Handler sendhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Order order = new Order();
                    order.setOrder_id(Integer.parseInt(jsonObject.get("order_id").toString()));
                    order.setMall_img(jsonObject.get("mall_img").toString());
                    order.setMall_name(jsonObject.get("mall_name").toString());
                    order.setMall_describe(jsonObject.get("mall_describe").toString());
                    order.setMall_price(jsonObject.get("mall_price").toString());
                    Sendlist.add(order);
                }


                SendlistView.setAdapter(sendadapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Handler payhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);
                    Order order = new Order();
                    order.setMall_img(jsonObject.get("mall_img").toString());
                    order.setMall_name(jsonObject.get("mall_name").toString());
                    order.setMall_describe(jsonObject.get("mall_describe").toString());
                    order.setMall_price(jsonObject.get("mall_price").toString());
                    paylist.add(order);
                }

                Payadapter = new PaymentAdapter((LinkedList<Order>) paylist, context);
                PaylistView.setAdapter(Payadapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_information);
        viewPager = (ViewPager) findViewById(R.id.view);
        tabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        listtitle = new ArrayList<String>();
        listtitle.add("全部订单");
        listtitle.add("待收货");
        listtitle.add("待发货");
        listtitle.add("待付款");
        LayoutInflater layoutInflater = getLayoutInflater();
        View checkreceive = layoutInflater.inflate(R.layout.receivedadapter, null);
        View checksend = layoutInflater.inflate(R.layout.sendlistview, null);
        View checkpay = layoutInflater.inflate(R.layout.paymentlist, null);
        View allorder = layoutInflater.inflate(R.layout.orderlistview, null);
        list.add(allorder);
        list.add(checkreceive);
        list.add(checksend);
        list.add(checkpay);
        MyPagerAdapter adapter = new MyPagerAdapter(list);
        viewPager.setAdapter(adapter);
        tabStrip.setTextSize(1, 20);
        tabStrip.setPadding(50, 50, 50, 50);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.rgb(244,164,96));
        Intent itemintent = getIntent();
        tabStrip.setNonPrimaryAlpha(0.5f);
        switch (itemintent.getStringExtra("item")) {
            case "0":
                viewPager.setCurrentItem(0);
                tabStrip.setTextColor(Color.rgb(244,164,96));

                break;
            case "1":
                viewPager.setCurrentItem(1);
                tabStrip.setTextColor(Color.rgb(244,164,96));
                break;
            case "2":
                viewPager.setCurrentItem(2);
                tabStrip.setTextColor(Color.rgb(244,164,96));
                break;
            case "3":
                viewPager.setCurrentItem(3);
                tabStrip.setTextColor(Color.rgb(244,164,96));
                break;
        }

        OrderlistView = (ListView) allorder.findViewById(R.id.orderlist);
        OrderlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, OrderInformation.class);
                intent.putExtra("order_id", String.valueOf(orderlist.get(i).getOrder_id()));
                intent.putExtra("img", orderlist.get(i).getMall_img());
                intent.putExtra("name", orderlist.get(i).getMall_name());
                intent.putExtra("describe", orderlist.get(i).getMall_describe());
                intent.putExtra("price", orderlist.get(i).getMall_price());

                startActivity(intent);
            }
        });
        String address = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/SelectOrder?function=order&username=" + Frag_MainActivity.user.getUsername();
        OrderReadURL(address);
        ReceivelistView = (ListView) checkreceive.findViewById(R.id.receivelist);
        String receive = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/IsRecieve?username=" + Frag_MainActivity.user.getUsername();
        ReceiveReadURL(receive);
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return Receivelist.size();
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
            public View getView(final int i, View view, ViewGroup viewGroup) {


                ViewHolder holder = null;
                if (view == null) {
                    holder = new ViewHolder();
                    view = LayoutInflater.from(context).inflate(R.layout.activity_check_receive, viewGroup, false);
                    holder.imageView = (ImageView) view.findViewById(R.id.imag);
                    holder.name = (TextView) view.findViewById(R.id.name);
                    holder.describe = (TextView) view.findViewById(R.id.describe);
                    holder.price = (TextView) view.findViewById(R.id.price);
                    holder.receive = (Button) view.findViewById(R.id.receive);
                    view.setTag(holder);
                } else {

                    holder = (ViewHolder) view.getTag();
                }
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(Receivelist.get(i).getMall_img(), holder.imageView);
                holder.name.setText(Receivelist.get(i).getMall_name());
                holder.describe.setText(Receivelist.get(i).getMall_describe());
                holder.price.setText(Receivelist.get(i).getMall_price());
                final ViewHolder finalHolder = holder;
                holder.receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(context);
                        alertDialog = builder.setIcon(R.drawable.pe)
                                .setMessage("是否确认收货?")
                                .setTitle("系统提示")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String address = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/EnsuerReceive?mall_name=" + finalHolder.name.getText();
                                        ReceiveReadURL(address);
                                        Intent intent = new Intent(context, AllInformation.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        alertDialog.show();
                    }
                });
                return view;
            }
        };
        SendlistView = (ListView) checksend.findViewById(R.id.sendlist);
        String sendaddress = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/CheckSend?username=" + Frag_MainActivity.user.getUsername();
        SendReadURL(sendaddress);
        sendadapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return Sendlist.size();
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
            public View getView(final int i, View view, ViewGroup viewGroup) {
                SendViewHolder holder = null;
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.activity_check_send, viewGroup, false);
                    holder = new SendViewHolder();
                    holder.img = (ImageView) view.findViewById(R.id.imag);
                    holder.name = (TextView) view.findViewById(R.id.name);
                    holder.describe = (TextView) view.findViewById(R.id.describe);
                    holder.price = (TextView) view.findViewById(R.id.price);
                    holder.back = (Button) view.findViewById(R.id.back);
                    view.setTag(holder);
                } else {
                    holder = (SendViewHolder) view.getTag();
                }
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(configuration);
                ImageLoader.getInstance().displayImage(Sendlist.get(i).getMall_img(), holder.img);
                holder.name.setText(Sendlist.get(i).getMall_name());
                holder.describe.setText(Sendlist.get(i).getMall_describe());
                holder.price.setText(Sendlist.get(i).getMall_price());
                holder.back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(context);
                        alertDialog = builder.setIcon(R.drawable.pe)
                                .setMessage("确认退款?")
                                .setTitle("系统提示")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String backaddress = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/SelectOrder?function=back&order_id=" +
                                                Sendlist.get(i).getOrder_id();
                                        BackReadURL(backaddress);
                                        Toast.makeText(context, "退款成功", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(context, AllInformation.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        alertDialog.show();

                    }
                });
                return view;
            }
        };
        PaylistView = (ListView) checkpay.findViewById(R.id.paylist);
        String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/IsPayment?username=" + Frag_MainActivity.user.getUsername();
        PayReadURL(url);

    }

    static class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView describe;
        TextView price;
        Button receive;
    }

    static class SendViewHolder {
        ImageView img;
        TextView name;
        TextView describe;
        TextView price;
        Button back;
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

    public void OrderReadURL(final String url) {
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
                Orderhandler.sendMessage(message);
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

    public void ReceiveReadURL(final String url) {
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
                Receivehandler.sendMessage(message);
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

    public void SendReadURL(final String url) {
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
                sendhandler.sendMessage(message);
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

    public void BackReadURL(final String url) {
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

    public void PayReadURL(final String url) {
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
                payhandler.sendMessage(message);
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

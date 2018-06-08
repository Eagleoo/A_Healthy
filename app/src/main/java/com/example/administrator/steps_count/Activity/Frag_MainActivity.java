package com.example.administrator.steps_count.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.steps_count.Main_Activity.Plan_Activity;
import com.example.administrator.steps_count.Main_Activity.Plan_Add_Activity;
import com.example.administrator.steps_count.Main_Activity.Text_Activity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.CircleFragment;
import com.example.administrator.steps_count.fragment.FootFragment;
import com.example.administrator.steps_count.fragment.MainFragment;
import com.example.administrator.steps_count.fragment.MallFragment;
import com.example.administrator.steps_count.fragment.MeFragment;

import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.StepService;
import com.example.administrator.steps_count.step.Step_MainActivity;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import com.example.administrator.steps_count.model.User;


public class Frag_MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FrameLayout frameLayout;
    private RadioButton tv_main,tv_mall,tv_foot,tv_circle,tv_me;

    public static User user;
    private MyBroadcost myBroadcost;

    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler());

    public static String localhost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_mainactivity);
<<<<<<< HEAD
        localhost="192.168.43.53";
=======



        setupService();

        localhost="192.168.1.104";

>>>>>>> bfbf647603fa895a85ac309cb1d972a9468dae5d
        myBroadcost=new MyBroadcost();


        IntentFilter intentFilter=new IntentFilter("android.intent.action.Broadcast");
        registerReceiver(myBroadcost,intentFilter);
        dynamicFragment(new MainFragment(),"mainFragment");
        initView();
        Intent intent = getIntent();
        user= (User) intent.getSerializableExtra("user");
        tv_main.setChecked(true);



    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);

    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            };

            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcost);
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }


    public void initView(){
        frameLayout=(FrameLayout)findViewById(R.id.framelayout);
        tv_main= (RadioButton) findViewById(R.id.tv_main);
        tv_mall= (RadioButton) findViewById(R.id.tv_mall);
        tv_foot= (RadioButton) findViewById(R.id.tv_foot);
        tv_circle= (RadioButton) findViewById(R.id.tv_circle);
        tv_me= (RadioButton) findViewById(R.id.tv_me);

        tv_main.setOnClickListener(this);
        tv_mall.setOnClickListener(this);
        tv_foot.setOnClickListener(this);
        tv_circle.setOnClickListener(this);
        tv_me.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_main:
                dynamicFragment(new MainFragment(),"mainFragment");break;
            case R.id.tv_mall:
                dynamicFragment(new MallFragment(),"mallFragment");break;
            case R.id.tv_foot:
                dynamicFragment(new FootFragment(),"footFragment");break;
            case R.id.tv_circle:
                dynamicFragment(new CircleFragment(),"circleFragment"); break;

            case R.id.tv_me:
                dynamicFragment(new MeFragment(),"meFragment");break;
            case R.id.add_plan:
                startActivity(new Intent(this,Plan_Add_Activity.class));break;
            case R.id.fin_plan:
                Intent intent=new Intent(Frag_MainActivity.this,Plan_Activity.class);
                startActivity(intent);break;

            case R.id.text_more:
                startActivity(new Intent(this,Text_Activity.class));break;
            case R.id.fin_text:
                startActivity(new Intent(this,Text_Activity.class));break;
        }
    }

    //动态添加Fragment
    public void dynamicFragment(android.support.v4.app.Fragment fragment,String tag){
        //1.获取碎片管理器
        FragmentManager supportFramentManager=getSupportFragmentManager();
        //2.开启一个事务
        FragmentTransaction beginTransaction=supportFramentManager.beginTransaction();
        //3.添加碎片
        beginTransaction.replace(R.id.framelayout,fragment,tag);
        //4.提交事务
        beginTransaction.commit();
    }
    class  MyBroadcost extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}

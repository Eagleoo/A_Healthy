package com.example.administrator.steps_count.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;

public class ActivityControl extends AppCompatActivity {
    private static LinkedList<Activity> activities=new LinkedList<Activity>();
    //增加管理活动类的方法，在另外的类中可调用，将该活动添加到管理列表activities中
    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }
    //关闭所有管理列表中的活动方法
    public static void finishAll()
    {
        //将管理列表中的活动一个一个进行关闭，关闭前进行状态检测，将关闭的活动关闭
        for(Activity activity:activities)
        {
            if(!activity.isFinishing())
            {
                activity.finish();
            }
        }
    }
}

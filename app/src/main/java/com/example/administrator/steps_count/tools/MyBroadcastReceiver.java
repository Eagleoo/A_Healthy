package com.example.administrator.steps_count.tools;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.administrator.steps_count.step.StepService;

/**
 * Created by Administrator on 2018/3/24/024.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            boolean isRunningService=false;
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo StepService :manager.getRunningServices(Integer.MAX_VALUE)){
               if("StepService".equals(StepService.service.getClassName())){
                    isRunningService=true;
                }
            }

            if (!isRunningService) {Log.d("66666","666666666666666666666");
                Intent i = new Intent(context, StepService.class);
                context.startService(i);
            }
        }
    }
}

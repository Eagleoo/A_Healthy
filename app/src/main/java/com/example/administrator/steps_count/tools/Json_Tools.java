package com.example.administrator.steps_count.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.StepEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Json_Tools {
    public  String Step_ToJson(List<StepEntity> stepEntityList) throws JSONException {
        if (stepEntityList== null) return "";
        JSONArray array = new JSONArray();
        JSONObject jsonObject ;
        StepEntity stepEntity ;
        for (int i = 0; i < stepEntityList.size(); i++) {
            stepEntity = stepEntityList.get(i);
            jsonObject = new JSONObject();
            jsonObject.put("date", stepEntity.getCurDate());
            jsonObject.put("step", stepEntity.getSteps());
            array.put(jsonObject);
        }
        return array.toString();
    }
    //检测上传数据是是否有网络
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

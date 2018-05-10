package com.example.administrator.steps_count.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.administrator.steps_count.Main_Activity.Plan;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.StepEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public  String Plan_ToJson(Plan plan) throws JSONException {
        if (plan== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_name",plan.getP_name());
        jsonObject.put("p_select",plan.getP_select());
        jsonObject.put("p_type",plan.getP_type());

        return jsonObject.toString();
    }

    public  String Plan_ToJson2(Plan plan) throws JSONException {
        if (plan== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_name",plan.getP_name());
        jsonObject.put("p_select",plan.getP_select());

        return jsonObject.toString();
    }

    public List<Plan> Json_ToPlan(String key, String jsonString) {
        List<Plan> list = new ArrayList<Plan>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);

            for (int i = 0; i < Persons.length(); i++) {
                Plan plan = new Plan();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                plan.setP_name(jsonObject2.getString("p_name"));
                plan.setP_type(jsonObject2.getString("p_type"));
                plan.setP_select(jsonObject2.getInt("p_select"));
                list.add(plan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
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

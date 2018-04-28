package com.example.administrator.steps_count.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView updatepwd;
    private TextView address;
    private TextView updatecost;
    private TextView about;
    private TextView exit;
    private AlertDialog.Builder builder=null;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        updatepwd= (TextView) findViewById(R.id.pwdupdate);
        address= (TextView) findViewById(R.id.address);
        updatecost= (TextView) findViewById(R.id.updatecost);
        about= (TextView) findViewById(R.id.about);
        exit= (TextView) findViewById(R.id.exit);
        updatepwd.setOnClickListener(this);
        address.setOnClickListener(this);
        updatecost.setOnClickListener(this);
        about.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pwdupdate:
                Intent updatepwd=new Intent(SettingActivity.this,Updatepwd.class);
                startActivity(updatepwd);
                break;
            case R.id.updatecost:
                Intent costpwd=new Intent(SettingActivity.this,CostpwdActivity.class);
                startActivity(costpwd);
                break;
            case R.id.address:
                Intent controlad=new Intent(SettingActivity.this,GoodsActivity.class);
                startActivity(controlad);
                break;
            case R.id.about:
                Intent about=new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(about);
                break;
            case R.id.exit:
                if (Frag_MainActivity.name==null)
                {
                    Toast.makeText(SettingActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                }else {
                    builder = new AlertDialog.Builder(SettingActivity.this);
                    alertDialog = builder.setIcon(R.drawable.pe)
                            .setMessage("确认退出?")
                            .setTitle("系统提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent exit = new Intent();
                                    exit.setAction("android.intent.action.Broadcast");
                                    sendBroadcast(exit);
                                    Intent logout = new Intent(SettingActivity.this, LoginActivity.class);
                                    startActivity(logout);
                                }
                            })
                            .setNegativeButton("取消", null)

                            .create();
                    alertDialog.show();
                }
                break;


        }
    }
}

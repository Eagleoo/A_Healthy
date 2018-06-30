package com.example.administrator.steps_count.Main_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.ActivityControl;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.step.User_DBOpenHelper;

public class Eat_Aim_Activity extends AppCompatActivity {
    private TextView html_ka;
    private Button btn_eat_aim;
    private EditText eat_aim_ka;
    private User_DBOpenHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_aim_layout);

        html_ka=(TextView)findViewById(R.id.html_ka);
        btn_eat_aim=(Button)findViewById(R.id.btn_eat_aim);
        eat_aim_ka=(EditText)findViewById(R.id.eat_aim_ka);

        String html="https://www.zhihu.com/answer/18245407";
        html_ka.setText(html);
        html_ka.setAutoLinkMask(Linkify.ALL);
        html_ka.setMovementMethod(LinkMovementMethod.getInstance());

        btn_eat_aim.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db=new User_DBOpenHelper(Eat_Aim_Activity.this);
                        if (eat_aim_ka.getText().toString().equals("")){
                            Toast.makeText(Eat_Aim_Activity.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(db.getAimKa()==null){
                                db.addNewUserKa(eat_aim_ka.getText().toString());
                            }
                            else {
                                db.updateKa(eat_aim_ka.getText().toString());
                            }
                            Toast.makeText(Eat_Aim_Activity.this, "设置成功！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Eat_Aim_Activity.this,Eat_Activity.class));
                            finish();
                        }

                    }
                }
        );
    }
}

package com.example.administrator.steps_count.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.LoginActivity;
import com.example.administrator.steps_count.Activity.PerMassageActivity;
import com.example.administrator.steps_count.Activity.SettingActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.UserModel;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView txt_login;
    private TextView per_message;
    private ImageView head;
    private TextView setting;
    private UserModel userModel;
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.person_layout,container,false);
        userModel=new UserModel();
        gson=new Gson();
        txt_login= (TextView) view.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
        per_message= (TextView) view.findViewById(R.id.per_message);
        per_message.setOnClickListener(this);
        head= (ImageView) view.findViewById(R.id.head);
        setting= (TextView) view.findViewById(R.id.setting);
        setting.setOnClickListener(this);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);
        if (Frag_MainActivity.data!=null)
        {
            ImageLoader.getInstance().displayImage(Frag_MainActivity.data,head);
        }
        if(Frag_MainActivity.name!=null)
        {
            txt_login.setText(Frag_MainActivity.name);
            txt_login.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txt_login:
                Intent login=new Intent(getActivity(),LoginActivity.class);
                startActivity(login);
                break;
            case R.id.per_message:
                Intent per_msg=new Intent(getActivity(),PerMassageActivity.class);
                startActivity(per_msg);
                break;
            case R.id.setting:
                Intent setting=new Intent(getActivity(), SettingActivity.class);
                startActivity(setting);
                break;
        }
    }
}

package com.example.administrator.steps_count.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/1.
 */

public class User implements Serializable{
        private int user_id;
        private String user_phone;
        private String user_password;
        private String sex;
        private double user_tall;
        private double user_weight;
        private int user_age;
        private String username;
        private String portrait;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getUser_id() {
            return user_id;
        }
        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_password() {
            return user_password;
        }
        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }


    public double getUser_tall() {
        return user_tall;
    }

    public void setUser_tall(double user_tall) {
        this.user_tall = user_tall;
    }

    public double getUser_weight() {
        return user_weight;
    }

    public void setUser_weight(double user_weight) {
        this.user_weight = user_weight;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }



    }



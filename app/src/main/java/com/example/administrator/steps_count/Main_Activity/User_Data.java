package com.example.administrator.steps_count.Main_Activity;

public class User_Data {
    private String user_ka;
    private String user_drink;
    private String user_coffee;
    private String user_sleep;
    private String user_date;

    public User_Data() {
    }

    public User_Data(String user_ka, String user_drink, String user_coffee, String user_sleep, String user_date) {
        this.user_ka = user_ka;
        this.user_drink = user_drink;
        this.user_coffee = user_coffee;
        this.user_sleep = user_sleep;
        this.user_date = user_date;
    }

    public String getUser_ka() {
        return user_ka;
    }

    public void setUser_ka(String user_ka) {
        this.user_ka = user_ka;
    }

    public String getUser_drink() {
        return user_drink;
    }

    public void setUser_drink(String user_drink) {
        this.user_drink = user_drink;
    }

    public String getUser_coffee() {
        return user_coffee;
    }

    public void setUser_coffee(String user_coffee) {
        this.user_coffee = user_coffee;
    }

    public String getUser_sleep() {
        return user_sleep;
    }

    public void setUser_sleep(String user_sleep) {
        this.user_sleep = user_sleep;
    }

    public String getUser_date() {
        return user_date;
    }

    public void setUser_date(String user_date) {
        this.user_date = user_date;
    }

    @Override
    public String toString() {
        return "Food[user_ka="+user_ka+",user_drink="+user_drink+",user_coffee="+user_coffee+",user_sleep="+user_sleep+",user_date="+user_date+"]";
    }
}

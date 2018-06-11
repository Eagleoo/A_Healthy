package com.example.administrator.steps_count.Main_Activity;

public class Food {
    private String f_name;
    private String f_ka;
    private String f_type;

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_ka() {
        return f_ka;
    }

    public void setF_ka(String f_ka) {
        this.f_ka = f_ka;
    }

    public String getF_type() {
        return f_type;
    }

    public void setF_type(String f_type) {
        this.f_type = f_type;
    }

    @Override
    public String toString() {
        return "Food[f_name="+f_name+",f_ka="+f_ka+",f_type="+f_type+"]";
    }
}

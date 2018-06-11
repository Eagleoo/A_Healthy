package com.example.administrator.steps_count.Main_Activity;

public class U_Food {
    private int U_id;
    private String f_name;
    private String f_ka;
    private String f_time;
    private String f_date;
    private String f_ke;

    public int getU_id() {
        return U_id;
    }

    public void setU_id(int u_id) {
        U_id = u_id;
    }

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

    public String getF_time() {
        return f_time;
    }

    public void setF_time(String f_time) {
        this.f_time = f_time;
    }

    public String getF_date() {
        return f_date;
    }

    public void setF_date(String f_date) {
        this.f_date = f_date;
    }

    public String getF_ke() {
        return f_ke;
    }

    public void setF_ke(String f_ke) {
        this.f_ke = f_ke;
    }

    @Override
    public String toString() {
        return "U_Food[U_id="+U_id+",f_name="+f_name+",f_ka="+f_ka+",f_date="+f_date+",f_time="+f_time+",f_ke="+f_ke+"]";
    }
}

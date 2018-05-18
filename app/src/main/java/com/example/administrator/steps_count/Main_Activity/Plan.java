package com.example.administrator.steps_count.Main_Activity;

public class Plan {
    private String p_name;
    private String p_type;
    private int p_select;

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public int getP_select() {
        return p_select;
    }

    public void setP_select(int p_select) {
        this.p_select = p_select;
    }

    @Override
    public String toString() {
        return "Plan[p_name="+p_name+",p_type="+p_type+",p_select="+p_select+"]";
    }
}

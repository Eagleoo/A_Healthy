package com.example.administrator.steps_count.Main_Activity;

public class U_Plan {
    private int U_id;
    private String p_name;
    private int p_select;

    public int getU_id() {
        return U_id;
    }

    public void setU_id(int u_id) {
        U_id = u_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public int getP_select() {
        return p_select;
    }

    public void setP_select(int p_select) {
        this.p_select = p_select;
    }

    @Override
    public String toString() {
        return "U_Plan[U_id="+U_id+",p_name="+p_name+",p_select="+p_select+"]";
    }
}

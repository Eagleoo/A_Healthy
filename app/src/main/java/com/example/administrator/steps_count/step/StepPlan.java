package com.example.administrator.steps_count.step;

public class StepPlan {
    private String p_steps;
    private String p_km;
    private String p_ka;
    private int u_id;



    public StepPlan(){

    }

    public StepPlan(String p_steps, String p_km, String p_ka,int u_id) {
        this.p_steps = p_steps;
        this.p_km = p_km;
        this.p_ka=p_ka;
        this.u_id=u_id;
    }

    public String getP_steps() {
        return p_steps;
    }

    public void setP_steps(String p_steps) {
        this.p_steps = p_steps;
    }

    public String getP_km() {
        return p_km;
    }

    public void setP_km(String p_km) {
        this.p_km = p_km;
    }

    public String getP_ka() {
        return p_ka;
    }

    public void setP_ka(String p_ka) {
        this.p_ka = p_ka;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "StepPlan[p_steps="+p_steps+",p_km="+p_km+",p_ka="+p_ka+",u_id="+u_id+"]";
    }
}

package com.example.administrator.steps_count.Main_Activity;

public class Weight {
    private String weight;
    private String w_date;
    private String w_text;
    private String w_aim;

    public Weight(){

    }

    public Weight(String weight, String w_date, String w_text, String w_aim) {
        this.weight = weight;
        this.w_date = w_date;
        this.w_text = w_text;
        this.w_aim = w_aim;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getW_date() {
        return w_date;
    }

    public void setW_date(String w_date) {
        this.w_date = w_date;
    }

    public String getW_text() {
        return w_text;
    }

    public void setW_text(String w_text) {
        this.w_text = w_text;
    }

    public String getW_aim() {
        return w_aim;
    }

    public void setW_aim(String w_aim) {
        this.w_aim = w_aim;
    }

    @Override
    public String toString() {
        return "StepPlan[weight="+weight+",w_date="+w_date+",w_text="+w_text+",w_aim="+w_aim+"]";
    }
}

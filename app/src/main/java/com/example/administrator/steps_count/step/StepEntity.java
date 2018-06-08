package com.example.administrator.steps_count.step;

/**
 * Created by fySpring
 * Date : 2017/3/24
 * To do :
 */

public class StepEntity {

    private String curDate; //当天的日期
    private String steps;//当天的步数
    private String totalStepsKm;
    private String totalStepsKa;

    public StepEntity() {
    }

    public StepEntity(String curDate, String steps,String totalStepsKm,String totalStepsKa) {
        this.curDate = curDate;
        this.steps = steps;
        this.totalStepsKm=totalStepsKm;
        this.totalStepsKa=totalStepsKa;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getTotalStepsKm() {
        return totalStepsKm;
    }

    public void setTotalStepsKm(String totalStepsKm) {
        this.totalStepsKm = totalStepsKm;
    }

    public String getTotalStepsKa() {
        return totalStepsKa;
    }

    public void setTotalStepsKa(String totalStepsKa) {
        this.totalStepsKa = totalStepsKa;
    }

    @Override
    public String toString() {
        return "StepEntity{" +
                "curDate='" + curDate + '\'' +
                ", steps=" + steps +
                '}';
    }
}

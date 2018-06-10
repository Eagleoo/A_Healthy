package com.example.administrator.steps_count.step;

public class Map {
    private String time;
    private String speed;
    private String km;
    private String ka;
    private String date;

    public Map(){

    }

    public Map(String time, String speed, String km, String ka, String date) {
        this.time = time;
        this.speed = speed;
        this.km = km;
        this.ka = ka;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getKa() {
        return ka;
    }

    public void setKa(String ka) {
        this.ka = ka;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

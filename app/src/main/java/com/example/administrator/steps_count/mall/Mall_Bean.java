package com.example.administrator.steps_count.mall;

/**
 * Created by PC on 2018/3/8.
 */

public class Mall_Bean {
    private int id;
    private int img;
    private String name;
    private String fine;

    public Mall_Bean(int id, int img, String name, String fine) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.fine = fine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mall_Bean() {
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }
}

package com.example.administrator.steps_count.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Circle implements Serializable{
private int id;
private String title;
private String content;
private String imag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }
}

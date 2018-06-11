package com.example.administrator.steps_count.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/21.
 */

public class Respones implements Serializable{
    private String name;
    private String content;
    private int id;
    private int reviewid;


    public Respones() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewid() {
        return reviewid;
    }

    public void setReviewid(int reviewid) {
        this.reviewid = reviewid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

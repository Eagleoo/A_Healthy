package com.example.administrator.steps_count.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/19.
 */

public class Dynamics implements Serializable{
    private int id;
    private String author;
    private String time;
    private String img;
    private String img_content;
    private String content;
    private int like_num;
    private int review_num;
    private String dynamicLocation;

    public String getDynamicLocation() {
        return dynamicLocation;
    }

    public void setDynamicLocation(String dynamicLocation) {
        this.dynamicLocation = dynamicLocation;
    }

    public String getImg_content() {
        return img_content;
    }

    public void setImg_content(String img_content) {
        this.img_content = img_content;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getReview_num() {
        return review_num;
    }

    public void setReview_num(int review_num) {
        this.review_num = review_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

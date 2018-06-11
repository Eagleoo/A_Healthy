package com.example.administrator.steps_count.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/19.
 */

public class Review implements Serializable {
    private int id;
    private String username;
    private String content;
    private int consult_id;
    private String imag;


    public Review() {
    }




    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getConsult_id() {
        return consult_id;
    }

    public void setConsult_id(int consult_id) {
        this.consult_id = consult_id;
    }
}

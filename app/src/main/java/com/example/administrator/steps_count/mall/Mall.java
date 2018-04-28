package com.example.administrator.steps_count.mall;

public class Mall {
    private String mall_id;
    private String mall_name;
    private String mall_describe;
    private String mall_price;
    private String mall_img;
    private String mall_detail_img;
    private String mall_type;

    public Mall() {
    }

    public Mall(String mall_id, String mall_name, String mall_describe, String mall_price, String mall_img, String mall_detail_img, String mall_type) {
        this.mall_id = mall_id;
        this.mall_name = mall_name;
        this.mall_describe = mall_describe;
        this.mall_price = mall_price;
        this.mall_img = mall_img;
        this.mall_detail_img = mall_detail_img;
        this.mall_type = mall_type;
    }

    public String getMall_id() {
        return mall_id;
    }

    public void setMall_id(String mall_id) {
        this.mall_id = mall_id;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getMall_describe() {
        return mall_describe;
    }

    public void setMall_describe(String mall_describe) {
        this.mall_describe = mall_describe;
    }

    public String getMall_price() {
        return mall_price;
    }

    public void setMall_price(String mall_price) {
        this.mall_price = mall_price;
    }

    public String getMall_img() {
        return mall_img;
    }

    public void setMall_img(String mall_img) {
        this.mall_img = mall_img;
    }

    public String getMall_detail_img() {
        return mall_detail_img;
    }

    public void setMall_detail_img(String mall_detail_img) {
        this.mall_detail_img = mall_detail_img;
    }

    public String getMall_type() {
        return mall_type;
    }

    public void setMall_type(String mall_type) {
        this.mall_type = mall_type;
    }

    @Override
    public String toString() {
        return "Mall [mall_id=" + mall_id+ ", mall_name=" + mall_name + ", mall_describe=" + mall_describe + ", mall_price=" + mall_price
                + ", mall_img=" + mall_img +", mall_detail_img=" + mall_detail_img + ", mall_type=" + mall_type+"]";
    }
}

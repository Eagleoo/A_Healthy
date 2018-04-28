package com.example.administrator.steps_count.mall;

public class Banner_img {
    private String banner_img1;
    private String banner_img2;
    private String banner_img3;
    private String banner_img4;
    private String banner_img5;

    public Banner_img() {
    }

    public Banner_img(String banner_img1, String banner_img2, String banner_img3, String banner_img4, String banner_img5) {
        this.banner_img1 = banner_img1;
        this.banner_img2 = banner_img2;
        this.banner_img3 = banner_img3;
        this.banner_img4 = banner_img4;
        this.banner_img5 = banner_img5;
    }

    public String getBanner_img1() {
        return banner_img1;
    }

    public void setBanner_img1(String banner_img1) {
        this.banner_img1 = banner_img1;
    }

    public String getBanner_img2() {
        return banner_img2;
    }

    public void setBanner_img2(String banner_img2) {
        this.banner_img2 = banner_img2;
    }

    public String getBanner_img3() {
        return banner_img3;
    }

    public void setBanner_img3(String banner_img3) {
        this.banner_img3 = banner_img3;
    }

    public String getBanner_img4() {
        return banner_img4;
    }

    public void setBanner_img4(String banner_img4) {
        this.banner_img4 = banner_img4;
    }

    public String getBanner_img5() {
        return banner_img5;
    }

    public void setBanner_img5(String banner_img5) {
        this.banner_img5 = banner_img5;
    }

    @Override
    public String toString() {
        return "Banner [banner_img1=" +banner_img1+ ", banner_img2=" + banner_img2
                + ", banner_img3=" + banner_img3 + ", banner_img4=" + banner_img4
                + " banner_img5=" + banner_img5+"]";
    }
}

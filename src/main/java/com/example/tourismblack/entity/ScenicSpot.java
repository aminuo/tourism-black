package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "scenic_spots")
public class ScenicSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String img;
    private String introduce;
    private String address;
    private String times;
    private Integer viewCount;
    private Integer collectCount;
    private Integer commentCount;
    private Integer hotValue;
    private Integer hotStatus;

    // getter和setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    public Integer getHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(Integer hotStatus) {
        this.hotStatus = hotStatus;
    }

    public void calculateHotValue() {
        int view = viewCount != null ? viewCount : 0;
        int collect = collectCount != null ? collectCount : 0;
        int comment = commentCount != null ? commentCount : 0;
        this.hotValue = (int) Math.round(view * 0.4 + collect * 0.3 + comment * 0.3);
    }

    public void calculateHotStatus() {
        int value = hotValue != null ? hotValue : 0;
        if (value <= 50) {
            this.hotStatus = 0;
        } else if (value <= 150) {
            this.hotStatus = 1;
        } else if (value <= 300) {
            this.hotStatus = 2;
        } else {
            this.hotStatus = 3;
        }
    }

    public void updateHotMetrics() {
        calculateHotValue();
        calculateHotStatus();
    }

    @Override
    public String toString() {
        return "ScenicSpot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", introduce='" + introduce + '\'' +
                ", address='" + address + '\'' +
                ", times='" + times + '\'' +
                ", viewCount=" + viewCount +
                ", collectCount=" + collectCount +
                ", commentCount=" + commentCount +
                ", hotValue=" + hotValue +
                ", hotStatus=" + hotStatus +
                '}';
    }
}
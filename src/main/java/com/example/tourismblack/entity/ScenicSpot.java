package com.example.tourismblack.entity;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "scenic_spot_tags", joinColumns = @JoinColumn(name = "scenic_spot_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
                ", tags=" + tags +
                '}';
    }
}
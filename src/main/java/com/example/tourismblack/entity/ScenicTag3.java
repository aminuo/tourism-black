package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "scenic_tag3")
public class ScenicTag3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "scenic_id")
    private Integer scenicId;
    @Column(name = "tag3_code")
    private String tag3Code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScenicId() {
        return scenicId;
    }

    public void setScenicId(Integer scenicId) {
        this.scenicId = scenicId;
    }

    public String getTag3Code() {
        return tag3Code;
    }

    public void setTag3Code(String tag3Code) {
        this.tag3Code = tag3Code;
    }

    @Override
    public String toString() {
        return "ScenicTag3{" +
                "id=" + id +
                ", scenicId=" + scenicId +
                ", tag3Code=" + tag3Code +
                '}';
    }
}
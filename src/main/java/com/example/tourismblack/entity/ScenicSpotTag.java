package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "scenic_spot_tags", uniqueConstraints = @UniqueConstraint(columnNames = { "scenic_spot_id", "tag_id" }))
public class ScenicSpotTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "scenic_spot_id")
    private Integer scenicSpotId;
    @Column(name = "tag_id")
    private Integer tagId;

    // getter和setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(Integer scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "ScenicSpotTag{" +
                "id=" + id +
                ", scenicSpotId=" + scenicSpotId +
                ", tagId=" + tagId +
                '}';
    }
}
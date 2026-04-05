package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "scenic_tag_property")
public class ScenicTagProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "scenic_id")
    private Integer scenicId;
    @Column(name = "property_code")
    private String propertyCode;

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

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    @Override
    public String toString() {
        return "ScenicTagProperty{" +
                "id=" + id +
                ", scenicId=" + scenicId +
                ", propertyCode=" + propertyCode +
                '}';
    }
}
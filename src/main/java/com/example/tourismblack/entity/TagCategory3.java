package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_category3")
public class TagCategory3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c2_id")
    private Integer c2Id;
    private String name;
    private Integer sort;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getC2Id() {
        return c2Id;
    }

    public void setC2Id(Integer c2Id) {
        this.c2Id = c2Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TagCategory3{" +
                "id=" + id +
                ", c2Id=" + c2Id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", code='" + code + '\'' +
                '}';
    }
}
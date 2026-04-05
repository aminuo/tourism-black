package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_category2")
public class TagCategory2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c1_id")
    private Integer c1Id;
    private String name;
    private Integer sort;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getC1Id() {
        return c1Id;
    }

    public void setC1Id(Integer c1Id) {
        this.c1Id = c1Id;
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
        return "TagCategory2{" +
                "id=" + id +
                ", c1Id=" + c1Id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", code='" + code + '\'' +
                '}';
    }
}
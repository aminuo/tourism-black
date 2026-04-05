package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_category1")
public class TagCategory1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer sort;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "TagCategory1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", code='" + code + '\'' +
                '}';
    }
}
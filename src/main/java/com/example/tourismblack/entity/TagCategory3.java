package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_category3")
public class TagCategory3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c2_code")
    private String c2Code;
    private String name;
    private Integer sort;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
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
                ", c2Code='" + c2Code + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", code='" + code + '\'' +
                '}';
    }
}
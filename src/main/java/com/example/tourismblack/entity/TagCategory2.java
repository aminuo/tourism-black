package com.example.tourismblack.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_category2")
public class TagCategory2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c1_code")
    private String c1Code;
    private String name;
    private Integer sort;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getC1Code() {
        return c1Code;
    }

    public void setC1Code(String c1Code) {
        this.c1Code = c1Code;
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
                ", c1Code='" + c1Code + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", code='" + code + '\'' +
                '}';
    }
}
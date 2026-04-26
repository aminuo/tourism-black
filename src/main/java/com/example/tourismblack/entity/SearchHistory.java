package com.example.tourismblack.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    private String keyword;
    @Column(name = "create_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", keyword='" + keyword + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
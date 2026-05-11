package com.example.tourismblack.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTagPreferenceId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "tag_id")
    private Integer tagId;

    public UserTagPreferenceId() {
    }

    public UserTagPreferenceId(Integer userId, Integer tagId) {
        this.userId = userId;
        this.tagId = tagId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTagPreferenceId that = (UserTagPreferenceId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tagId);
    }
}
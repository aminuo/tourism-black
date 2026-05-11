package com.example.tourismblack.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tag_preference")
public class UserTagPreference {

    @EmbeddedId
    private UserTagPreferenceId id;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "collect_count")
    private Integer collectCount;

    @Column(name = "preference_score")
    private Double preferenceScore;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public UserTagPreference() {
    }

    public UserTagPreference(Integer userId, Integer tagId) {
        this.id = new UserTagPreferenceId(userId, tagId);
        this.viewCount = 0;
        this.collectCount = 0;
        this.preferenceScore = 0.0;
        this.updateTime = LocalDateTime.now();
    }

    public void incrementViewCount() {
        if (viewCount == null) {
            viewCount = 0;
        }
        viewCount++;
        calculatePreferenceScore();
        updateTime = LocalDateTime.now();
    }

    public void incrementCollectCount() {
        if (collectCount == null) {
            collectCount = 0;
        }
        collectCount++;
        calculatePreferenceScore();
        updateTime = LocalDateTime.now();
    }

    public void decrementCollectCount() {
        if (collectCount != null && collectCount > 0) {
            collectCount--;
            calculatePreferenceScore();
            updateTime = LocalDateTime.now();
        }
    }

    private void calculatePreferenceScore() {
        int view = viewCount != null ? viewCount : 0;
        int collect = collectCount != null ? collectCount : 0;
        this.preferenceScore = view * 0.3 + collect * 0.7;
    }

    public UserTagPreferenceId getId() {
        return id;
    }

    public void setId(UserTagPreferenceId id) {
        this.id = id;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Double getPreferenceScore() {
        return preferenceScore;
    }

    public void setPreferenceScore(Double preferenceScore) {
        this.preferenceScore = preferenceScore;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
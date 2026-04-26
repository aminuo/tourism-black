package com.example.tourismblack.repository;

import com.example.tourismblack.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
    // 根据用户ID查询搜索历史，按创建时间倒序
    List<SearchHistory> findByUserIdOrderByCreateTimeDesc(Integer userId);
    
    // 根据用户ID和关键词查询是否存在相同的搜索记录
    SearchHistory findByUserIdAndKeyword(Integer userId, String keyword);
    
    // 根据用户ID删除所有搜索历史
    void deleteByUserId(Integer userId);
}
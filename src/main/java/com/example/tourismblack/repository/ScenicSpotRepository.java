package com.example.tourismblack.repository;

import java.util.List;

import com.example.tourismblack.entity.ScenicSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotRepository extends JpaRepository<ScenicSpot, Integer> {
    // JpaRepository已经提供了基本的CRUD操作，包括查询全部数据的方法
    
    // 按标题模糊查询
    List<ScenicSpot> findByTitleContaining(String title);
    
    // 按标签模糊查询
    List<ScenicSpot> findByTagContaining(String tag);
    
    // 按标题和标签同时模糊查询
    List<ScenicSpot> findByTitleContainingAndTagContaining(String title, String tag);
}

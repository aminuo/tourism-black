package com.example.tourismblack.repository;

import java.util.List;

import com.example.tourismblack.entity.ScenicSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotRepository extends JpaRepository<ScenicSpot, Integer> {
    // JpaRepository已经提供了基本的CRUD操作，包括查询全部数据的方法

    // 按标题模糊查询
    List<ScenicSpot> findByTitleContaining(String title);

    // 通过标签名称查询景点
    @Query("SELECT s FROM ScenicSpot s JOIN s.tags t WHERE t.name = :tagName")
    List<ScenicSpot> findByTagName(@Param("tagName") String tagName);

    // 通过标题和标签名称同时查询景点
    @Query("SELECT s FROM ScenicSpot s JOIN s.tags t WHERE s.title LIKE %:title% AND t.name = :tagName")
    List<ScenicSpot> findByTitleContainingAndTagName(@Param("title") String title, @Param("tagName") String tagName);
}

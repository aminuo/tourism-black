package com.example.tourismblack.repository;

import com.example.tourismblack.entity.ScenicSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotRepository extends JpaRepository<ScenicSpot, Integer> {
    // JpaRepository已经提供了基本的CRUD操作，包括查询全部数据的方法
}

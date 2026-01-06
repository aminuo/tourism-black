package com.example.tourismblack.repository;

import com.example.tourismblack.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    // JpaRepository已经提供了基本的CRUD操作，包括查询全部数据的方法
}
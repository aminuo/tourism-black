package com.example.tourismblack.repository;

import com.example.tourismblack.entity.Favorite;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    // 根据用户和景点查询收藏记录
    Optional<Favorite> findByUserAndScenicSpot(User user, ScenicSpot scenicSpot);
    
    // 根据用户查询所有收藏的景点
    List<Favorite> findByUser(User user);
    
    // 根据用户ID查询所有收藏的景点
    List<Favorite> findByUserId(Integer userId);
}
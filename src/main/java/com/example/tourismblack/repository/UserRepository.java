package com.example.tourismblack.repository;

import com.example.tourismblack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByOpenid(String openid);
}
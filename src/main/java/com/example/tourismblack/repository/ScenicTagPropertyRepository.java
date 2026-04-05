package com.example.tourismblack.repository;

import com.example.tourismblack.entity.ScenicTagProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenicTagPropertyRepository extends JpaRepository<ScenicTagProperty, Integer> {
    List<ScenicTagProperty> findByScenicId(Integer scenicId);
}
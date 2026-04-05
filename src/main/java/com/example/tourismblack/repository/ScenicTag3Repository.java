package com.example.tourismblack.repository;

import com.example.tourismblack.entity.ScenicTag3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenicTag3Repository extends JpaRepository<ScenicTag3, Integer> {
    List<ScenicTag3> findByScenicId(Integer scenicId);
}
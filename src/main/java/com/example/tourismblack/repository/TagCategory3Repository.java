package com.example.tourismblack.repository;

import com.example.tourismblack.entity.TagCategory3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagCategory3Repository extends JpaRepository<TagCategory3, Integer> {
    List<TagCategory3> findByC2Id(Integer c2Id);
    Optional<TagCategory3> findByCode(String code);
}
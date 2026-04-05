package com.example.tourismblack.repository;

import com.example.tourismblack.entity.TagCategory2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagCategory2Repository extends JpaRepository<TagCategory2, Integer> {
    List<TagCategory2> findByC1Id(Integer c1Id);
}
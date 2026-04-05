package com.example.tourismblack.repository;

import com.example.tourismblack.entity.TagCategory1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagCategory1Repository extends JpaRepository<TagCategory1, Integer> {
}
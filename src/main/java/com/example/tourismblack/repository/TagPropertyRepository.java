package com.example.tourismblack.repository;

import com.example.tourismblack.entity.TagProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagPropertyRepository extends JpaRepository<TagProperty, Integer> {
    List<TagProperty> findByType(String type);
    Optional<TagProperty> findByCode(String code);
}
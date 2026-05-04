package com.example.tourismblack.repository;

import com.example.tourismblack.entity.ScenicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenicCommentRepository extends JpaRepository<ScenicComment, Integer> {
    List<ScenicComment> findByScenicId(Integer scenicId);

    List<ScenicComment> findByUserId(Integer userId);

    List<ScenicComment> findByScenicIdAndStatus(Integer scenicId, Integer status);

    List<ScenicComment> findByUserIdAndStatus(Integer userId, Integer status);

    List<ScenicComment> findByStatus(Integer status);
}
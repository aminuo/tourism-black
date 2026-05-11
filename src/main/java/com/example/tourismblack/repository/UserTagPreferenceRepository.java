package com.example.tourismblack.repository;

import com.example.tourismblack.entity.UserTagPreference;
import com.example.tourismblack.entity.UserTagPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTagPreferenceRepository extends JpaRepository<UserTagPreference, UserTagPreferenceId> {

    List<UserTagPreference> findByIdUserId(Integer userId);

    List<UserTagPreference> findByIdUserIdOrderByPreferenceScoreDesc(Integer userId);
}
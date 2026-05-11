package com.example.tourismblack.service;

import com.example.tourismblack.entity.*;
import com.example.tourismblack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserTagPreferenceService {

    @Autowired
    private UserTagPreferenceRepository userTagPreferenceRepository;

    @Autowired
    private ScenicTag3Repository scenicTag3Repository;

    @Autowired
    private ScenicTagPropertyRepository scenicTagPropertyRepository;

    @Autowired
    private TagCategory3Repository tagCategory3Repository;

    @Autowired
    private TagPropertyRepository tagPropertyRepository;

    @Autowired
    private ScenicSpotRepository scenicSpotRepository;

    @Transactional
    public void updatePreferencesOnView(Integer userId, Integer scenicSpotId) {
        List<Integer> tagIds = getTagIdsForScenicSpot(scenicSpotId);

        for (Integer tagId : tagIds) {
            updateViewCount(userId, tagId);
        }
    }

    @Transactional
    public void updatePreferencesOnCollect(Integer userId, Integer scenicSpotId, boolean isCollect) {
        List<Integer> tagIds = getTagIdsForScenicSpot(scenicSpotId);

        for (Integer tagId : tagIds) {
            if (isCollect) {
                updateCollectCount(userId, tagId, true);
            } else {
                updateCollectCount(userId, tagId, false);
            }
        }
    }

    private List<Integer> getTagIdsForScenicSpot(Integer scenicSpotId) {
        List<Integer> tagIds = new ArrayList<>();

        List<ScenicTag3> scenicTag3s = scenicTag3Repository.findByScenicId(scenicSpotId);
        for (ScenicTag3 st3 : scenicTag3s) {
            tagCategory3Repository.findByCode(st3.getTag3Code()).ifPresent(tag3 -> {
                tagIds.add(tag3.getId());
            });
        }

        List<ScenicTagProperty> scenicTagProperties = scenicTagPropertyRepository.findByScenicId(scenicSpotId);
        for (ScenicTagProperty stp : scenicTagProperties) {
            tagPropertyRepository.findByCode(stp.getPropertyCode()).ifPresent(property -> {
                tagIds.add(property.getId());
            });
        }

        return tagIds;
    }

    private void updateViewCount(Integer userId, Integer tagId) {
        UserTagPreferenceId id = new UserTagPreferenceId(userId, tagId);
        UserTagPreference preference = userTagPreferenceRepository.findById(id)
                .orElse(new UserTagPreference(userId, tagId));

        preference.incrementViewCount();
        userTagPreferenceRepository.save(preference);
    }

    private void updateCollectCount(Integer userId, Integer tagId, boolean increment) {
        UserTagPreferenceId id = new UserTagPreferenceId(userId, tagId);
        UserTagPreference preference = userTagPreferenceRepository.findById(id)
                .orElse(new UserTagPreference(userId, tagId));

        if (increment) {
            preference.incrementCollectCount();
        } else {
            preference.decrementCollectCount();
        }
        userTagPreferenceRepository.save(preference);
    }

    public List<UserTagPreference> getUserPreferences(Integer userId) {
        return userTagPreferenceRepository.findByIdUserIdOrderByPreferenceScoreDesc(userId);
    }

    public Map<String, Object> getUserStats(Integer userId) {
        List<UserTagPreference> preferences = userTagPreferenceRepository.findByIdUserId(userId);

        int totalViewCount = 0;
        int totalCollectCount = 0;
        double totalPreferenceScore = 0.0;

        for (UserTagPreference pref : preferences) {
            totalViewCount += pref.getViewCount() != null ? pref.getViewCount() : 0;
            totalCollectCount += pref.getCollectCount() != null ? pref.getCollectCount() : 0;
            totalPreferenceScore += pref.getPreferenceScore() != null ? pref.getPreferenceScore() : 0.0;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalViewCount", totalViewCount);
        stats.put("totalCollectCount", totalCollectCount);
        stats.put("totalPreferenceScore", totalPreferenceScore);
        stats.put("tagCount", preferences.size());

        return stats;
    }

    public List<ScenicSpot> getScenicSpotsSortedByPreference(Integer userId) {
        List<UserTagPreference> preferences = userTagPreferenceRepository.findByIdUserIdOrderByPreferenceScoreDesc(userId);

        if (preferences.isEmpty()) {
            return scenicSpotRepository.findAll();
        }

        Map<Integer, Double> tagScoreMap = preferences.stream()
                .collect(Collectors.toMap(
                        p -> p.getId().getTagId(),
                        UserTagPreference::getPreferenceScore,
                        (v1, v2) -> v1
                ));

        List<ScenicSpot> allSpots = scenicSpotRepository.findAll();

        Map<Integer, Double> scenicScoreMap = new HashMap<>();
        for (ScenicSpot spot : allSpots) {
            double totalScore = calculateScenicSpotScore(spot.getId(), tagScoreMap);
            scenicScoreMap.put(spot.getId(), totalScore);
        }

        return allSpots.stream()
                .sorted((s1, s2) -> Double.compare(
                        scenicScoreMap.getOrDefault(s2.getId(), 0.0),
                        scenicScoreMap.getOrDefault(s1.getId(), 0.0)
                ))
                .collect(Collectors.toList());
    }

    private double calculateScenicSpotScore(Integer scenicSpotId, Map<Integer, Double> tagScoreMap) {
        Set<Integer> spotTagIds = new HashSet<>();

        List<ScenicTag3> scenicTag3s = scenicTag3Repository.findByScenicId(scenicSpotId);
        for (ScenicTag3 st3 : scenicTag3s) {
            tagCategory3Repository.findByCode(st3.getTag3Code()).ifPresent(tag3 -> {
                spotTagIds.add(tag3.getId());
            });
        }

        List<ScenicTagProperty> scenicTagProperties = scenicTagPropertyRepository.findByScenicId(scenicSpotId);
        for (ScenicTagProperty stp : scenicTagProperties) {
            tagPropertyRepository.findByCode(stp.getPropertyCode()).ifPresent(property -> {
                spotTagIds.add(property.getId());
            });
        }

        double totalScore = 0.0;
        for (Integer tagId : spotTagIds) {
            Double score = tagScoreMap.get(tagId);
            if (score != null) {
                totalScore += score;
            }
        }

        return totalScore;
    }
}
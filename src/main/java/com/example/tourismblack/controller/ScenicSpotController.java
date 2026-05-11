package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.entity.TagCategory3;
import com.example.tourismblack.entity.TagProperty;
import com.example.tourismblack.entity.ScenicTag3;
import com.example.tourismblack.entity.ScenicTagProperty;
import com.example.tourismblack.entity.SearchHistory;
import com.example.tourismblack.entity.User;
import com.example.tourismblack.repository.ScenicSpotRepository;
import com.example.tourismblack.repository.ScenicTag3Repository;
import com.example.tourismblack.repository.ScenicTagPropertyRepository;
import com.example.tourismblack.repository.TagCategory3Repository;
import com.example.tourismblack.repository.TagPropertyRepository;
import com.example.tourismblack.repository.SearchHistoryRepository;
import com.example.tourismblack.repository.UserRepository;
import com.example.tourismblack.service.UserTagPreferenceService;
import com.example.tourismblack.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/scenic-spots")
public class ScenicSpotController {

    @Autowired
    private ScenicSpotRepository scenicSpotRepository;

    @Autowired
    private ScenicTag3Repository scenicTag3Repository;

    @Autowired
    private ScenicTagPropertyRepository scenicTagPropertyRepository;

    @Autowired
    private TagCategory3Repository tagCategory3Repository;

    @Autowired
    private TagPropertyRepository tagPropertyRepository;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTagPreferenceService userTagPreferenceService;

    @GetMapping
    public ResponseResult<List<Map<String, Object>>> getAllScenicSpots(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tagCode", required = false) String tagCode,
            @RequestParam(value = "c3Code", required = false) String c3Code,
            @RequestParam(value = "propertyCode", required = false) String propertyCode,
            @RequestHeader(value = "token", required = false) String token) {
        List<ScenicSpot> scenicSpots;

        boolean hasC3 = c3Code != null && !c3Code.isEmpty();
        boolean hasProperty = propertyCode != null && !propertyCode.isEmpty();

        if (hasC3 && hasProperty) {
            if (title != null && !title.isEmpty()) {
                scenicSpots = scenicSpotRepository.findByTitleContainingAndTag3CodeAndPropertyCode(title, c3Code, propertyCode);
            } else {
                scenicSpots = scenicSpotRepository.findByTag3CodeAndPropertyCode(c3Code, propertyCode);
            }
        } else if (hasC3) {
            if (title != null && !title.isEmpty()) {
                scenicSpots = scenicSpotRepository.findByTitleContainingAndTag3Code(title, c3Code);
            } else {
                scenicSpots = scenicSpotRepository.findByTag3Code(c3Code);
            }
        } else if (hasProperty) {
            if (title != null && !title.isEmpty()) {
                scenicSpots = scenicSpotRepository.findByTitleContainingAndPropertyCode(title, propertyCode);
            } else {
                scenicSpots = scenicSpotRepository.findByPropertyCode(propertyCode);
            }
        } else if (tagCode != null && !tagCode.isEmpty()) {
            if (title != null && !title.isEmpty()) {
                if (tagCode.startsWith("C1_")) {
                    Integer id = Integer.parseInt(tagCode.substring(3));
                    scenicSpots = scenicSpotRepository.findByTitleContainingAndC1Id(title, id);
                } else if (tagCode.startsWith("C2_")) {
                    Integer id = Integer.parseInt(tagCode.substring(3));
                    scenicSpots = scenicSpotRepository.findByTitleContainingAndC2Id(title, id);
                } else {
                    return ResponseResult.error(400, null);
                }
            } else {
                if (tagCode.startsWith("C1_")) {
                    Integer id = Integer.parseInt(tagCode.substring(3));
                    scenicSpots = scenicSpotRepository.findByC1Id(id);
                } else if (tagCode.startsWith("C2_")) {
                    Integer id = Integer.parseInt(tagCode.substring(3));
                    scenicSpots = scenicSpotRepository.findByC2Id(id);
                } else {
                    return ResponseResult.error(400, null);
                }
            }
        } else if (title != null && !title.isEmpty()) {
            scenicSpots = scenicSpotRepository.findByTitleContaining(title);
        } else {
            scenicSpots = scenicSpotRepository.findAll();
        }

        if (title != null && !title.isEmpty() && token != null) {
            try {
                if (JWTUtil.validateToken(token)) {
                    String openid = JWTUtil.getOpenidFromToken(token);
                    if (openid != null) {
                        User user = userRepository.findByOpenid(openid);
                        if (user != null) {
                            SearchHistory existingHistory = searchHistoryRepository.findByUserIdAndKeyword(user.getId(), title);
                            if (existingHistory != null) {
                                searchHistoryRepository.delete(existingHistory);
                            }
                            SearchHistory searchHistory = new SearchHistory();
                            searchHistory.setUserId(user.getId());
                            searchHistory.setKeyword(title);
                            searchHistoryRepository.save(searchHistory);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (ScenicSpot scenicSpot : scenicSpots) {
            resultList.add(buildScenicSpotWithTags(scenicSpot));
        }
        return ResponseResult.success(resultList);
    }

    @GetMapping("/preference")
    public ResponseResult<List<Map<String, Object>>> getScenicSpotsByPreference(
            @RequestHeader(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseResult.error(401, null);
        }

        try {
            if (!JWTUtil.validateToken(token)) {
                return ResponseResult.error(401, null);
            }

            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                return ResponseResult.error(401, null);
            }

            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                return ResponseResult.error(404, null);
            }

            List<ScenicSpot> scenicSpots = userTagPreferenceService.getScenicSpotsSortedByPreference(user.getId());

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (ScenicSpot scenicSpot : scenicSpots) {
                resultList.add(buildScenicSpotWithTags(scenicSpot));
            }
            return ResponseResult.success(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }

    @GetMapping("/preference/stats")
    public ResponseResult<Map<String, Object>> getUserPreferenceStats(
            @RequestHeader(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseResult.error(401, null);
        }

        try {
            if (!JWTUtil.validateToken(token)) {
                return ResponseResult.error(401, null);
            }

            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                return ResponseResult.error(401, null);
            }

            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                return ResponseResult.error(404, null);
            }

            Map<String, Object> stats = userTagPreferenceService.getUserStats(user.getId());
            return ResponseResult.success(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseResult<Map<String, Object>> getScenicSpotById(
            @PathVariable Integer id,
            @RequestHeader(value = "token", required = false) String token) {
        Optional<ScenicSpot> optionalScenicSpot = scenicSpotRepository.findById(id);
        if (optionalScenicSpot.isPresent()) {
            ScenicSpot scenicSpot = optionalScenicSpot.get();
            if (scenicSpot.getViewCount() == null) {
                scenicSpot.setViewCount(0);
            }
            scenicSpot.setViewCount(scenicSpot.getViewCount() + 1);
            scenicSpot.updateHotMetrics();
            scenicSpotRepository.save(scenicSpot);

            if (token != null && !token.isEmpty()) {
                try {
                    if (JWTUtil.validateToken(token)) {
                        String openid = JWTUtil.getOpenidFromToken(token);
                        if (openid != null) {
                            User user = userRepository.findByOpenid(openid);
                            if (user != null) {
                                userTagPreferenceService.updatePreferencesOnView(user.getId(), id);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return ResponseResult.success(buildScenicSpotWithTags(scenicSpot));
        } else {
            return ResponseResult.error(404, null);
        }
    }

    private Map<String, Object> buildScenicSpotWithTags(ScenicSpot scenicSpot) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", scenicSpot.getId());
        result.put("title", scenicSpot.getTitle());
        result.put("img", scenicSpot.getImg());
        result.put("introduce", scenicSpot.getIntroduce());
        result.put("address", scenicSpot.getAddress());
        result.put("times", scenicSpot.getTimes());
        result.put("hotStatus", scenicSpot.getHotStatus());

        List<Map<String, Object>> tagsList = new ArrayList<>();

        List<ScenicTag3> scenicTag3s = scenicTag3Repository.findByScenicId(scenicSpot.getId());
        for (ScenicTag3 st3 : scenicTag3s) {
            TagCategory3 tag3 = tagCategory3Repository.findByCode(st3.getTag3Code()).orElse(null);
            if (tag3 != null) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", tag3.getId());
                tagMap.put("code", tag3.getCode());
                tagMap.put("name", tag3.getName());
                tagMap.put("type", "category");
                tagsList.add(tagMap);
            }
        }

        List<ScenicTagProperty> stps = scenicTagPropertyRepository.findByScenicId(scenicSpot.getId());
        for (ScenicTagProperty stp : stps) {
            TagProperty prop = tagPropertyRepository.findByCode(stp.getPropertyCode()).orElse(null);
            if (prop != null) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", prop.getId());
                tagMap.put("code", prop.getCode());
                tagMap.put("name", prop.getName());
                tagMap.put("type", prop.getType());
                tagsList.add(tagMap);
            }
        }

        result.put("tags", tagsList);

        return result;
    }
}
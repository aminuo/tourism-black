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

    /**
     * 查询景点数据，支持按名称和标签查询
     * 
     * @param title        景点名称
     * @param tagCode      标签编码（格式：类型前缀_实际ID，如C1_1、C2_1，用于一级和二级标签）
     * @param c3Code       三级标签code（C3_xxx格式）
     * @param propertyCode 属性标签code（P_xxx格式）
     * @param token        用户token（可选）
     * @return 景点列表
     */
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

        // 保存搜索历史（如果有搜索关键词且用户已登录）
        if (title != null && !title.isEmpty() && token != null) {
            try {
                // 验证token
                if (JWTUtil.validateToken(token)) {
                    // 从token中提取openid
                    String openid = JWTUtil.getOpenidFromToken(token);
                    if (openid != null) {
                        // 根据openid查询用户
                        User user = userRepository.findByOpenid(openid);
                        if (user != null) {
                            // 检查是否已存在相同的搜索记录
                            SearchHistory existingHistory = searchHistoryRepository.findByUserIdAndKeyword(user.getId(),
                                    title);
                            if (existingHistory != null) {
                                // 如果已存在，删除旧记录
                                searchHistoryRepository.delete(existingHistory);
                            }
                            // 创建新的搜索历史记录
                            SearchHistory searchHistory = new SearchHistory();
                            searchHistory.setUserId(user.getId());
                            searchHistory.setKeyword(title);
                            searchHistoryRepository.save(searchHistory);
                        }
                    }
                }
            } catch (Exception e) {
                // 搜索历史保存失败不影响搜索结果
                e.printStackTrace();
            }
        }

        // 构建返回数据，包含标签信息
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (ScenicSpot scenicSpot : scenicSpots) {
            resultList.add(buildScenicSpotWithTags(scenicSpot));
        }
        return ResponseResult.success(resultList);
    }

    /**
     * 通过id查询景点详情
     * 
     * @param id 景点id
     * @return 景点详情
     */
    @GetMapping("/{id}")
    public ResponseResult<Map<String, Object>> getScenicSpotById(@PathVariable Integer id) {
        Optional<ScenicSpot> optionalScenicSpot = scenicSpotRepository.findById(id);
        if (optionalScenicSpot.isPresent()) {
            ScenicSpot scenicSpot = optionalScenicSpot.get();
            // 增加浏览量
            if (scenicSpot.getViewCount() == null) {
                scenicSpot.setViewCount(0);
            }
            scenicSpot.setViewCount(scenicSpot.getViewCount() + 1);
            // 更新热度指标
            scenicSpot.updateHotMetrics();
            scenicSpotRepository.save(scenicSpot);
            return ResponseResult.success(buildScenicSpotWithTags(scenicSpot));
        } else {
            return ResponseResult.error(404, null);
        }
    }

    /**
     * 构建包含标签信息的景点数据
     * 
     * @param scenicSpot 景点实体
     * @return 包含标签信息的景点数据
     */
    private Map<String, Object> buildScenicSpotWithTags(ScenicSpot scenicSpot) {
        Map<String, Object> result = new HashMap<>();
        // 基本景点信息
        result.put("id", scenicSpot.getId());
        result.put("title", scenicSpot.getTitle());
        result.put("img", scenicSpot.getImg());
        result.put("introduce", scenicSpot.getIntroduce());
        result.put("address", scenicSpot.getAddress());
        result.put("times", scenicSpot.getTimes());
        result.put("hotStatus", scenicSpot.getHotStatus());

        // 合并标签（三级标签和属性标签）
        List<Map<String, Object>> tagsList = new ArrayList<>();

        // 添加三级标签
        List<ScenicTag3> scenicTag3s = scenicTag3Repository.findByScenicId(scenicSpot.getId());
        for (ScenicTag3 st3 : scenicTag3s) {
            TagCategory3 tag3 = tagCategory3Repository.findByCode(st3.getTag3Code()).orElse(null);
            if (tag3 != null) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", tag3.getId());
                tagMap.put("code", tag3.getCode());
                tagMap.put("name", tag3.getName());
                tagMap.put("type", "category"); // 标记为分类标签
                tagsList.add(tagMap);
            }
        }

        // 添加属性标签
        List<ScenicTagProperty> stps = scenicTagPropertyRepository.findByScenicId(scenicSpot.getId());
        for (ScenicTagProperty stp : stps) {
            TagProperty prop = tagPropertyRepository.findByCode(stp.getPropertyCode()).orElse(null);
            if (prop != null) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", prop.getId());
                tagMap.put("code", prop.getCode());
                tagMap.put("name", prop.getName());
                tagMap.put("type", prop.getType()); // 使用属性标签的类型
                tagsList.add(tagMap);
            }
        }

        result.put("tags", tagsList);

        return result;
    }
}

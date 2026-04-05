package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.entity.TagCategory3;
import com.example.tourismblack.entity.TagProperty;
import com.example.tourismblack.entity.ScenicTag3;
import com.example.tourismblack.entity.ScenicTagProperty;
import com.example.tourismblack.repository.ScenicSpotRepository;
import com.example.tourismblack.repository.ScenicTag3Repository;
import com.example.tourismblack.repository.ScenicTagPropertyRepository;
import com.example.tourismblack.repository.TagCategory3Repository;
import com.example.tourismblack.repository.TagPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * 查询景点数据，支持按名称和标签查询
     * 
     * @param title   景点名称
     * @param tagCode 标签编码（格式：类型前缀_实际ID，如C1_1、C2_1、C3_1、P_1）
     * @return 景点列表
     */
    @GetMapping
    public ResponseResult<List<Map<String, Object>>> getAllScenicSpots(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tagCode", required = false) String tagCode) {
        List<ScenicSpot> scenicSpots;

        // 根据标签编码前缀判断标签类型
        if (title != null && tagCode != null) {
            // 按标题和标签查询
            if (tagCode.startsWith("C1_")) {
                // 一级标签
                Integer id = Integer.parseInt(tagCode.substring(3));
                scenicSpots = scenicSpotRepository.findByTitleContainingAndC1Id(title, id);
            } else if (tagCode.startsWith("C2_")) {
                // 二级标签
                Integer id = Integer.parseInt(tagCode.substring(3));
                scenicSpots = scenicSpotRepository.findByTitleContainingAndC2Id(title, id);
            } else if (tagCode.startsWith("C3_")) {
                // 三级标签
                scenicSpots = scenicSpotRepository.findByTitleContainingAndTag3Code(title, tagCode);
            } else if (tagCode.startsWith("P_")) {
                // 属性标签
                scenicSpots = scenicSpotRepository.findByTitleContainingAndPropertyCode(title, tagCode);
            } else {
                // 无效的标签编码
                return ResponseResult.error(400, null);
            }
        } else if (tagCode != null) {
            // 按标签查询
            if (tagCode.startsWith("C1_")) {
                // 一级标签
                Integer id = Integer.parseInt(tagCode.substring(3));
                scenicSpots = scenicSpotRepository.findByC1Id(id);
            } else if (tagCode.startsWith("C2_")) {
                // 二级标签
                Integer id = Integer.parseInt(tagCode.substring(3));
                scenicSpots = scenicSpotRepository.findByC2Id(id);
            } else if (tagCode.startsWith("C3_")) {
                // 三级标签
                scenicSpots = scenicSpotRepository.findByTag3Code(tagCode);
            } else if (tagCode.startsWith("P_")) {
                // 属性标签
                scenicSpots = scenicSpotRepository.findByPropertyCode(tagCode);
            } else {
                // 无效的标签编码
                return ResponseResult.error(400, null);
            }
        } else if (title != null) {
            // 按标题查询
            scenicSpots = scenicSpotRepository.findByTitleContaining(title);
        } else {
            // 查询所有
            scenicSpots = scenicSpotRepository.findAll();
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
            return ResponseResult.success(buildScenicSpotWithTags(optionalScenicSpot.get()));
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

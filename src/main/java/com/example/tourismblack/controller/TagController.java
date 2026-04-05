package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.TagCategory1;
import com.example.tourismblack.entity.TagCategory2;
import com.example.tourismblack.entity.TagCategory3;
import com.example.tourismblack.entity.TagProperty;
import com.example.tourismblack.repository.TagCategory1Repository;
import com.example.tourismblack.repository.TagCategory2Repository;
import com.example.tourismblack.repository.TagCategory3Repository;
import com.example.tourismblack.repository.TagPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagCategory1Repository tagCategory1Repository;

    @Autowired
    private TagCategory2Repository tagCategory2Repository;

    @Autowired
    private TagCategory3Repository tagCategory3Repository;

    @Autowired
    private TagPropertyRepository tagPropertyRepository;

    /**
     * 获取标签分类体系
     * 
     * @return 标签分类体系
     */
    @GetMapping("/categories")
    public ResponseResult<Map<String, Object>> getTagCategories() {
        try {
            // 获取一级分类
            List<TagCategory1> category1List = tagCategory1Repository.findAll();

            // 构建分类体系
            List<Map<String, Object>> categories = new java.util.ArrayList<>();
            for (TagCategory1 category1 : category1List) {
                Map<String, Object> category1Map = new HashMap<>();
                category1Map.put("id", category1.getId());
                category1Map.put("code", category1.getCode());
                category1Map.put("name", category1.getName());
                category1Map.put("sort", category1.getSort());

                // 获取二级分类
                List<TagCategory2> category2List = tagCategory2Repository.findByC1Code(category1.getCode());
                List<Map<String, Object>> category2s = new java.util.ArrayList<>();
                for (TagCategory2 category2 : category2List) {
                    Map<String, Object> category2Map = new HashMap<>();
                    category2Map.put("id", category2.getId());
                    category2Map.put("code", category2.getCode());
                    category2Map.put("c1Code", category2.getC1Code());
                    category2Map.put("name", category2.getName());
                    category2Map.put("sort", category2.getSort());

                    // 获取三级分类
                    List<TagCategory3> category3List = tagCategory3Repository.findByC2Code(category2.getCode());
                    List<Map<String, Object>> category3s = new java.util.ArrayList<>();
                    for (TagCategory3 category3 : category3List) {
                        Map<String, Object> category3Map = new HashMap<>();
                        category3Map.put("id", category3.getId());
                        category3Map.put("code", category3.getCode());
                        category3Map.put("c2Code", category3.getC2Code());
                        category3Map.put("name", category3.getName());
                        category3Map.put("sort", category3.getSort());
                        category3s.add(category3Map);
                    }
                    category2Map.put("children", category3s);
                    category2s.add(category2Map);
                }
                category1Map.put("children", category2s);
                categories.add(category1Map);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories);
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "获取标签分类失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }

    /**
     * 获取标签属性
     * 
     * @return 标签属性列表
     */
    @GetMapping("/properties")
    public ResponseResult<Map<String, Object>> getTagProperties() {
        try {
            // 获取所有标签属性
            List<TagProperty> properties = tagPropertyRepository.findAll();

            // 按类型分组
            Map<String, List<Map<String, Object>>> propertiesByType = new HashMap<>();
            for (TagProperty property : properties) {
                String type = property.getType();
                if (!propertiesByType.containsKey(type)) {
                    propertiesByType.put(type, new java.util.ArrayList<>());
                }
                Map<String, Object> propertyMap = new HashMap<>();
                propertyMap.put("id", property.getId());
                propertyMap.put("code", property.getCode());
                propertyMap.put("name", property.getName());
                propertyMap.put("type", property.getType());
                propertyMap.put("sort", property.getSort());
                propertiesByType.get(type).add(propertyMap);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("properties", propertiesByType);
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "获取标签属性失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }
}

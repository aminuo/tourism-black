package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.Favorite;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.entity.TagCategory3;
import com.example.tourismblack.entity.TagProperty;
import com.example.tourismblack.entity.User;
import com.example.tourismblack.repository.FavoriteRepository;
import com.example.tourismblack.repository.ScenicSpotRepository;
import com.example.tourismblack.repository.ScenicTag3Repository;
import com.example.tourismblack.repository.ScenicTagPropertyRepository;
import com.example.tourismblack.repository.TagCategory3Repository;
import com.example.tourismblack.repository.TagPropertyRepository;
import com.example.tourismblack.repository.UserRepository;
import com.example.tourismblack.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

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

    // 添加收藏
    @PostMapping("/addFavorite")
    public ResponseResult<Map<String, Object>> addFavorite(@RequestHeader("token") String token, @RequestBody Map<String, Integer> request) {
        try {
            // 验证token
            if (!JWTUtil.validateToken(token)) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "token无效或已过期");
                return ResponseResult.error(401, errorData);
            }

            // 从token中提取openid
            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "无法从token中获取openid");
                return ResponseResult.error(401, errorData);
            }

            // 根据openid查询用户
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "用户不存在");
                return ResponseResult.error(404, errorData);
            }

            // 获取景点ID
            Integer scenicSpotId = request.get("scenicSpotId");
            if (scenicSpotId == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "景点ID不能为空");
                return ResponseResult.error(400, errorData);
            }

            // 查询景点
            ScenicSpot scenicSpot = scenicSpotRepository.findById(scenicSpotId).orElse(null);
            if (scenicSpot == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "景点不存在");
                return ResponseResult.error(404, errorData);
            }

            // 检查是否已经收藏
            if (favoriteRepository.findByUserAndScenicSpot(user, scenicSpot).isPresent()) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "已经收藏过该景点");
                return ResponseResult.error(400, errorData);
            }

            // 创建收藏记录
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setScenicSpot(scenicSpot);
            favoriteRepository.save(favorite);

            // 返回成功信息
            Map<String, Object> result = new HashMap<>();
            result.put("message", "收藏成功");
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "添加收藏失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }

    // 取消收藏
    @PostMapping("/removeFavorite")
    public ResponseResult<Map<String, Object>> removeFavorite(@RequestHeader("token") String token, @RequestBody Map<String, Integer> request) {
        try {
            // 验证token
            if (!JWTUtil.validateToken(token)) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "token无效或已过期");
                return ResponseResult.error(401, errorData);
            }

            // 从token中提取openid
            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "无法从token中获取openid");
                return ResponseResult.error(401, errorData);
            }

            // 根据openid查询用户
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "用户不存在");
                return ResponseResult.error(404, errorData);
            }

            // 获取景点ID
            Integer scenicSpotId = request.get("scenicSpotId");
            if (scenicSpotId == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "景点ID不能为空");
                return ResponseResult.error(400, errorData);
            }

            // 查询景点
            ScenicSpot scenicSpot = scenicSpotRepository.findById(scenicSpotId).orElse(null);
            if (scenicSpot == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "景点不存在");
                return ResponseResult.error(404, errorData);
            }

            // 查找收藏记录
            Favorite favorite = favoriteRepository.findByUserAndScenicSpot(user, scenicSpot).orElse(null);
            if (favorite == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "未收藏该景点");
                return ResponseResult.error(400, errorData);
            }

            // 删除收藏记录
            favoriteRepository.delete(favorite);

            // 返回成功信息
            Map<String, Object> result = new HashMap<>();
            result.put("message", "取消收藏成功");
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "取消收藏失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }

    // 获取用户收藏的景点列表
    @GetMapping("/getFavorites")
    public ResponseResult<Map<String, Object>> getFavorites(@RequestHeader("token") String token) {
        try {
            // 验证token
            if (!JWTUtil.validateToken(token)) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "token无效或已过期");
                return ResponseResult.error(401, errorData);
            }

            // 从token中提取openid
            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "无法从token中获取openid");
                return ResponseResult.error(401, errorData);
            }

            // 根据openid查询用户
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "用户不存在");
                return ResponseResult.error(404, errorData);
            }

            // 查询用户收藏的景点
            List<Favorite> favorites = favoriteRepository.findByUser(user);

            // 构建返回数据
            List<Map<String, Object>> scenicSpotList = new ArrayList<>();
            for (Favorite favorite : favorites) {
                ScenicSpot scenicSpot = favorite.getScenicSpot();
                scenicSpotList.add(buildScenicSpotWithTags(scenicSpot));
            }

            // 返回成功信息
            Map<String, Object> result = new HashMap<>();
            result.put("favorites", scenicSpotList);
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "获取收藏列表失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }

    /**
     * 构建包含标签信息的景点数据
     * @param scenicSpot 景点实体
     * @return 包含标签信息的景点数据
     */
    private Map<String, Object> buildScenicSpotWithTags(ScenicSpot scenicSpot) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", scenicSpot.getId());
        result.put("title", scenicSpot.getTitle());
        result.put("img", scenicSpot.getImg());
        result.put("introduce", scenicSpot.getIntroduce());
        result.put("address", scenicSpot.getAddress());
        result.put("times", scenicSpot.getTimes());

        // 合并标签（三级标签和属性标签）
        List<Map<String, Object>> tagsList = new ArrayList<>();
        
        // 添加三级标签
        List<com.example.tourismblack.entity.ScenicTag3> scenicTag3s = scenicTag3Repository.findByScenicId(scenicSpot.getId());
        for (com.example.tourismblack.entity.ScenicTag3 scenicTag3 : scenicTag3s) {
            Optional<TagCategory3> optionalTag3 = tagCategory3Repository.findByCode(scenicTag3.getTag3Code());
            if (optionalTag3.isPresent()) {
                TagCategory3 tag3 = optionalTag3.get();
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", tag3.getId());
                tagMap.put("code", tag3.getCode());
                tagMap.put("name", tag3.getName());
                tagMap.put("type", "category"); // 标记为分类标签
                tagsList.add(tagMap);
            }
        }
        
        // 添加属性标签
        List<com.example.tourismblack.entity.ScenicTagProperty> scenicTagProperties = scenicTagPropertyRepository.findByScenicId(scenicSpot.getId());
        for (com.example.tourismblack.entity.ScenicTagProperty scenicTagProperty : scenicTagProperties) {
            Optional<TagProperty> optionalProperty = tagPropertyRepository.findByCode(scenicTagProperty.getPropertyCode());
            if (optionalProperty.isPresent()) {
                TagProperty property = optionalProperty.get();
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", property.getId());
                tagMap.put("code", property.getCode());
                tagMap.put("name", property.getName());
                tagMap.put("type", property.getType()); // 使用属性标签的类型
                tagsList.add(tagMap);
            }
        }
        
        result.put("tags", tagsList);

        return result;
    }
}
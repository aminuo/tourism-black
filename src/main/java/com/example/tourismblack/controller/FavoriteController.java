package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.Favorite;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.entity.Tag;
import com.example.tourismblack.entity.User;
import com.example.tourismblack.repository.FavoriteRepository;
import com.example.tourismblack.repository.ScenicSpotRepository;
import com.example.tourismblack.repository.UserRepository;
import com.example.tourismblack.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 添加收藏
    @PostMapping("/addFavorite")
    public ResponseResult<Map<String, Object>> addFavorite(@RequestHeader("token") String token,
            @RequestBody Map<String, Integer> request) {
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
    public ResponseResult<Map<String, Object>> removeFavorite(@RequestHeader("token") String token,
            @RequestBody Map<String, Integer> request) {
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
                Map<String, Object> scenicSpotMap = new HashMap<>();
                scenicSpotMap.put("id", scenicSpot.getId());
                scenicSpotMap.put("title", scenicSpot.getTitle());
                scenicSpotMap.put("img", scenicSpot.getImg());
                scenicSpotMap.put("introduce", scenicSpot.getIntroduce());
                scenicSpotMap.put("address", scenicSpot.getAddress());
                scenicSpotMap.put("times", scenicSpot.getTimes());

                // 添加标签信息
                List<Map<String, Object>> tagList = new ArrayList<>();
                if (scenicSpot.getTags() != null) {
                    for (Tag tag : scenicSpot.getTags()) {
                        Map<String, Object> tagMap = new HashMap<>();
                        tagMap.put("id", tag.getId());
                        tagMap.put("name", tag.getName());
                        tagList.add(tagMap);
                    }
                }
                scenicSpotMap.put("tags", tagList);

                scenicSpotList.add(scenicSpotMap);
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
}
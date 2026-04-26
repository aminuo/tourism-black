package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.SearchHistory;
import com.example.tourismblack.entity.User;
import com.example.tourismblack.repository.SearchHistoryRepository;
import com.example.tourismblack.repository.UserRepository;
import com.example.tourismblack.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/search-history")
public class SearchHistoryController {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 保存搜索历史
     * 
     * @param token   用户token
     * @param request 包含搜索关键词的请求
     * @return 保存结果
     */
    @PostMapping
    public ResponseResult<Map<String, Object>> saveSearchHistory(
            @RequestHeader("token") String token,
            @RequestBody Map<String, String> request) {
        try {
            // 验证token
            if (!JWTUtil.validateToken(token)) {
                return ResponseResult.error(401, null);
            }

            // 从token中提取openid
            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                return ResponseResult.error(401, null);
            }

            // 根据openid查询用户
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                return ResponseResult.error(404, null);
            }

            // 获取搜索关键词
            String keyword = request.get("keyword");
            if (keyword == null || keyword.isEmpty()) {
                return ResponseResult.error(400, null);
            }

            // 检查是否已存在相同的搜索记录
            SearchHistory existingHistory = searchHistoryRepository.findByUserIdAndKeyword(user.getId(), keyword);
            if (existingHistory != null) {
                // 如果已存在，删除旧记录，后续会创建新记录
                searchHistoryRepository.delete(existingHistory);
            }

            // 创建新的搜索历史记录
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setUserId(user.getId());
            searchHistory.setKeyword(keyword);
            searchHistoryRepository.save(searchHistory);

            // 返回成功结果
            Map<String, Object> result = new HashMap<>();
            result.put("message", "搜索历史保存成功");
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }

    /**
     * 获取搜索历史列表
     * 
     * @param token 用户token
     * @return 搜索历史列表
     */
    @GetMapping
    public ResponseResult<List<Map<String, Object>>> getSearchHistoryList(
            @RequestHeader("token") String token) {
        try {
            // 验证token
            if (!JWTUtil.validateToken(token)) {
                return ResponseResult.error(401, null);
            }

            // 从token中提取openid
            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                return ResponseResult.error(401, null);
            }

            // 根据openid查询用户
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                return ResponseResult.error(404, null);
            }

            // 查询搜索历史
            List<SearchHistory> searchHistories = searchHistoryRepository
                    .findByUserIdOrderByCreateTimeDesc(user.getId());

            // 构建返回数据
            List<Map<String, Object>> resultList = new java.util.ArrayList<>();
            for (SearchHistory history : searchHistories) {
                Map<String, Object> historyMap = new HashMap<>();
                historyMap.put("id", history.getId());
                historyMap.put("keyword", history.getKeyword());
                historyMap.put("createTime", history.getCreateTime());
                resultList.add(historyMap);
            }

            return ResponseResult.success(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }

    /**
     * 清空搜索历史
     * 
     * @param token 用户token
     * @return 清空结果
     */
    @DeleteMapping
    public ResponseResult<Map<String, Object>> clearSearchHistory(
            @RequestHeader("token") String token) {
        try {
            // 验证token
            if (!JWTUtil.validateToken(token)) {
                return ResponseResult.error(401, null);
            }

            // 从token中提取openid
            String openid = JWTUtil.getOpenidFromToken(token);
            if (openid == null) {
                return ResponseResult.error(401, null);
            }

            // 根据openid查询用户
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                return ResponseResult.error(404, null);
            }

            // 清空搜索历史
            searchHistoryRepository.deleteByUserId(user.getId());

            // 返回成功结果
            Map<String, Object> result = new HashMap<>();
            result.put("message", "搜索历史清空成功");
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }
}
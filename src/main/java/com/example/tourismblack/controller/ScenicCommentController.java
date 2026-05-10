package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.ScenicComment;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.entity.User;
import com.example.tourismblack.repository.ScenicCommentRepository;
import com.example.tourismblack.repository.ScenicSpotRepository;
import com.example.tourismblack.repository.UserRepository;
import com.example.tourismblack.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/scenic-comments")
public class ScenicCommentController {

    @Autowired
    private ScenicCommentRepository scenicCommentRepository;

    @Autowired
    private ScenicSpotRepository scenicSpotRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseResult<Map<String, Object>> createScenicComment(
            @RequestHeader("token") String token,
            @RequestBody Map<String, Object> request) {
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

            Integer scenicId = (Integer) request.get("scenicId");
            String content = (String) request.get("content");
            Integer score = (Integer) request.get("score");

            if (scenicId == null || content == null || content.isEmpty() || score == null) {
                return ResponseResult.error(400, null);
            }

            if (score < 1 || score > 5) {
                return ResponseResult.error(400, null);
            }

            ScenicComment comment = new ScenicComment();
            comment.setUserId(user.getId());
            comment.setScenicId(scenicId);
            comment.setContent(content);
            comment.setScore(score);
            comment.setCreateTime(LocalDateTime.now());
            // comment.setStatus(0); // 0=待审核，1=已通过，2=已驳回（暂时注释，以后可能有用）

            scenicCommentRepository.save(comment);

            // 更新景点的评论数和热度指标
            scenicSpotRepository.findById(scenicId).ifPresent(scenicSpot -> {
                if (scenicSpot.getCommentCount() == null) {
                    scenicSpot.setCommentCount(0);
                }
                scenicSpot.setCommentCount(scenicSpot.getCommentCount() + 1);
                scenicSpot.updateHotMetrics();
                scenicSpotRepository.save(scenicSpot);
            });

            Map<String, Object> result = new HashMap<>();
            result.put("message", "评论提交成功");
            result.put("id", comment.getId());
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }

    @GetMapping
    public ResponseResult<List<Map<String, Object>>> getScenicCommentList(
            @RequestParam(value = "scenicId", required = false) Integer scenicId,
            @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            List<ScenicComment> comments;

            // TODO: 以后可能需要根据审核状态过滤，暂不启用
            // if (scenicId != null && userId != null) {
            // comments = scenicCommentRepository.findByScenicIdAndStatus(scenicId, 1);
            // } else if (scenicId != null) {
            // comments = scenicCommentRepository.findByScenicIdAndStatus(scenicId, 1);
            // } else if (userId != null) {
            // comments = scenicCommentRepository.findByUserIdAndStatus(userId, 1);
            // } else {
            // comments = scenicCommentRepository.findByStatus(1);
            // }

            if (scenicId != null && userId != null) {
                comments = scenicCommentRepository.findByScenicId(scenicId);
            } else if (scenicId != null) {
                comments = scenicCommentRepository.findByScenicId(scenicId);
            } else if (userId != null) {
                comments = scenicCommentRepository.findByUserId(userId);
            } else {
                comments = scenicCommentRepository.findAll();
            }

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (ScenicComment comment : comments) {
                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("id", comment.getId());
                commentMap.put("userId", comment.getUserId());
                commentMap.put("scenicId", comment.getScenicId());
                commentMap.put("content", comment.getContent());
                commentMap.put("score", comment.getScore());
                commentMap.put("createTime", comment.getCreateTime());
                commentMap.put("status", comment.getStatus());

                User user = userRepository.findById(comment.getUserId()).orElse(null);
                if (user != null) {
                    commentMap.put("avatarUrl", user.getAvatarUrl());
                    commentMap.put("nickName", user.getNickName());
                } else {
                    commentMap.put("avatarUrl", null);
                    commentMap.put("nickName", null);
                }

                resultList.add(commentMap);
            }

            return ResponseResult.success(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(500, null);
        }
    }
}
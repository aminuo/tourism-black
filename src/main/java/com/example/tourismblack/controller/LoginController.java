package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.User;
import com.example.tourismblack.repository.UserRepository;
import com.example.tourismblack.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // 微信小程序appid和secret，从配置文件中读取
    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        if (code == null || code.isEmpty()) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "code不能为空");
            return ResponseResult.error(400, errorData);
        }

        try {
            // 调用微信接口验证code
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format(WX_LOGIN_URL, appid, secret, code);
            String response = restTemplate.getForObject(url, String.class);

            // 解析微信返回的结果
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> wxResult = objectMapper.readValue(response, Map.class);

            // 检查是否有错误
            if (wxResult.containsKey("errcode")) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "微信登录失败：" + wxResult.get("errmsg"));
                return ResponseResult.error(500, errorData);
            }

            // 获取openid和session_key
            String openid = (String) wxResult.get("openid");
            String sessionKey = (String) wxResult.get("session_key");

            // 处理用户信息
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                // 首次登录，创建新用户
                user = new User();
                user.setOpenid(openid);
                user.setSessionKey(sessionKey);
                userRepository.save(user);
            } else {
                // 非首次登录，更新session_key
                user.setSessionKey(sessionKey);
                userRepository.save(user);
            }

            // 生成token
            String token = JWTUtil.generateToken(openid);

            // 返回token和用户信息
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("openid", openid);
            result.put("userId", user.getId());

            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "登录失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }

    @GetMapping("/getUserInfo")
    public ResponseResult<Map<String, Object>> getUserInfo(@RequestHeader("token") String token) {
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

            // 根据openid查询用户信息
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "用户不存在");
                return ResponseResult.error(404, errorData);
            }

            // 返回用户信息
            Map<String, Object> result = new HashMap<>();
            result.put("id", user.getId());
            result.put("openid", user.getOpenid());
            result.put("avatarUrl", user.getAvatarUrl());
            result.put("nickName", user.getNickName());

            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "获取用户信息失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }

    @PostMapping("/updateUserInfo")
    public ResponseResult<Map<String, Object>> updateUserInfo(@RequestHeader("token") String token,
            @RequestBody Map<String, String> request) {
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

            // 根据openid查询用户信息
            User user = userRepository.findByOpenid(openid);
            if (user == null) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("message", "用户不存在");
                return ResponseResult.error(404, errorData);
            }

            // 更新用户信息
            if (request.containsKey("avatarUrl")) {
                user.setAvatarUrl(request.get("avatarUrl"));
            }
            if (request.containsKey("nickName")) {
                user.setNickName(request.get("nickName"));
            }

            // 保存更新后的用户信息
            userRepository.save(user);

            // 返回更新后的用户信息
            Map<String, Object> result = new HashMap<>();
            result.put("id", user.getId());
            result.put("openid", user.getOpenid());
            result.put("avatarUrl", user.getAvatarUrl());
            result.put("nickName", user.getNickName());

            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", "更新用户信息失败：" + e.getMessage());
            return ResponseResult.error(500, errorData);
        }
    }
}
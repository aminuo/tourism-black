package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.repository.ScenicSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/scenic-spots")
public class ScenicSpotController {

    @Autowired
    private ScenicSpotRepository scenicSpotRepository;

    /**
     * 查询景点数据，支持按名称和标签查询
     * @param title 景点名称
     * @param tag 景点标签
     * @return 景点列表
     */
    @GetMapping
    public ResponseResult<List<ScenicSpot>> getAllScenicSpots(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tag", required = false) String tag) {
        List<ScenicSpot> scenicSpots;
        if (title != null && tag != null) {
            // 同时按名称和标签查询
            scenicSpots = scenicSpotRepository.findByTitleContainingAndTagContaining(title, tag);
        } else if (title != null) {
            // 只按名称查询
            scenicSpots = scenicSpotRepository.findByTitleContaining(title);
        } else if (tag != null) {
            // 只按标签查询
            scenicSpots = scenicSpotRepository.findByTagContaining(tag);
        } else {
            // 查询所有
            scenicSpots = scenicSpotRepository.findAll();
        }
        return ResponseResult.success(scenicSpots);
    }
}

package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.ScenicSpot;
import com.example.tourismblack.repository.ScenicSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/scenic-spots")
public class ScenicSpotController {

    @Autowired
    private ScenicSpotRepository scenicSpotRepository;

    /**
     * 查询所有景点数据
     * @return 所有景点列表
     */
    @GetMapping
    public ResponseResult<List<ScenicSpot>> getAllScenicSpots() {
        return ResponseResult.success(scenicSpotRepository.findAll());
    }
}

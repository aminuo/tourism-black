package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.Banner;
import com.example.tourismblack.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/banners")
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 查询所有banner数据
     * @return 所有banner列表
     */
    @GetMapping
    public ResponseResult<List<Banner>> getAllBanners() {
        return ResponseResult.success(bannerRepository.findAll());
    }
}
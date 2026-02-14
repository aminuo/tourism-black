package com.example.tourismblack.controller;

import com.example.tourismblack.common.ResponseResult;
import com.example.tourismblack.entity.Tag;
import com.example.tourismblack.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    /**
     * 获取所有标签
     * @return 标签列表
     */
    @GetMapping
    public ResponseResult<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return ResponseResult.success(tags);
    }
}

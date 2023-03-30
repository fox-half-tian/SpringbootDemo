package com.fox.es_canal.controller;

import com.fox.es_canal.constant.BlogConstants;
import com.fox.es_canal.entity.Result;
import com.fox.es_canal.service.BlogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-03-22 20:16
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    /**
     * 通过关键词获取数据列表
     *
     * @param keyWords 关键词
     * @param pageNo   页码
     * @return 数据列表，按照相关性从高到低进行排序
     */
    @GetMapping("/list")
    public Result list(@RequestParam("keyWords") String keyWords,
                       @RequestParam("pageNo") Integer pageNo) {
        return blogService.list(keyWords, pageNo, BlogConstants.SEARCH_PAGE_NUM);
    }
}

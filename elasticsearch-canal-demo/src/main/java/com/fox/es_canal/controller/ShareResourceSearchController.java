package com.fox.es_canal.controller;

import com.fox.es_canal.entity.Result;
import com.fox.es_canal.service.ShareResourceSearchService;
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
@RequestMapping("/search")
public class ShareResourceSearchController {

    @Resource
    private ShareResourceSearchService shareResourceSearchService;

    /**
     * 通过关键词获取数据列表
     *
     * @param keyWords 关键词
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @return 数据列表，按照相关性从高到低进行排序
     */
    @GetMapping("/list")
    public Result list(@RequestParam("keyWords") String keyWords,
                       @RequestParam("pageNo") Integer pageNo,
                       @RequestParam("pageSize") Integer pageSize) {
        return shareResourceSearchService.list(keyWords, pageNo, pageSize);
    }
}

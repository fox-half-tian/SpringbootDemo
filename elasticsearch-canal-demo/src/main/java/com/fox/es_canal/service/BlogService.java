package com.fox.es_canal.service;

import com.fox.es_canal.entity.Result;

/**
 * @author 狐狸半面添
 * @create 2023-03-22 20:18
 */
public interface BlogService {

    /**
     * 通过关键词获取数据列表
     *
     * @param keyWords 关键词
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 数据列表，按照相关性从高到低进行排序
     */
    Result list(String keyWords, int pageNo, int pageSize);
}

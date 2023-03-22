package com.fox.es.controller;

import com.fox.es.entity.Result;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 狐狸半面添
 * @create 2023-03-22 18:33
 */
@RestController
public class TestController {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/getEsInfo")
    public Result getEsInfo() throws IOException {
        MainResponse info = restHighLevelClient.info(RequestOptions.DEFAULT);
        return Result.ok(info);
    }
}

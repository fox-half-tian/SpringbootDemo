package com.fox.es_canal.controller;

import com.fox.es_canal.entity.Result;
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

    /**
     * 用于测试是否连接 es 成功
     *
     * @return 返回 es 的基本信息，等价于访问：http://192.168.65.133:9200
     * @throws IOException 异常信息
     */
    @GetMapping("/getEsInfo")
    public Result getEsInfo() throws IOException {
        MainResponse info = restHighLevelClient.info(RequestOptions.DEFAULT);
        return Result.ok(info);
    }
}

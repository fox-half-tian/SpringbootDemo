package com.fox.cqhttp.config;

import com.fox.cqhttp.interceptor.HeaderRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过 RestTemplate,我们可以发出 http 请求(支持 Restful 风格),
 * 去调用 Controller 提供的 API 接口, 就像我们使用浏览器发出 http 请求, 调用该 API 接口一样
 *
 * @author 狐狸半面添
 * @create 2023-02-17 1:43
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 配置注入 RestTemplate bean/对象
     *
     * @return 实例对象
     */
    @Bean
    public RestTemplate getRestTemplate(){
        SimpleClientHttpRequestFactory  factory = new SimpleClientHttpRequestFactory();
        //设置连接超时
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);

        // 设置默认请求头
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE));
        restTemplate.setInterceptors(interceptors);
        return new RestTemplate(factory);
    }
}

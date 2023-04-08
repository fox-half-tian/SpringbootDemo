package com.fox.cqhttp.config;

import com.fox.cqhttp.interceptor.HeaderRequestInterceptor;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
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
    public RestTemplate getRestTemplate() throws Exception{
//        SimpleClientHttpRequestFactory  factory = new SimpleClientHttpRequestFactory();
        //设置连接超时
        TrustStrategy acceptingTrustStrategy = ((x509Certificates, authType) -> true);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(25000);
        factory.setReadTimeout(25000);
//        RestTemplate restTemplate = new RestTemplate(
//                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
//        );
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.setRequestFactory(factory);
        RestTemplate restTemplate = new RestTemplate(factory);
        // 设置默认请求头
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE));
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.ACCEPT,MediaType.ALL_VALUE));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }


}

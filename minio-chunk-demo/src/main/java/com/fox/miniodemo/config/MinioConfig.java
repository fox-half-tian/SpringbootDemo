package com.fox.miniodemo.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 狐狸半面添
 * @create 2023-02-08 16:26
 */
@Configuration
public class MinioConfig {

    /**
     * 连接的ip和端口
     */
    @Value("${minio.endpoint}")
    private String endpoint;
    /**
     * 访问秘钥（也称用户id）
     */
    @Value("${minio.accessKey}")
    private String accessKey;
    /**
     * 私有秘钥（也称密码）
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
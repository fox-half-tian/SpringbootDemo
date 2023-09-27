package com.zhulang.xfxh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 狐狸半面添
 * @create 2023-09-15 0:46
 */
@Configuration
@ConfigurationProperties(prefix = "xfxh")
@Data
public class XfXhConfig {
    private String hostUrl;
    private String domain;
    private Float temperature;
    private Integer maxTokens;
    private String appId;
    private String apiKey;
    private String apiSecret;
    private Integer maxInteractCount;
    private Integer maxUserCount;
    private Integer userRecordMaxStatus;
    private Integer maxResponseTime;
}

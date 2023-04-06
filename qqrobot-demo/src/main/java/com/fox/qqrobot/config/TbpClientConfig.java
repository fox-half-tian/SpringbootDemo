package com.fox.qqrobot.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tbp.v20190627.TbpClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 狐狸半面添
 * @create 2023-04-05 22:21
 */
@Configuration
@Getter
public class TbpClientConfig {
    /**
     * 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
     * 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
     */
    @Value("${tbp.secretId}")
    private String secretId;

    /**
     * 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
     * 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
     */
    @Value("${tbp.secretKey}")
    private String secretKey;

    /**
     * 就近地域接入域名为 tbp.tencentcloudapi.com
     * 推荐使用就近地域接入域名。根据调用接口时客户端所在位置，会自动解析到最近的某个具体地域的服务器。
     */
    @Value("${tbp.endpoint}")
    private String endpoint;

    /**
     * 机器人标识，用于定义抽象机器人。
     */
    @Value("${tbp.botId}")
    private String botId;

    @Value("${tbp.botEnv}")
    private String botEnv;

    @Bean
    public TbpClient tbpClient(){
        Credential cred = new Credential(secretId, secretKey);

        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(endpoint);

        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        // 实例化要请求产品的client对象,clientProfile是可选的
        return new TbpClient(cred, "", clientProfile);
    }
}

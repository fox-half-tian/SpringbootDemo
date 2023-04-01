package com.fox.sms.config;

import com.fox.sms.util.AliSmsTemplateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 狐狸半面添
 * @create 2023-04-01 15:46
 */
@Configuration
public class SmsConfig {

    /**
     * 配置阿里短信发送工具类
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.ali")
    public AliSmsTemplateUtils aliSmsTemplateUtils(){
        return new AliSmsTemplateUtils();
    }
}

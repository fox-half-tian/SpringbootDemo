package com.fox.sms.util;

import cn.hutool.json.JSONUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * 阿里云发送短信工具类，需要注入容器
 *
 * @author 狐狸半面添
 * @create 2023-04-01 15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class AliSmsTemplateUtils {
    /**
     * 子用户的访问键
     */
    private String accessKeyId;
    /**
     * 子用户的访问密钥
     */
    private String accessKeySecret;
    /**
     * 签名的内容
     */
    private String signName;
    /**
     * 登录短信模板的id
     */
    private String loginTemplateCode;

    /**
     * 发送登录验证码
     *
     * @param phone 手机号
     */
    public void sendLoginCode(String phone, String code){
        // 配置凭据身份验证信息，包括 accessKeyId 与 accessKeySecret
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());

        // 客户端配置
        AsyncClient client = AsyncClient.builder()
                // 地域id，这里是乌兰察布
                .region("cn-wulanchabu")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // 访问的域名，不要修改
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                                // 设置超时时长
                                .setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();

        // 请求参数设置
        HashMap<String, String> contentParam = new HashMap<>();
        contentParam.put("code",code);
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(signName)
                .templateCode(loginTemplateCode)
                .phoneNumbers(phone)
                .templateParam(JSONUtil.toJsonStr(contentParam))
                .build();

        // 异步获取API请求的返回值
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);

        // 异步处理返回值
        response.thenAccept(resp -> {
            System.out.println(new Gson().toJson(resp));
        }).exceptionally(throwable -> {
            // 处理异常
            log.error("发送登录短信验证码发生异常：{}",throwable.getMessage());
            return null;
        });

        // 关闭客户端
        client.close();
    }
}

package com.fox.sms.service;

import com.fox.sms.entity.Result;

/**
 * @author 狐狸半面添
 * @create 2023-04-01 16:26
 */
public interface SmsAliService {
    /**
     * 发送用于登录与注册的验证码
     *
     * @param phone 手机号
     * @return 发送情况
     */
    Result sendLoginCode(String phone);
}

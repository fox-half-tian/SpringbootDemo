package com.fox.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.fox.sms.entity.Result;
import com.fox.sms.service.SmsAliService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-04-01 15:45
 */
@RestController
@RequestMapping("/sms-ali")
public class SmsAliController {
    @Resource
    private SmsAliService smsAliService;

    /**
     * 发送用于登录的验证码
     *
     * @param object 手机号的json对象
     * @return 发送情况
     */
    @PostMapping("/sendLoginCode")
    public Result sendLoginCode(@RequestBody JSONObject object){
        String phone = object.getString("phone");
        return smsAliService.sendLoginCode(phone);
    }
}

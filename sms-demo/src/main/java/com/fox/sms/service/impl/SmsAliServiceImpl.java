package com.fox.sms.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;
import com.fox.sms.constant.RedisConstants;
import com.fox.sms.entity.Result;
import com.fox.sms.service.SmsAliService;
import com.fox.sms.util.AliSmsTemplateUtils;
import com.fox.sms.util.RedisCacheUtils;
import com.fox.sms.util.RegexUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-04-01 16:25
 */
@Service
public class SmsAliServiceImpl implements SmsAliService {
    @Resource
    private AliSmsTemplateUtils aliSmsTemplateUtils;
    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Override
    public Result sendLoginCode(String phone) {
        // 1.校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST, "手机号格式错误");
        }
        String key = RedisConstants.LOGIN_USER_CODE_KEY + phone;

        // 2.查看缓存中是否已经存在，得到剩余TTL
        Long expire = redisCacheUtils.getExpire(key);

        // 3.存在并且剩余时长大于4分钟则不可再次发送验证码
        if(expire > RedisConstants.LOGIN_USER_CODE_AGAIN_TTL){
            return Result.error(701,"发送失败，验证码仍在有效期内");
        }

        // 4.验证码不存在或者剩余时长小于四分钟，则可以继续发送验证码 --> 先生成六位随机数
         String code = RandomUtil.randomNumbers(6);

        // 关于恶意并发的问题，在短信云平台已经自动做了处理，这里就无需处理

        // 5.先存储到 redis，附带该手机号已经验证的次数，初始化为0，进行校验时分割得到 code 和 count。
        redisCacheUtils.setCacheObject(key,code+",0",RedisConstants.LOGIN_USER_CODE_TTL);

        // 6.发送短信验证到手机
        boolean success = aliSmsTemplateUtils.sendLoginCode(phone, code);

        // 7.返回
        if (success){
            return Result.ok();
        }else{
            // 移除redis中的缓存记录
            redisCacheUtils.deleteObject(RedisConstants.LOGIN_USER_CODE_KEY + phone);
            return Result.error();
        }
    }
}

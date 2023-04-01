package com.fox.sms.constant;

/**
 * 定义 redis 缓存的常量前缀
 *
 * @author 狐狸半面添
 * @create 2023-04-01 16:37
 */
public class RedisConstants {
    /**
     * 用户登录&注册手机验证码
     * 有效期：5分钟
     * 剩余时长大于 4分钟 则无法再次发送
     */
    public static final String LOGIN_USER_CODE_KEY = "login:user:code:";
    public static final Long LOGIN_USER_CODE_TTL = 60 * 5L;
    public static final Long LOGIN_USER_CODE_AGAIN_TTL = 60 * 4L;
}

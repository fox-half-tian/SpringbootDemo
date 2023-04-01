package com.fox.sms.util;

import cn.hutool.core.util.StrUtil;

/**
 * 校验格式工具类
 *
 * @author 狐狸半面添
 * @create 2023-04-01 16:32
 */
public class RegexUtils {


    /**
     * 正则表达式模板
     *
     * @author 狐狸半面添
     * @create 2023-01-16 22:39
     */
    public static class RegexPatterns {
        /**
         * 手机号正则
         */
        public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    }

    /**
     * 是否是无效手机格式
     *
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneInvalid(String phone) {
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }

    /**
     * 校验是否不符合正则格式
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return true:符合  false:不符合
     */
    private static boolean mismatch(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}

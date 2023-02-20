package com.fox.miniodemo.util;

import cn.hutool.core.util.StrUtil;

/**
 * 校验格式工具类
 *
 * @author 狐狸半面添
 * @create 2023-01-16 22:17
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
         * md5十六进制正则：32个字符
         */
        public static final String MD5_HEX_REGEX = "^[0-9abcdef]{32}$";

        /**
         * 文件名称正则：最多255个字符
         */
        public static final String FILE_NAME_REGEX = "^.{1,255}$";

        /**
         * 文件标签正则：最多32个字符
         */
        public static final String FILE_TAG_REGEX = "^.{1,32}$";

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


    /**
     * 是否是无效十六进制md5格式
     *
     * @param md5Hex md5的十六进制
     * @return true:符合，false：不符合
     */
    public static boolean isMd5HexInvalid(String md5Hex){
        return mismatch(md5Hex, RegexPatterns.MD5_HEX_REGEX);
    }

}
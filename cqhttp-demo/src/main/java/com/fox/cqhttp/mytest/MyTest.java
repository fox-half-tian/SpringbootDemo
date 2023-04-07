package com.fox.cqhttp.mytest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 狐狸半面添
 * @create 2023-04-06 23:14
 */
public class MyTest {
    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis(); // 获取当前时间戳
        System.out.println(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义日期格式
        String formattedDate = sdf.format(new Date()); // 将时间戳转为指定格式字符串
        System.out.println(formattedDate); // 输出格式化后的字符串
    }
}

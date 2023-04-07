package com.fox.cqhttp.mytest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 狐狸半面添
 * @create 2023-04-06 23:14
 */
public class MyTest {
    public static void main(String[] args) {
        String str = "我”爱“你";
        String[] split = str.split("[\"“]");
        for (String s : split) {
            System.out.println(s);
        }
    }
}

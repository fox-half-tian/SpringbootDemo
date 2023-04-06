package com.fox.cqhttp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-04-06 12:39
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class CqHttpDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CqHttpDemoApplication.class,args);
    }
}

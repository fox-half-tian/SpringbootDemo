package com.fox.qqrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

/**
 * @author 狐狸半面添
 * @create 2023-04-05 21:23
 */
@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class QqRobotDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(QqRobotDemoApplication.class, args);
    }
}

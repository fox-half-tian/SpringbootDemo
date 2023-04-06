package com.fox.qqrobot;

import com.fox.qqrobot.service.QqChatService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-04-05 22:48
 */
@SpringBootTest
public class SpringbootTest {
    @Resource
    private QqChatService qqChatService;
    @Test
    void test01(){
//        String response = qqChatService.getResponse("谢添喜欢谁");
        String response = qqChatService.getResponse("湖南天气如何");
        System.out.println(response);
    }
}

package com.fox.cqhttp.handle;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 3:11
 */
@Slf4j
public class DogHandler implements ReceiveTypeHandler {
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
    ;

    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://v.api.aa1.cn/api/tiangou/index.php", String.class);
        String str = Objects.requireNonNull(forEntity.getBody());
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text(str.substring(4,str.length()-4)), false);
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://v.api.aa1.cn/api/tiangou/index.php", String.class);
        String str = Objects.requireNonNull(forEntity.getBody());
        bot.sendPrivateMsg(event.getUserId(), Msg.builder().text(str.substring(4,str.length()-4)), false);
    }
}

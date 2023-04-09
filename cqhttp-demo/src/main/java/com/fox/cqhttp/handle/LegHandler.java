package com.fox.cqhttp.handle;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 3:11
 */
@Slf4j
public class LegHandler implements ReceiveTypeHandler {
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> forEntity = restTemplate.exchange("http://yichen.api.z7zz.cn/api/tu.php", HttpMethod.GET, entity, String.class);
//        String url = "https:" + Objects.requireNonNull(forEntity.getBody()).substring(5).trim();
//        String url = Objects.requireNonNull(forEntity.getBody()).trim();
        String url = Objects.requireNonNull(forEntity.getBody()).trim();
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text("这是你要的美腿").face(24).image(url), false);
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> forEntity = restTemplate.exchange("http://yichen.api.z7zz.cn/api/tu.php", HttpMethod.GET, entity, String.class);
        String url = Objects.requireNonNull(forEntity.getBody()).trim();
        bot.sendPrivateMsg(event.getUserId(), Msg.builder().text("这是你要的美腿").face(24).image(url), false);
    }
}

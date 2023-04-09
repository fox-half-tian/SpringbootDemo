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

/**
 * @author 狐狸半面添
 * @create 2023-04-07 3:11
 */
@Slf4j
public class BeautyVlogHandler implements ReceiveTypeHandler{
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);;

    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://v.api.aa1.cn/api/api-dy-girl/index.php?aa1=json",String.class);
        JSONObject queryInfo = JSONUtil.parseObj(forEntity.getBody());
        if ("200".equals(queryInfo.getStr("result"))) {
            String url = queryInfo.getStr("mp4");
            System.out.println(url);
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().video(url,"https://i.75ll.com/up/64/a9/01/1b864a1ee14a8ffd2735f8087d01a964.jpg",true), false);
        }else{
            log.error("调用小姐姐视频接口错误：{}",queryInfo.getStr("error"));
            String info = "很抱歉，我好像出了点小问题，暂时无法与你分享小姐姐视频";
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text(info).face(66), false);
        }
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://v.api.aa1.cn/api/api-dy-girl/index.php?aa1=json",String.class);
        JSONObject queryInfo = JSONUtil.parseObj(forEntity.getBody());
        if ("200".equals(queryInfo.getStr("result"))) {
            String url = queryInfo.getStr("mp4");
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().video(url,"https://i.75ll.com/up/64/a9/01/1b864a1ee14a8ffd2735f8087d01a964.jpg",false), false);

        }else{
            log.error("调用小姐姐视频接口错误：{}",queryInfo.getStr("error"));
            String info = "很抱歉，我好像出了点小问题，暂时无法与你分享小姐姐视频";
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().text(info).face(66), false);
        }
    }
}

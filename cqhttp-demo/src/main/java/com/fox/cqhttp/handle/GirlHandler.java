package com.fox.cqhttp.handle;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Random;

/**
 * @author 狐狸半面添
 * @create 2023-04-08 22:16
 */
@Slf4j
public class GirlHandler implements ReceiveTypeHandler {
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
    private int cacheNum = 0;

    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.mxnzp.com/api/image/girl/list/random?app_id=ibfdtnlmnysqlqne&app_secret=emlDZll2ZTZHTzlKc2JHcVFIVHdFUT09", String.class);
        JSONObject queryInfo = JSONUtil.parseObj(forEntity.getBody());
        if ("1".equals(queryInfo.getStr("code"))) {
            JSONArray jsonArray = JSONUtil.parseArray(queryInfo.getStr("data"));
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text("这是你要的清纯女孩").face(176).image(jsonArray.getJSONObject(RandomUtil.randomInt(0, jsonArray.size() - 1)).getStr("imageUrl")), false);
        } else {
            log.error("调用清纯女孩接口错误：{}", queryInfo.getStr("msg"));
            String info = "很抱歉，我好像出了点小问题，暂时无法与你分析清纯女孩";
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text(info).face(66), false);
        }
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.mxnzp.com/api/image/girl/list/random?app_id=ibfdtnlmnysqlqne&app_secret=emlDZll2ZTZHTzlKc2JHcVFIVHdFUT09", String.class);
        JSONObject queryInfo = JSONUtil.parseObj(forEntity.getBody());
        if ("1".equals(queryInfo.getStr("code"))) {
            JSONArray jsonArray = JSONUtil.parseArray(queryInfo.getStr("data"));
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().text("这是你要的清纯女孩").face(176).image(jsonArray.getJSONObject(RandomUtil.randomInt(0, jsonArray.size() - 1)).getStr("imageUrl")), false);
        } else {
            log.error("调用清纯女孩接口错误：{}", queryInfo.getStr("msg"));
            String info = "很抱歉，我好像出了点小问题，暂时无法与你分析清纯女孩";
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().text(info).face(66), false);
        }
    }
}

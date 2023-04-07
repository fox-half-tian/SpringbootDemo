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

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.fox.cqhttp.constant.ImageConstants.LESSON_TABLE_URL;


/**
 * @author 狐狸半面添
 * @create 2023-04-07 1:12
 */
@Slf4j
public class MyInfoHandler implements ReceiveTypeHandler {
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);;

    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        OnebotEvent.GroupMessageEvent.Sender sender = event.getSender();
        // 获取 等级 与 连续在线天数
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.youwk.cn/api/qq/q_zl?key=yvyRGvZr3QUFEIb3gGkKxf0CEev3AI&qq=" + sender.getUserId(),String.class);
        JSONObject jsonObject = JSONUtil.parseObj(forEntity.getBody());
        JSONObject queryInfo =  JSONUtil.parseObj(jsonObject.getStr("data"));
        if ("200".equals(jsonObject.getStr("code"))) {
            String info = "\n丘丘号：" + sender.getUserId()
                    + "\n昵称：" + sender.getNickname()
                    + "\nQQ等级：" + queryInfo.getStr("grade")
                    + "\n连续在线天数：" + queryInfo.getStr("tianshu");
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text("你的信息如下").face(21).text(info), false);
        }else{
            log.error("调用QQ信息接口错误：{}",jsonObject.getStr("msg"));
            String info = "\n丘丘号：" + sender.getUserId()
                    + "\n昵称：" + sender.getNickname()
                    +"\n很抱歉，我好像出了点小问题，其它信息无法获取";
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text("你的信息如下").face(21).text(info), false);
        }
    }
    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        OnebotEvent.PrivateMessageEvent.Sender sender = event.getSender();
        // 获取 等级 与 连续在线天数
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.youwk.cn/api/qq/q_zl?key=yvyRGvZr3QUFEIb3gGkKxf0CEev3AI&qq=" + sender.getUserId(),String.class);
        JSONObject jsonObject = JSONUtil.parseObj(forEntity.getBody());
        JSONObject queryInfo =  JSONUtil.parseObj(jsonObject.getStr("data"));
        if ("200".equals(jsonObject.getStr("code"))) {
            String info = "\n丘丘号：" + sender.getUserId()
                    + "\n昵称：" + sender.getNickname()
                    + "\nQQ等级：" + queryInfo.getStr("grade")
                    + "\n连续在线天数：" + queryInfo.getStr("tianshu");
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().text("你的信息如下").face(21).text(info), false);
        }else{
            log.error("调用QQ信息接口错误：{}",jsonObject.getStr("msg"));
            String info = "\n丘丘号：" + sender.getUserId()
                    + "\n昵称：" + sender.getNickname()
                    +"\n很抱歉，我好像出了点小问题，其它信息无法获取";
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().text("你的信息如下").face(21).text(info), false);
        }
    }
}

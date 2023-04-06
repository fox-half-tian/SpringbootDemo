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
public class LangTalkHandler implements ReceiveTypeHandler{
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);;

    @Override
    public void handle(Bot bot, OnebotEvent.GroupMessageEvent event) {
        OnebotEvent.GroupMessageEvent.Sender sender = event.getSender();
        // 获取社会语录
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.youwk.cn/api/shehui?key=yvyRGvZr3QUFEIb3gGkKxf0CEev3AI",String.class);
        JSONObject queryInfo = JSONUtil.parseObj(forEntity.getBody());
        if ("0".equals(queryInfo.getStr("code"))) {
            String data = queryInfo.getStr("data");
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text(data.substring(0,data.length()-2)).face(66), false);
        }else{
            log.error("调用社会语录接口错误：{}",queryInfo.getStr("msg"));
            String info = "\n很抱歉，我好像出了点小问题，暂时无法与你分析有关于我";
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text(info).face(66), false);
        }
    }
}

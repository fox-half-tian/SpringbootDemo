package com.fox.cqhttp.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static com.fox.cqhttp.constant.ImageConstants.LESSON_TABLE_URL;
import static com.fox.cqhttp.constant.ReplyConstants.ANSWER_BLANK_QUESTION;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:48
 */
public class LessonTableHandler implements ReceiveTypeHandler{
    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).image(LESSON_TABLE_URL), false);
    }
    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        bot.sendPrivateMsg(event.getUserId(), Msg.builder().image(LESSON_TABLE_URL), false);
    }
}

package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;

import java.util.Map;

import static com.fox.cqhttp.constant.ImageConstants.LESSON_TABLE_URL;
import static com.fox.cqhttp.constant.ReplyConstants.ANSWER_BLANK_QUESTION;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:48
 */
public class LessonTableHandler implements ReceiveTypeHandler{
    @Override
    public void handle(Bot bot, OnebotEvent.GroupMessageEvent event) {
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).image(LESSON_TABLE_URL), false);
    }
}

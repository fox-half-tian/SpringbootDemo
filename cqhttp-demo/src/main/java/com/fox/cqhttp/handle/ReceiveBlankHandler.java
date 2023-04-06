package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;

import static com.fox.cqhttp.constant.ReplyConstants.ANSWER_BLANK_QUESTION;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:57
 */
public class ReceiveBlankHandler implements ReceiveTypeHandler {
    @Override
    public void handle(Bot bot, OnebotEvent.GroupMessageEvent event) {
        bot.sendGroupMsg(event.getGroupId(), Msg.builder()
                .at(event.getUserId())
                .face(9)
                .text("你艾特我，却又不对我说什么，是为什么呢？是不喜欢小浪吗？")
                .face(67), false);
    }
}

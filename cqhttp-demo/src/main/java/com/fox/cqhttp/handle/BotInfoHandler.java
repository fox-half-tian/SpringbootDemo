package com.fox.cqhttp.handle;

import com.fox.cqhttp.util.TimeFormatUtils;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author 狐狸半面添
 * @create 2023-04-07 1:12
 */
public class BotInfoHandler implements ReceiveTypeHandler {
    private final String UP_TIME = TimeFormatUtils.format(LocalDateTime.now());

    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId())
                .text("\n")
                .face(12)
                .text("我是你的机器人助理小浪")
                .text("\n开发者：狐狸半面添")
                .text("\n最近上线时间：" + UP_TIME)
                .text("\n当前版本：0.0.1-beta"), false);
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        bot.sendPrivateMsg(event.getUserId(), Msg.builder().face(12)
                .text("我是你的机器人助理小浪")
                .text("\n开发者：狐狸半面添")
                .text("\n最近上线时间：" + UP_TIME)
                .text("\n当前版本：0.0.2-beta"), false);
    }

}

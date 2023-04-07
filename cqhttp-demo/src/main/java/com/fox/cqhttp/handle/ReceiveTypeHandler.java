package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import onebot.OnebotEvent;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:46
 */
public interface ReceiveTypeHandler {
    /**
     * 处理群聊
     *
     * @param bot 机器人
     * @param event 事件
     */
    void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event);

    /**
     * 处理私聊
     *
     * @param bot 机器人
     * @param event 事件
     */
    void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event);
}

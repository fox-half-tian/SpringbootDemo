package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import onebot.OnebotEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:46
 */
public interface ReceiveTypeHandler {
    /**
     * 处理
     *
     * @param bot 机器人
     * @param event 事件
     */
    void handle(Bot bot, OnebotEvent.GroupMessageEvent event);
}

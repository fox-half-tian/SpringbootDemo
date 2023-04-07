package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;


/**
 * @author 狐狸半面添
 * @create 2023-04-07 1:12
 */
public class MyIconHandler implements ReceiveTypeHandler {
    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()), false);
        bot.sendGroupMsg(event.getGroupId(), Msg.builder().image("https://q2.qlogo.cn/headimg_dl?dst_uin=" + event.getUserId() + "@qq.com&spec=640"), false);
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        bot.sendGroupMsg(event.getUserId(), Msg.builder().image("https://q2.qlogo.cn/headimg_dl?dst_uin=" + event.getUserId() + "@qq.com&spec=640"), false);
    }


}

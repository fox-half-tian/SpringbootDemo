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
                .text("\n当前版本：1.0.0")
                .text("\n⭐⭐⭐⭐菜单列表⭐⭐⭐⭐")
                .text("\n*  我的课表  *  我的头像  *")
                .text("\n*  蓄蓄语录  *  浪浪语录  *")
                .text("\n*  随机美腿  *  清纯女孩  *")
                .text("\n*  我的信息  *  你的信息  *")
                .text("\n*  全员禁言  *  全员解禁  *")
                .text("\n*  解禁成员  *  禁言　　  *")
                .text("\n*  智障回答  *  问浪回答  *")
                .text("\n*  舔狗日记  *  次元女孩  *")
                , false);
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {
        bot.sendPrivateMsg(event.getUserId(), Msg.builder().face(12)
                        .face(12)
                        .text("我是你的机器人助理小浪")
                        .text("\n开发者：狐狸半面添")
                        .text("\n最近上线时间：" + UP_TIME)
                        .text("\n当前版本：1.0.0")
                        .text("\n⭐⭐⭐⭐菜单列表⭐⭐⭐⭐")
                        .text("\n*  我的课表  *  我的头像  *")
                        .text("\n*  蓄蓄语录  *  浪浪语录  *")
                        .text("\n*  随机美腿  *  清纯女孩  *")
                        .text("\n*  我的信息  *  你的信息  *")
                        .text("\n*  全员禁言  *  全员解禁  *")
                        .text("\n*  解禁成员  *  禁言　　  *")
                        .text("\n*  智障回答  *  问浪回答  *")
                        .text("\n*  舔狗日记  *  次元女孩  *")
                , false);
    }

}

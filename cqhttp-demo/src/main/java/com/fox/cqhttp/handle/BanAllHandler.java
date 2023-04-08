package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;

import static com.fox.cqhttp.constant.ReplyConstants.GROUP_BAN_ALL_TIP;


/**
 * @author 狐狸半面添
 * @create 2023-04-07 1:12
 */
public class BanAllHandler implements ReceiveTypeHandler {
    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        if ("member".equals(event.getSender().getRole())){
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text("你当前没有权限禁言噢").face(20), false);
            return;
        }
        bot.setGroupWholeBan(event.getGroupId(),true);
        bot.sendGroupMsg(event.getGroupId(), GROUP_BAN_ALL_TIP, false);
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {

    }


}

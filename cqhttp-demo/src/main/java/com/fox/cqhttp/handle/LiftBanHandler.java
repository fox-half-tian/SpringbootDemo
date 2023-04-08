package com.fox.cqhttp.handle;

import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotBase;
import onebot.OnebotEvent;

import static com.fox.cqhttp.constant.ReplyConstants.GROUP_LIFT_BAN_ALL_TIP;


/**
 * @author 狐狸半面添
 * @create 2023-04-07 1:12
 */
public class LiftBanHandler implements ReceiveTypeHandler {
    @Override
    public void handleGroup(Bot bot, OnebotEvent.GroupMessageEvent event) {
        if ("member".equals(event.getSender().getRole())){
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(event.getUserId()).text("你当前没有权限解除禁言噢").face(20), false);
            return;
        }
        // 获取所有非自己的at名单
        for (OnebotBase.Message message : event.getMessageList()) {
            if ("at".equals(message.getType())
                    && !(bot.getSelfId() + "").equals(message.getDataOrThrow("qq"))) {
                bot.setGroupBan(event.getGroupId(), Long.parseLong(message.getDataOrThrow("qq")), 0);
            }
        }
    }

    @Override
    public void handlePrivate(Bot bot, OnebotEvent.PrivateMessageEvent event) {

    }


}

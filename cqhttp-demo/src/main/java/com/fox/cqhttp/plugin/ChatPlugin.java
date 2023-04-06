package com.fox.cqhttp.plugin;

import cn.hutool.core.util.StrUtil;
import com.fox.cqhttp.service.TxAnswerService;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.bot.BotPlugin;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotBase;
import onebot.OnebotEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.fox.cqhttp.constant.ReplyConstants.ANSWER_BLANK_QUESTION;
import static com.fox.cqhttp.constant.ReplyConstants.GROUP_BAN_TIP;

/**
 * @author 狐狸半面添
 * @create 2023-04-06 12:39
 */
@Component
@Slf4j
public class ChatPlugin extends BotPlugin {
    @Resource
    private TxAnswerService txAnswerService;

    /**
     * 收到群消息时调用此方法
     *
     * @param bot 机器人
     * @param event 事件
     * @return 是否继续下一个插件
     */
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull OnebotEvent.GroupMessageEvent event) {
        // 群号
        long groupId = event.getGroupId();
        // 接收内容
        String text = event.getRawMessage();
        // 获取消息的所有类型
        List<OnebotBase.Message> messageList = event.getMessageList();
        boolean notHandle = true;
        StringBuilder str = new StringBuilder();
        for (OnebotBase.Message message : messageList) {
            // 满足是在 at 并且是在 at 机器人
            if ("at".equals(message.getType())
                    && (bot.getSelfId() + "").equals(message.getDataOrThrow("qq"))) {
                notHandle = false;
                str = new StringBuilder();
                for (OnebotBase.Message againMsg : messageList) {
                    if ("text".equals(againMsg.getType())) {
                        str.append(againMsg.getDataOrThrow("text")).append(" ");
                    }
                }
            }
        }

        // 如果不是在 at 自己，就退出
        if (notHandle) {
            return MESSAGE_BLOCK;
        }

        // 如果是空白字符
        if (StrUtil.isBlank(str.toString())) {
            bot.sendGroupMsg(groupId, ANSWER_BLANK_QUESTION, false);
            return MESSAGE_BLOCK;
        }
        // 回复，内容由 腾讯云tbp 处理
        bot.sendGroupMsg(groupId, Msg.builder().at(event.getUserId()).text(txAnswerService.getResponse(str.toString())), false);
        // 当存在多个plugin时，不执行下一个plugin
        return MESSAGE_BLOCK;
    }

    /**
     * 群禁言时调用此方法
     *
     * @param bot 机器人
     * @param event 事件
     * @return 是否继续下一个插件
     */
    @Override
    public int onGroupBanNotice(@NotNull Bot bot, @NotNull OnebotEvent.GroupBanNoticeEvent event) {
        bot.sendGroupMsg(event.getGroupId(),GROUP_BAN_TIP,false);
        return MESSAGE_BLOCK;
    }

}

package com.fox.cqhttp.plugin;

import cn.hutool.core.util.StrUtil;
import com.fox.cqhttp.handle.ReceiveTypeHandler;
import com.fox.cqhttp.service.GptService;
import com.fox.cqhttp.service.TxAnswerService;
import com.fox.cqhttp.util.ReceiveTypeUtils;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.bot.BotPlugin;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotBase;
import onebot.OnebotEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.fox.cqhttp.constant.ReplyConstants.*;

/**
 * @author 狐狸半面添
 * @create 2023-04-06 12:39
 */
@Component
@Slf4j
public class ChatPlugin extends BotPlugin {
    @Resource
    private TxAnswerService txAnswerService;
    @Resource
    private GptService gptService;

    /**
     * 收到群消息时调用此方法
     *
     * @param bot   机器人
     * @param event 事件
     * @return 是否继续下一个插件
     */
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull OnebotEvent.GroupMessageEvent event) {
        // 群号
        long groupId = event.getGroupId();
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
                        String text = againMsg.getDataOrThrow("text");
//                        if (text.contains())
                        str.append(againMsg.getDataOrThrow(text)).append(" ");
                    }
                }
                break;
            }
        }

        // 如果不是在 at 自己，就退出
        if (notHandle) {
            return MESSAGE_BLOCK;
        }

        // 对接收的类型进行处理
        ReceiveTypeHandler receiveTypeHandler = ReceiveTypeUtils.get(str.toString());
        if (receiveTypeHandler != null) {
            receiveTypeHandler.handleGroup(bot, event);
            return MESSAGE_BLOCK;
        }

        if (str.length() > 3 && "问浪".equals(str.substring(0, 4).trim())) {
            // 判断问题是否为有效字符
            String question = str.substring(4, str.length());
            if (StringUtils.hasText(question)) {
                String response = gptService.getResponse(question);
                if (response.length() > 6 && "很抱歉地告诉您".equals(response.substring(0, 7))) {
                    bot.sendPrivateMsg(event.getUserId(), Msg.builder().at(event.getUserId()).text("可以再问一次小浪吗，我刚刚走神了").face(9), false);
                }
                bot.sendGroupMsg(groupId, Msg.builder().at(event.getUserId()).text(response), false);
            } else {
                bot.sendGroupMsg(groupId, Msg.builder().at(event.getUserId()).text("你想问小浪什么呢").face(20), false);
            }
        }
//        else if (str.length() > 4 && " 授予头衔".equals(str.substring(0, 5))) {
//            String role = event.getSender().getRole();
//            System.out.println(role);
//            // 授予头衔
//            String[] split = str.toString().trim().split("[\"“”]");
//            if (split.length != 3) {
//                // 回复，内容由 腾讯云tbp 处理
//                bot.sendGroupMsg(groupId, Msg.builder().at(event.getUserId()).text(txAnswerService.getResponse(str.toString())), false);
//            } else {
//                String rank = split[1];
//                for (OnebotBase.Message message : messageList) {
//                    if ("at".equals(message.getType())) {
//                        long qq = Long.parseLong(message.getDataOrThrow("qq"));
//                        if (bot.getSelfId() != qq) {
//                            bot.setGroupSpecialTitle(event.getGroupId(), qq, split[1], -1L);
//                            bot.sendGroupMsg(event.getGroupId(), Msg.builder().at(qq).text("你被赋予了头衔—“" + split[1] + "”").face(180), false);
//                        }
//                    }
//                }
//            }
//
//        }
        else {
            // 回复，内容由 腾讯云tbp 处理
            bot.sendGroupMsg(groupId, Msg.builder().at(event.getUserId()).text(txAnswerService.getResponse(str.toString())), false);
            // 当存在多个plugin时，不执行下一个plugin
        }
        return MESSAGE_BLOCK;
    }

    /**
     * 群禁言时调用此方法
     *
     * @param bot   机器人
     * @param event 事件
     * @return 是否继续下一个插件
     */
    @Override
    public int onGroupBanNotice(@NotNull Bot bot, @NotNull OnebotEvent.GroupBanNoticeEvent event) {
        // 有禁言消息的处理
        if ("ban".equals(event.getSubType())) {
            if (event.getUserId() == 0) {
                // 对全体禁言的处理
                bot.sendGroupMsg(event.getGroupId(), GROUP_BAN_ALL_TIP, false);
            } else {
                // 对个人禁言的处理
            }
        } else {
            // 对 解禁消息的处理
            if (event.getUserId() == 0) {
                // 对全体解禁做处理
                bot.sendGroupMsg(event.getGroupId(), GROUP_LIFT_BAN_ALL_TIP, false);
            } else {
                // 对个人解禁的处理
            }
        }
        return MESSAGE_BLOCK;
    }


    /**
     * 群人数增加时调用此方法
     *
     * @param bot   机器人
     * @param event 事件
     * @return 是否继续下一个插件
     */
    @Override
    public int onGroupIncreaseNotice(@NotNull Bot bot, @NotNull OnebotEvent.GroupIncreaseNoticeEvent event) {

        bot.sendGroupMsg(event.getGroupId(), Msg.builder()
                .at(event.getUserId())
                .text("欢迎你加入这颗小小的逐浪星球，我是小浪，有事可以艾特我哦")
                .face(18).image("https://sangxin-tian.oss-cn-nanjing.aliyuncs.com/qq-robot/future.jpg"), false);
        return MESSAGE_BLOCK;
    }

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull OnebotEvent.PrivateMessageEvent event) {
        List<OnebotBase.Message> messageList = event.getMessageList();
        String str = "";
        for (OnebotBase.Message message : messageList) {
            if ("text".equals(message.getType())) {
                str = str + message.getDataOrThrow("text") + " ";
            }
        }
        // 对接收的类型进行处理
        ReceiveTypeHandler receiveTypeHandler = ReceiveTypeUtils.get(str);
        if (receiveTypeHandler != null) {
            receiveTypeHandler.handlePrivate(bot, event);
            return MESSAGE_BLOCK;
        }

        if (str.length() > 2 && "问浪".equals(str.substring(0, 2).trim())) {
            // 判断问题是否为有效字符
            String question = str.substring(2);
            if (StringUtils.hasText(question)) {
                String response = gptService.getResponse(question);
                if (response.length() > 6 && "很抱歉地告诉您".equals(response.substring(0, 7))) {
                    bot.sendPrivateMsg(event.getUserId(), Msg.builder().text("可以再问一次小浪吗，我刚刚走神了").face(9), false);
                } else {
                    bot.sendPrivateMsg(event.getUserId(), Msg.builder().text(response), false);
                }
            } else {
                bot.sendPrivateMsg(event.getUserId(), Msg.builder().text("你想和小浪说些什么呢").face(20), false);
            }
        } else {
            // 回复，内容由 腾讯云tbp 处理
            bot.sendPrivateMsg(event.getUserId(), Msg.builder().text(txAnswerService.getResponse(str)), false);
            // 当存在多个plugin时，不执行下一个plugin
        }
        return MESSAGE_BLOCK;

    }
}

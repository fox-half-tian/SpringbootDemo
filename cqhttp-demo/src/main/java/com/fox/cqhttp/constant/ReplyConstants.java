package com.fox.cqhttp.constant;

import net.lz1998.pbbot.utils.Msg;

/**
 * @author 狐狸半面添
 * @create 2023-04-06 23:17
 */
public class ReplyConstants {
    public static final Msg ANSWER_BLANK_QUESTION = Msg.builder()
            // 哭脸
            .face(9)
            .text("你艾特我，却又不对我说什么，是为什么呢？是不喜欢小浪吗？")
            .face(67);

    public static final Msg GROUP_BAN_TIP =  Msg.builder()
            .face(23)
            .text("小浪提醒你，现在是禁言时刻哦");
}

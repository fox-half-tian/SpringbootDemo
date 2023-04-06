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

    public static final Msg GROUP_BAN_ALL_TIP =  Msg.builder()
            .face(9)
            .text("哼哼，你们是不是做了对不起小浪的事情，让小浪好伤心，你们现在都不要讲话啦")
            .face(35);

    public static final Msg GROUP_LIFT_BAN_ALL_TIP =  Msg.builder()
            .text("好吧")
            .face(172)
            .text("，小浪原谅你们了")
            .face(21);
}

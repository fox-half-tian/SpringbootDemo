//package com.fox.qqrobot.listener;
//
//import com.fox.qqrobot.service.QqChatService;
//import love.forte.simboot.annotation.Listener;
//import love.forte.simbot.ID;
//import love.forte.simbot.event.FriendAddRequestEvent;
//import love.forte.simbot.event.FriendMessageEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.annotation.Resource;
//
///**
// * 监听类
// * Author:木芒果
// */
////@Component
//public class MessageListener01 {
//    private static final Logger log = LoggerFactory.getLogger(MessageListener01.class);
//    @Resource
//    private QqChatService qqChatService;
//
//    /**
//     * 监听好友添加请求
//     */
//    @Listener
//    public void onFriendAddRequest(FriendAddRequestEvent friendAddRequestEvent) {
//        String message = friendAddRequestEvent.getMessage();
//        ID id = friendAddRequestEvent.getFriend().getId();
//        log.info(id + ",添加我为好友");
//        //触发关键词即自动同意，否则拒绝
//        if (message.equals("木芒果")) {
//            log.info("同意添加" + id + "为好友");
//            friendAddRequestEvent.acceptAsync();
//        } else {
//            log.info("拒绝添加" + id + "为好友");
//            friendAddRequestEvent.rejectAsync();
//        }
//    }
//
//    /**
//     * 监听消息
//     */
//    @Listener
//    public synchronized void onMessage(FriendMessageEvent friendMessage) {
//        String msg = friendMessage.getMessageContent().getPlainText();
//        //把空格自动转换为逗号
//        msg = msg.trim().replaceAll(" ", ",");
//        log.info(friendMessage.getFriend().getId() + "提问：" + msg);
//        //AI自动回复
//        String reply = qqChatService.getResponse(msg);
//        //异步回复消息
//        friendMessage.replyAsync(reply);
//    }
//}
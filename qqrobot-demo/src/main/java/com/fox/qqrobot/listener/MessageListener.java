package com.fox.qqrobot.listener;

import com.alibaba.fastjson.JSONObject;
import com.fox.qqrobot.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.OnFriendAddRequest;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.Reply;
import love.forte.simbot.api.message.ReplyAble;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.component.mirai.message.event.MiraiFriendRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 消息监听类
 * Author:木芒果
 */
@Component
@Slf4j
public class MessageListener {
 
    static final String URL = "http://api.qingyunke.com/api.php";
 
 
    /**
     * 监听好友添加请求
     */
    @OnFriendAddRequest
    public void friendAddRequest(MiraiFriendRequest miraiFriendRequest) {
        long fromId = miraiFriendRequest.getEvent().getFromId();
        log.info(fromId + ",添加我为好友");
        //触发关键词即自动同意，否则拒绝
        if (miraiFriendRequest.getEvent().getMessage().equals("我爱你")) {
            log.info("同意添加" + fromId + "为好友");
            miraiFriendRequest.getEvent().accept();
        } else {
            log.info("拒绝添加" + fromId + "为好友");
            miraiFriendRequest.getEvent().reject(false);
        }
    }    
 
 
    /**
     * 监听私聊消息
     */
    @OnPrivate
    public void privateMsg(PrivateMsg privateMsg, MsgSender sender) {
        // 智能聊天
        sendMsg(privateMsg, sender, false);
    }
 
 
    /**
     * 监听群消息
     */
    @OnGroup
    public ReplyAble groupMsg(GroupMsg groupMsg, MsgSender sender) {
        // 默认关闭群聊模式，需要的话把注释去掉
        //return sendMsg(groupMsg, sender, true);
        return null;
    }
 
    /**
     * 通过青客云封装智能聊天
     */
    private ReplyAble sendMsg(MessageGet commonMsg, MsgSender sender, boolean group) {
        log.info("智能聊天中~~~,接收消息：qq={}, msg={}", commonMsg.getAccountInfo().getAccountCode(),
                commonMsg.getMsgContent().getMsg());
        Runtime run = Runtime.getRuntime();
        //锁屏
        if (commonMsg.getMsgContent().getMsg().equals("锁屏")) {
            try {
                run.exec("cmd /c start rundll32.exe user32.dll,LockWorkStation");
                sender.SENDER.sendPrivateMsg(commonMsg, "宝！已经为您的电脑进行锁屏了哦！");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //关机
        if (commonMsg.getMsgContent().getMsg().equals("关机")) {
            try {
                run.exec("cmd /c shutdown -t 30 -s");
                sender.SENDER.sendPrivateMsg(commonMsg, "宝！已经为您的电脑进行30秒倒计时关机了哦！");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //取消关机
        if (commonMsg.getMsgContent().getMsg().equals("取消关机")) {
            try {
                run.exec("cmd /c shutdown -a");
                sender.SENDER.sendPrivateMsg(commonMsg, "宝！已经为您取消关机了哦！");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 
 
        // MsgSender中存在三大送信器，以及非常多的重载方法。
        // 通过get请求调用聊天接口
        String result = HttpUtil.get(URL.concat("?key=free&appid=0&msg=").concat(commonMsg.getMsgContent().getMsg()));
        if (!StringUtils.isEmpty(result)) {
            final JSONObject json = JSONObject.parseObject(result);
            if (json.getInteger("result") == 0 && !StringUtils.isEmpty(json.getString("content"))) {
                final String msg = json.getString("content").replace("{br}", "\n");
                log.info("智能聊天中~~~,发送消息：qq={}, msg={}", commonMsg.getAccountInfo().getAccountCode(), msg);
                //发送群消息
                if (group) {
                    // 参数1：回复的消息 参数2：是否at当事人
                    return Reply.reply(msg, true);
                }
                //发送私聊消息
                sender.SENDER.sendPrivateMsg(commonMsg, msg);
            }
        }
        return null;
    }
 
}
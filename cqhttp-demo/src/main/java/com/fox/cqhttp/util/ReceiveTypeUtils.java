package com.fox.cqhttp.util;

import cn.hutool.core.util.StrUtil;
import com.fox.cqhttp.handle.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:51
 */
public class ReceiveTypeUtils {
    public static final Map<String, ReceiveTypeHandler> RECEIVE_TYPE_MAP =  new HashMap<>();
    static {
        RECEIVE_TYPE_MAP.put("课表",new LessonTableHandler());
        RECEIVE_TYPE_MAP.put("",new ReceiveBlankHandler());
        RECEIVE_TYPE_MAP.put("我的头像",new MyIconHandler());
        RECEIVE_TYPE_MAP.put("浪浪语录",new LangTalkHandler());
        RECEIVE_TYPE_MAP.put("蓄蓄语录",new XuTalkHandler());
        RECEIVE_TYPE_MAP.put("清纯女孩",new GirlHandler());
        RECEIVE_TYPE_MAP.put("我的信息",new MyInfoHandler());
        RECEIVE_TYPE_MAP.put("你的信息",new BotInfoHandler());
        RECEIVE_TYPE_MAP.put("全员禁言",new BanAllHandler());
        RECEIVE_TYPE_MAP.put("解禁",new LiftBanHandler());
        RECEIVE_TYPE_MAP.put("全员解禁",new LiftBanAllHandler());
    }
    public static ReceiveTypeHandler get(String content){
        return RECEIVE_TYPE_MAP.get(StrUtil.removeAll(content,' '));
    }
}

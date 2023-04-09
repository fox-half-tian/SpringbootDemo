package com.fox.cqhttp.util;

import cn.hutool.core.util.StrUtil;
import com.fox.cqhttp.handle.*;
import org.aspectj.weaver.ast.Var;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 0:51
 */
public class ReceiveTypeUtils {
    public static final Map<String, ReceiveTypeHandler> RECEIVE_TYPE_MAP = new HashMap<>();
    private static final ReceiveTypeHandler receiveTypeHandler = new ReceiveBlankHandler();

    static {
        RECEIVE_TYPE_MAP.put("我的课表", new LessonTableHandler());
        RECEIVE_TYPE_MAP.put("随机美腿", new LegHandler());
        RECEIVE_TYPE_MAP.put("我的头像", new MyIconHandler());
        RECEIVE_TYPE_MAP.put("浪浪语录", new LangTalkHandler());
        RECEIVE_TYPE_MAP.put("蓄蓄语录", new XuTalkHandler());
        RECEIVE_TYPE_MAP.put("清纯女孩", new GirlHandler());
        RECEIVE_TYPE_MAP.put("我的信息", new MyInfoHandler());
        RECEIVE_TYPE_MAP.put("你的信息", new BotInfoHandler());
        RECEIVE_TYPE_MAP.put("全员禁言", new BanAllHandler());
        RECEIVE_TYPE_MAP.put("解禁成员", new LiftBanHandler());
        RECEIVE_TYPE_MAP.put("全员解禁", new LiftBanAllHandler());
        RECEIVE_TYPE_MAP.put("舔狗日记", new DogHandler());
        RECEIVE_TYPE_MAP.put("看看姐姐", new BeautyVlogHandler());
        RECEIVE_TYPE_MAP.put("次元女孩", new AnimeHandler());
    }

    public static ReceiveTypeHandler get(String content) {
        String str = StrUtil.removeAll(content, ' ');
        if (str.length() >= 4) {
            return RECEIVE_TYPE_MAP.get(str);
        } else if (str.length() == 0) {
            return receiveTypeHandler;
        } else {
            return null;
        }
    }
}

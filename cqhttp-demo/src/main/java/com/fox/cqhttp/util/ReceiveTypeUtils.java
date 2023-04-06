package com.fox.cqhttp.util;

import cn.hutool.core.util.StrUtil;
import com.fox.cqhttp.handle.LessonTableHandler;
import com.fox.cqhttp.handle.ReceiveBlankHandler;
import com.fox.cqhttp.handle.ReceiveTypeHandler;

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
    }
    public static ReceiveTypeHandler get(String content){
        return RECEIVE_TYPE_MAP.get(StrUtil.removeAll(content,' '));
    }
}

package com.zhulang.xfxh.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 消息对象
 *
 * @author 狐狸半面添
 * @create 2023-09-15 0:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgDTO {
    /**
     * 角色
     */
    private String role;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 响应结果字段：结果序号，取值为[0,10]; 当前为保留字段，开发者可忽略
     */
    private Integer index;

    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";

    public static MsgDTO createUserMsg(String content){
        return new MsgDTO(ROLE_USER,content,null);
    }

    public static MsgDTO createAssistantMsg(String content){
        return new MsgDTO(ROLE_ASSISTANT,content,null);
    }

}

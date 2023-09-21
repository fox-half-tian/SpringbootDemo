package com.zhulang.xfxh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-09-17 0:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractMsg {
    private MsgDTO userMsg;
    private MsgDTO assistantMsg;
}

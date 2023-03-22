package com.fox.es;

import com.alibaba.fastjson.JSON;
import com.fox.es.dto.BlogSimpleInfoDTO;

/**
 * @author 狐狸半面添
 * @create 2023-03-23 1:04
 */
public class CommonTest {
    public static void main(String[] args) {
        String json = "{\"createTime\":\"2023-03-23 00:40:20\",\"introduce\":\"Java的起源\",\"id\":1000,\"title\":\"Java语言\",\"userId\":1626989073847750657}";
        BlogSimpleInfoDTO blogSimpleInfoDTO = JSON.parseObject(json, BlogSimpleInfoDTO.class);
        System.out.println(blogSimpleInfoDTO);
    }
}

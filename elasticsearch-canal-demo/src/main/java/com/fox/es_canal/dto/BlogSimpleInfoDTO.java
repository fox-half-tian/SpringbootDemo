package com.fox.es_canal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-22 21:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSimpleInfoDTO {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

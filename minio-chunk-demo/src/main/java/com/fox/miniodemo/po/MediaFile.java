package com.fox.miniodemo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 第三方服务-媒资文件表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
@TableName("service_media_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（雪花算法）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型：文本，图片，音频，视频，其它
     */
    private String fileType;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 标签
     */
    private String tag;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 文件的md5值
     */
    private String fileMd5;

    /**
     * 文件字节大小
     */
    private Long fileByteSize;
    /**
     * 文件格式化大小
     */
    private String fileFormatSize;

    /**
     * 上传人id
     */
    private Long userId;

    /**
     * 创建时间（上传时间）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
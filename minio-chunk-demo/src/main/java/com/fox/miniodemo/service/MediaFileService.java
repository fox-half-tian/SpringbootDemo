package com.fox.miniodemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fox.miniodemo.entity.Result;
import com.fox.miniodemo.po.MediaFile;

/**
 * <p>
 * 第三方服务-媒资文件表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
public interface MediaFileService extends IService<MediaFile> {

    /**
     * 文件上传前检查文件是否存在
     *
     * @param fileMd5 需要上传的文件的md5值
     * @return 是否存在, false-不存在 true-存在
     */
    Result checkFile(String fileMd5);

    /**
     * 分块文件上传前检测分块文件是否已存在
     *
     * @param fileMd5    分块文件的源文件md5
     * @param chunkIndex 分块文件索引
     * @return 是否存在, false-不存在 true-存在
     */
    Result checkChunk(String fileMd5, Integer chunkIndex);

    /**
     * 上传分块文件
     *
     * @param fileMd5    原文件md5值
     * @param chunkIndex 分块文件索引
     * @param bytes      分块文件的字节数组形式
     * @return 上传情况
     */
    Result uploadChunk(String fileMd5, Integer chunkIndex, byte[] bytes);

    /**
     * 合并分块文件
     *
     * @param fileMd5    文件的md5十六进制值
     * @param fileName   文件名
     * @param tag        文件标签
     * @param chunkTotal 文件块总数
     * @return 合并与上传情况
     */
    Result uploadMergeChunks(String fileMd5, String fileName, String tag, Integer chunkTotal);
}

package com.fox.miniodemo.controller;


import com.alibaba.fastjson.JSONObject;
import com.fox.miniodemo.entity.Result;
import com.fox.miniodemo.service.MediaFileService;
import com.fox.miniodemo.vo.CheckChunkFileVO;
import com.fox.miniodemo.vo.UploadMergeChunksVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 第三方服务-媒资文件表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
@RestController
@RequestMapping("/media-file")
public class MediaFileController {
    @Resource
    private MediaFileService mediaFileService;


    /**
     * 文件上传前检查文件是否已存在
     *
     * @param object 需要上传的文件的md5值
     * @return 是否存在, false-不存在 true-存在。如果存在，则会返回文件信息。
     */
    @PostMapping("/upload/checkFile")
    public Result checkFile(@RequestBody JSONObject object) {
        return mediaFileService.checkFile(object.getString("fileMd5"));
    }

    /**
     * 分块文件上传前检测分块文件是否已存在
     *
     * @param checkChunkFileVO 分块文件的源文件md5和该文件索引
     * @return 是否存在, false-不存在 true-存在
     */
    @PostMapping("/upload/checkChunk")
    public Result checkChunk(@Validated @RequestBody CheckChunkFileVO checkChunkFileVO) {
        return mediaFileService.checkChunk(checkChunkFileVO.getFileMd5(), checkChunkFileVO.getChunkIndex());
    }

    /**
     * 上传分块文件
     *
     * @param file       分块文件
     * @param fileMd5    原文件md5值
     * @param chunkIndex 分块文件索引
     * @return 上传情况
     */
    @PostMapping("/upload/uploadChunk")
    public Result uploadChunk(@RequestParam("file") MultipartFile file,
                              @RequestParam("fileMd5") String fileMd5,
                              @RequestParam("chunkIndex") Integer chunkIndex) throws Exception {
        return mediaFileService.uploadChunk(fileMd5, chunkIndex, file.getBytes());
    }

    /**
     * 合并分块文件
     *
     * @param uploadMergeChunksVO    文件的md5十六进制值+文件名+文件标签+分块文件总数
     * @return 合并与上传情况，如果成功，则返回文件信息
     */
    @PostMapping("/upload/uploadMergeChunks")
    public Result uploadMergeChunks(@Validated @RequestBody UploadMergeChunksVO uploadMergeChunksVO) {

        return mediaFileService.uploadMergeChunks(
                uploadMergeChunksVO.getFileMd5(),
                uploadMergeChunksVO.getFileName(),
                uploadMergeChunksVO.getTag(),
                uploadMergeChunksVO.getChunkTotal()
        );
    }
}

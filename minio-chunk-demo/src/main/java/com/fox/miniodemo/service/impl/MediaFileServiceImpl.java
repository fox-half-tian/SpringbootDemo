package com.fox.miniodemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fox.miniodemo.constant.HttpStatus;
import com.fox.miniodemo.dao.MediaFileMapper;
import com.fox.miniodemo.entity.Result;
import com.fox.miniodemo.po.MediaFile;
import com.fox.miniodemo.service.MediaFileService;
import com.fox.miniodemo.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 第三方服务-媒资文件表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
@Service
public class MediaFileServiceImpl extends ServiceImpl<MediaFileMapper, MediaFile> implements MediaFileService {
    @Resource
    private MediaFileMapper mediaFileMapper;
    @Resource
    private MinioClientUtils minioClientUtils;
    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public Result checkFile(String fileMd5) {
        HashMap<String, Object> resultMap = new HashMap<>();

        // 1.校验 fileMd5 合法性
        if (RegexUtils.isMd5HexInvalid(fileMd5)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件md5格式错误");
        }

        // 2.在文件表存在，并且在文件系统存在，此文件才存在
        // 2.1 判断是否在文件表中存在
        LambdaQueryWrapper<MediaFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MediaFile::getFileMd5, fileMd5);
        MediaFile mediaFile = mediaFileMapper.selectOne(wrapper);
        if (mediaFile == null) {
            resultMap.put("exist", false);
            return Result.ok(resultMap);
        }
        // 2.2 判断是否在文件系统存在
        try {
            InputStream inputStream = minioClientUtils.getObject(mediaFile.getBucket(), mediaFile.getFilePath());
            if (inputStream == null) {
                // 文件不存在
                resultMap.put("exist", false);
                return Result.ok(resultMap);
            }
        } catch (Exception e) {
            // 文件不存在
            resultMap.put("exist", false);
            return Result.ok(resultMap);
        }
        // 3.走到这里说明文件已存在，返回true
        resultMap.put("exist", true);
        // 4.封装文件信息
        resultMap.put("info", encodeFileInfo(mediaFile));
        // 5.返回结果
        return Result.ok(resultMap);
    }

    /**
     * 将 部分信息进行封装加密
     *
     * @param mediaFile 媒资对象
     * @return 加密结果
     */
    private String encodeFileInfo(MediaFile mediaFile) {
        HashMap<String, Object> fileMap = new HashMap<>(5);
        fileMap.put("fileType", mediaFile.getFileType());
        fileMap.put("filePath", mediaFile.getBucket() + "/" + mediaFile.getFilePath());
        fileMap.put("fileFormat", mediaFile.getFileFormat());
        fileMap.put("fileByteSize", mediaFile.getFileByteSize());
        fileMap.put("fileFormatSize", mediaFile.getFileFormatSize());
        return CipherUtils.encrypt(JSON.toJSONString(fileMap));
    }

    @Override
    public Result checkChunk(String fileMd5, Integer chunkIndex) {
        // 1.得到分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 2.分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        // 3.查看是否在文件系统存在（注意关闭流）
        try (
                InputStream inputStream = minioClientUtils.getObject(bucket, chunkFilePath)
        ) {

            if (inputStream == null) {
                //文件不存在
                return Result.ok(false);
            }
        } catch (Exception e) {
            //文件不存在
            return Result.ok(false);
        }

        // 4.走到这里说明文件已存在，返回true
        return Result.ok(true);
    }

    @Override
    public Result uploadChunk(String fileMd5, Integer chunkIndex, byte[] bytes) {
        if (RegexUtils.isMd5HexInvalid(fileMd5)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件md5格式错误");
        }
        if (chunkIndex == null || chunkIndex < 0) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "索引必须大于等于0");
        }
        // 1.得到分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 2.分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        try {
            // 3.将分块上传到文件系统
            minioClientUtils.uploadChunkFile(bytes, bucket, chunkFilePath);
            // 4.上传成功
            return Result.ok();
        } catch (Exception e) {
            // 上传失败
            return Result.error();
        }
    }

    @Override
    public Result uploadMergeChunks(String fileMd5, String fileName, String tag, Integer chunkTotal) {
        try {
            // 1.下载分块
//            Long start = System.currentTimeMillis();
            File[] chunkFiles = downloadChunkFilesFromMinio(fileMd5, chunkTotal);
//            Long end = System.currentTimeMillis();
//            System.out.println("下载分块耗时：" + (end - start) + " ms");
            // 2.根据文件名得到合并后文件的扩展名
            int index = fileName.lastIndexOf(".");
            String extension = index != -1 ? fileName.substring(index) : "";
            File tempMergeFile = null;
            try {
                try {
                    // 3.创建一个临时文件作为合并文件
                    tempMergeFile = File.createTempFile("merge", extension);
                } catch (IOException e) {
                    return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), "创建临时合并文件出错");
                }

                // 4.创建合并文件的流对象
                try (RandomAccessFile rafWrite = new RandomAccessFile(tempMergeFile, "rw")) {
                    byte[] b = new byte[1024];
                    for (File file : chunkFiles) {
                        // 5.读取分块文件的流对象
                        try (RandomAccessFile rafRead = new RandomAccessFile(file, "r");) {
                            int len = -1;
                            while ((len = rafRead.read(b)) != -1) {
                                // 6.向合并文件写数据
                                rafWrite.write(b, 0, len);
                            }
                        }

                    }
                } catch (IOException e) {
                    return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), "合并文件过程出错");
                }


                // 7.校验合并后的文件是否正确
                try (
                        // 7.1 获取合并后文件的流对象
                        FileInputStream mergeFileStream = new FileInputStream(tempMergeFile);
                ) {

                    // 7.2 获取合并文件的md5十六进制值
                    String mergeMd5Hex = DigestUtils.md5Hex(mergeFileStream);
                    // 7.3 校验
                    if (!fileMd5.equals(mergeMd5Hex)) {
                        return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "合并文件校验不通过");
                    }
                } catch (IOException e) {
                    return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), "合并文件校验出错");
                }

                // 8.得到 mimetype
                String mimeType = FileTypeUtils.getMimeType(tempMergeFile);

                // 9.拿到合并文件在minio的存储路径
                String mergeFilePath = getFilePathByMd5(fileMd5, extension);
                // 10.将合并后的文件上传到文件系统
                minioClientUtils.uploadChunkFile(tempMergeFile.getAbsolutePath(), bucket, mergeFilePath);

                // 11.设置需要入库的文件信息
                MediaFile mediaFile = new MediaFile();
                // 11.1 文件名
                mediaFile.setFileName(fileName.trim());
                // 11.2 文件类型
                mediaFile.setFileType(FileTypeUtils.getSimpleType(mimeType));
                // 11.3 文件格式
                mediaFile.setFileFormat(mimeType);
                // 11.4 文件标签
                mediaFile.setTag(tag.trim());
                // 11.5 存储桶
                mediaFile.setBucket(bucket);
                // 11.6 存储路径
                mediaFile.setFilePath(mergeFilePath);
                // 11.7 设置md5值
                mediaFile.setFileMd5(fileMd5);
                // 11.8 设置合并文件大小（单位：字节）
                mediaFile.setFileByteSize(tempMergeFile.length());
                // 11.9 设置文件格式大小
                mediaFile.setFileFormatSize(FileFormatUtils.formatFileSize(mediaFile.getFileByteSize()));
                // 11.10 设置上传者，注意，这里设置为了定值，实际开发中应当根据token拿到当前用户的Id
                mediaFile.setUserId(1622520025167032321L);
                // 12.保存至数据库
                this.save(mediaFile);

                // 15.返回成功
                return Result.ok(encodeFileInfo(mediaFile));
            } finally {
                // 13.删除临时分块文件
                for (File chunkFile : chunkFiles) {
                    if (chunkFile.exists()) {
                        chunkFile.delete();
                    }
                }
                // 14.删除合并的临时文件
                if (tempMergeFile != null) {
                    tempMergeFile.delete();
                }

            }
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 创建10个线程数量的线程池
     */
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * 下载所有的块文件
     *
     * @param fileMd5    源文件的md5值
     * @param chunkTotal 块总数
     * @return 所有块文件
     */
    private File[] downloadChunkFilesFromMinio(String fileMd5, int chunkTotal) throws Exception {
        // 1.得到分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 2.分块文件数组
        File[] chunkFiles = new File[chunkTotal];
        // 3.设置计数器
        CountDownLatch countDownLatch = new CountDownLatch(chunkTotal);
        // 4.开始逐个下载
        for (int i = 0; i < chunkTotal; i++) {
            int index = i;
            threadPool.execute(() -> {
                // 4.1 得到分块文件的路径
                String chunkFilePath = chunkFileFolderPath + index;
                // 4.2 下载分块文件
                try {
                    chunkFiles[index] = minioClientUtils.downloadFile("chunk", null, bucket, chunkFilePath);
                } catch (Exception e) {
                    // 计数器减1
                    countDownLatch.countDown();
                    throw new RuntimeException(e);
                }
                // 计数器减1
                countDownLatch.countDown();
            });
        }

        /*
            阻塞到任务执行完成,当countDownLatch计数器归零，这里的阻塞解除等待,
            给一个充裕的超时时间,防止无限等待，到达超时时间还没有处理完成则结束任务
         */
        countDownLatch.await(30, TimeUnit.MINUTES);

        // 5.返回所有块文件
        return chunkFiles;
    }

    //    private File[] downloadChunkFilesFromMinio(String fileMd5, int chunkTotal) throws Exception {
//        // 1.得到分块文件所在目录
//        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
//        // 2.分块文件数组
//        File[] chunkFiles = new File[chunkTotal];
//        // 3.设置计数器
//        CountDownLatch countDownLatch = new CountDownLatch(chunkTotal);
//        // 4.开始逐个下载
//        for (int i = 0; i < 10; i++) {
//            int index = i;
//            threadPool.execute(() -> {
//                for (int k = index;k<chunkTotal;k += 10) {
//                        // 4.1 得到分块文件的路径
//                        String chunkFilePath = chunkFileFolderPath + k;
//                    // 4.2 下载分块文件
//                    try {
//                        chunkFiles[k] = minioClientUtils.downloadFile("chunk", null, bucket, chunkFilePath);
//                    } catch (Exception e) {
//                        // 计数器减1
//                        countDownLatch.countDown();
//                        throw new RuntimeException(e);
//                    }
//                    // 计数器减1
//                    countDownLatch.countDown();
//                    System.out.println("第 " + k + "块下载完毕");
//                }
//            });
//        }

    /**
     * 得到分块文件的目录
     *
     * @param fileMd5 文件的md5值
     * @return 分块文件所在目录
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    /**
     * 得到合并文件的路径
     *
     * @param fileMd5 文件的md5十六进制值
     * @param fileExt 文件的扩展名
     * @return 合并文件路径
     */
    private String getFilePathByMd5(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }
}

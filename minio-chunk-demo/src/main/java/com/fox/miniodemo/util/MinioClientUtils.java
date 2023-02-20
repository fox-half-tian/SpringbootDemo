package com.fox.miniodemo.util;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;

import java.io.*;

/**
 * 操作minio的工具类
 *
 * @author 狐狸半面添
 * @create 2023-02-08 22:08
 */
public class MinioClientUtils {
    private final MinioClient minioClient;

    public MinioClientUtils(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 获取minio文件的输入流对象
     *
     * @param bucket   桶
     * @param filePath 文件路径
     * @return 输入流
     * @throws Exception 异常
     */
    public InputStream getObject(String bucket, String filePath) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(filePath).build());
    }

    /**
     * 将分块文件上传到分布式文件系统
     *
     * @param bytes    文件的字节数组
     * @param bucket   桶
     * @param filePath 存储在桶中的文件路径
     */
    public void uploadChunkFile(byte[] bytes, String bucket, String filePath) throws Exception {
        // 1.指定资源的媒体类型为未知二进制流，以分片形式上传至minio
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)
        ) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filePath)
                            // InputStream stream, long objectSize 对象大小, long partSize 分片大小(-1表示5M,最大不要超过5T，最多10000)
                            .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                            .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将文件上传到分布式文件系统
     *
     * @param bytes    文件的字节数组
     * @param bucket   桶
     * @param filePath 存储在桶中的文件路径
     */
    public void uploadFile(byte[] bytes, String bucket, String filePath) throws Exception {
        // 1.指定资源的媒体类型，默认未知二进制流
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        // 2.判断是否有后缀，有后缀则根据后缀推算出文件类型，否则使用默认的未知二进制流
        if (filePath.contains(".")) {
            // 取objectName中的扩展名
            String extension = filePath.substring(filePath.lastIndexOf("."));
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }

        // 3.以分片形式上传至minio
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                // InputStream stream, long objectSize 对象大小, long partSize 分片大小(-1表示5M,最大不要超过5T，最多10000)
                .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                .contentType(contentType)
                .build();
        // 上传
        minioClient.putObject(putObjectArgs);
    }


    /**
     * 根据文件路径将文件上传到文件系统
     *
     * @param naiveFilePath 本地文件路径
     * @param bucket        桶
     * @param minioFilePath 保存到minio的文件路径位置
     * @throws Exception 异常
     */
    public void uploadChunkFile(String naiveFilePath, String bucket, String minioFilePath) throws Exception {
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(bucket)
                .object(minioFilePath)
                .filename(naiveFilePath)
                .build();
        minioClient.uploadObject(uploadObjectArgs);
    }

    /**
     * 下载文件保存至本地临时文件中
     *
     * @param tempFilePrefix 临时文件的前缀
     * @param tempFileSuffix 临时文件的后缀
     * @param bucket         桶
     * @param filePath       文件路径
     * @return 携带数据的临时文件
     * @throws Exception 异常信息
     */
    public File downloadFile(String tempFilePrefix, String tempFileSuffix, String bucket, String filePath) throws Exception {
        // 1.创建空文件，临时保存下载下来的分块文件数据
        File tempFile = File.createTempFile(tempFilePrefix, tempFileSuffix);
        try (
                // 2.获取目标文件的输入流对象
                InputStream inputStream = getObject(bucket, filePath);
                // 3.获取临时空文件的输出流对象
                FileOutputStream outputStream = new FileOutputStream(tempFile);
        ) {
            // 4.进行数据拷贝
            IOUtils.copy(inputStream, outputStream);
            // 5.返回保存了数据的临时文件
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

//    public File downloadFile(String tempFilePrefix, String tempFileSuffix, String bucket, String filePath) throws Exception {
//        // 1.创建空文件，临时保存下载下来的分块文件数据
//        File tempFile = File.createTempFile(tempFilePrefix, tempFileSuffix);
//        try {
//            Long start = System.currentTimeMillis();
//            minioClient.downloadObject(
//                    DownloadObjectArgs.builder()
//                            // 指定 bucket 存储桶
//                            .bucket(bucket)
//                            // 指定 哪个文件
//                            .object(filePath)
//                            // 指定存放位置与名称
//                            .filename(tempFile.getPath())
//                            .build());
//            Long end = System.currentTimeMillis();
//            System.out.println("下载分块时间："+(end-start)+"ms");
//            // 5.返回保存了数据的临时文件
//            return tempFile;
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
}

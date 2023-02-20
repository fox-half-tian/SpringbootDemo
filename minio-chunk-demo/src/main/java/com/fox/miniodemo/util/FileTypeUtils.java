package com.fox.miniodemo.util;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型工具类
 *
 * @author 狐狸半面添
 * @create 2023-02-09 0:13
 */
public class FileTypeUtils {
    private static final Map<String, String> contentType = new HashMap<>();

    /**
     * 获取文件的 mime 类型
     *
     * @param file 文件
     * @return mime类型
     */
    public static String getMimeType(File file) {
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());
        Metadata metadata = new Metadata();
        metadata.add(TikaCoreProperties.RESOURCE_NAME_KEY, file.getName());
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * 根据 mimetype 获取文件的简单类型
     *
     * @param mimeType mime类型
     * @return 简单类型：文本，图片，音频，视频，其它
     */
    public static String getSimpleType(String mimeType) {
        String simpleType = mimeType.split("/")[0];
        switch (simpleType) {
            case "text":
                return "文本";
            case "image":
                return "图片";
            case "audio":
                return "音频";
            case "video":
                return "视频";
            case "application":
                return "其它";
            default:
                throw new RuntimeException("mimeType格式错误");
        }
    }

    // 测试
    public static void main(String[] args) {
        File file = new File("D:\\location语法规则.docx");
        String mimeType = getMimeType(file);
        System.out.println(mimeType);
        System.out.println(getSimpleType(mimeType));
    }
}
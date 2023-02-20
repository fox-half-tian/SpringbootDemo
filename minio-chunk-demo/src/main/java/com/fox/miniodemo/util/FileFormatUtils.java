package com.fox.miniodemo.util;

import java.text.DecimalFormat;

/**
 * 文件格式工具
 *
 * @author 狐狸半面添
 * @create 2023-02-09 19:43
 */
public class FileFormatUtils {
    /**
     * 将字节单位的文件大小转为格式化的文件大小表示
     *
     * @param fileLength 文件字节大小
     * @return 格式化文件大小表示
     */
    public static String formatFileSize(long fileLength) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileLength == 0) {
            return wrongSize;
        }
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + " B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + " KB";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + " GB";
        }
        return fileSizeString;
    }
}
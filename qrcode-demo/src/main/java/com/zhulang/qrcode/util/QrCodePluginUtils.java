package com.zhulang.qrcode.util;

import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author 狐狸半面添
 * @create 2023-08-29 21:41
 */
public class QrCodePluginUtils {

    private static String imageParseBase64(BufferedImage image) throws IOException {
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image, "png", fos);
        // 获取二维码图片的 base64 编码
        String imgEncode = Base64.encodeBase64String(fos.toByteArray());
        fos.flush();
        return imgEncode;
    }

    /**
     * 生成普通黑白二维码
     *
     * @param content 文本内容
     * @return 图片base64编码
     */
    public static String generateBlackWhiteCode(String content) throws IOException, WriterException {
        return imageParseBase64(QrCodeGenWrapper.of(content).asBufferedImage());
    }

    /**
     * 生成带 logo 的黑白二维码
     *
     * @param content 文本内容
     * @param logo    logo文件
     * @return 图片base64编码
     */
    public static String generateLogoBlackWhiteCode(String content, MultipartFile logo) throws IOException, WriterException {
        BufferedImage image = QrCodeGenWrapper.of(content)
                .setLogo(logo.getInputStream())
                // 设置 logo 图片与二维码之间的比例，10 表示 logo 的宽度等于二维码的 1/10
                .setLogoRate(10)
                // 设置 logo 图片的样式，将 logo 的边框形状设置为圆形
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .asBufferedImage();
        return imageParseBase64(image);
    }

    /**
     * 生成彩色二维码
     *
     * @param content 文本内容
     * @param color   颜色
     * @return 图片base64编码
     */
    public static String generateColorCode(String content, Color color) throws IOException, WriterException {
        BufferedImage image = QrCodeGenWrapper.of(content)
                // 指定画笔颜色
                .setDrawPreColor(color)
                .asBufferedImage();
        return imageParseBase64(image);
    }

    /**
     * 生成带背景图片的黑白二维码
     *
     * @param content         文本内容
     * @param backgroundImage 背景图文件
     * @return 图片base64编码
     */
    public static String generateBgBlackWhiteCode(String content, MultipartFile backgroundImage) throws IOException, WriterException {
        BufferedImage image = QrCodeGenWrapper.of(content)
                // 设置背景图
                .setBgImg(backgroundImage.getInputStream())
                // 设置背景图透明度
                .setBgOpacity(0.7F)
                .asBufferedImage();
        return imageParseBase64(image);
    }

    /**
     * 生成带特殊形状的二维码
     *
     * @param content   文本内容
     * @param drawStyle 绘制样式
     * @return 图片base64编码
     */
    public static String generateShapeCode(String content, QrCodeOptions.DrawStyle drawStyle) throws IOException, WriterException {
        BufferedImage image = QrCodeGenWrapper.of(content)
                // 启用二维码绘制时的缩放功能
                .setDrawEnableScale(true)
                // 指定绘制样式
                .setDrawStyle(drawStyle)
                .asBufferedImage();
        return imageParseBase64(image);
    }

    /**
     * 生成图片填充二维码
     *
     * @param content 文本内容
     * @param fillImg 填充图片
     * @return 图片base64编码
     */
    public static String generateImgFillCode(String content, MultipartFile fillImg) throws IOException, WriterException {
        BufferedImage image = QrCodeGenWrapper.of(content)
                // 设置二维码的错误纠正级别
                .setErrorCorrection(ErrorCorrectionLevel.H)
                // 绘制二维码时采用图片填充
                .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                // 设置填充的图片
                .addImg(1, 1, fillImg.getInputStream())
                .asBufferedImage();
        return imageParseBase64(image);
    }

}

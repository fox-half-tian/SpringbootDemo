package com.zhulang.qrcode.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-08-28 21:35
 */
public class ZXingUtils {
    public static String generateBlackWhiteCode(String content) throws WriterException, IOException {
        // 使用 Google 提供的 zxing 开源库，生成带 Logo 的黑白二维码

        // 需要创建一个 Map 集合，使用这个 Map 集合存储二维码相关的属性（参数）
        Map<EncodeHintType, Object> map = new HashMap<>(3);

        // 设置二维码的误差校正级别
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置二维码的字符集
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 设置二维码四周的留白，单位为 px
        map.put(EncodeHintType.MARGIN, 1);

        // 创建 zxing 的核心对象，MultiFormatWriter（多格式写入器）
        // 通过 MultiFormatWriter 对象来生成二维码
        MultiFormatWriter writer = new MultiFormatWriter();

        // writer.encode(内容, 什么格式的二维码, 二维码宽度, 二维码高度, 二维码参数)
        // 位矩阵对象（位矩阵对象内部实际上是一个二维数组，二维数组中每一个元素是 boolean 类型，true 代表黑色，false 代表白色）
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, map);

        // 获取矩阵的宽度
        int width = bitMatrix.getWidth();
        // 获取矩阵的高度
        int height = bitMatrix.getHeight();

        // 生成二维码图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 编写一个嵌套循环，遍历二维数组的一个循环，遍历位矩阵对象
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image, "png", fos);
        // 获取二维码图片的 base64 编码
        String imgEncode = Base64.encodeBase64String(fos.toByteArray());
        fos.flush();

        // 返回 base64 编码
        return imgEncode;
    }

    public static String generateLogoCode(String content, MultipartFile logo) throws WriterException, IOException {
        // 使用 Google 提供的 zxing 开源库，生成普通的黑白二维码

        // 需要创建一个 Map 集合，使用这个 Map 集合存储二维码相关的属性（参数）
        Map<EncodeHintType, Object> map = new HashMap<>(3);

        // 设置二维码的误差校正级别
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置二维码的字符集
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 设置二维码四周的留白，单位为 px
        map.put(EncodeHintType.MARGIN, 1);

        // 创建 zxing 的核心对象，MultiFormatWriter（多格式写入器）
        // 通过 MultiFormatWriter 对象来生成二维码
        MultiFormatWriter writer = new MultiFormatWriter();

        // writer.encode(内容, 什么格式的二维码, 二维码宽度, 二维码高度, 二维码参数)
        // 位矩阵对象（位矩阵对象内部实际上是一个二维数组，二维数组中每一个元素是 boolean 类型，true 代表黑色，false 代表白色）
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, map);

        // 获取矩阵的宽度
        int width = bitMatrix.getWidth();
        // 获取矩阵的高度
        int height = bitMatrix.getHeight();

        // 生成二维码图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 编写一个嵌套循环，遍历二维数组的一个循环，遍历位矩阵对象
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 给二维码添加 Logo
        // 1.获取 logo 图片：通过 ImageIO 的read方法，从输入流中读取，从而获得 logo 图片
        BufferedImage logoImage = ImageIO.read(logo.getInputStream());
        // 2.设置 logo 的宽度和高度
        int logoWidth = Math.min(logoImage.getWidth(), 60);
        int logoHeight = Math.min(logoImage.getHeight(), 60);
        // 3.将 logo 缩放：使用平滑缩放算法对原 logo 图像进行缩放得到一个全新的图像
        Image scaledLogo = logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
        // 4.将缩放后的 logo 画到黑白二维码上
        // 4.1 获取一个 2D 的画笔
        Graphics2D graphics2D = image.createGraphics();
        // 4.2 指定开始的坐标 x,y
        int x = (300 - logoWidth) / 2;
        int y = (300 - logoHeight) / 2;
        // 4.3 将缩放后的 logo 画上去
        graphics2D.drawImage(scaledLogo,x,y,null);
        // 4.4 创建一个具有指定位置、宽度、高度和圆角半径的圆角矩形，这个圆角矩形是用来绘制边框的
        Shape shape = new RoundRectangle2D.Float(x,y,logoWidth,logoHeight,10,10);
        // 4.5 使用一个宽度为 4px 的基本笔触
        graphics2D.setStroke(new BasicStroke(4f));
        // 4.6 给 logo 画圆角矩形
        graphics2D.draw(shape);
        // 4.7 释放画笔
        graphics2D.dispose();

        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image, "png", fos);
        // 获取二维码图片的 base64 编码
        String imgEncode = Base64.encodeBase64String(fos.toByteArray());
        fos.flush();

        // 返回 base64 编码
        return imgEncode;
    }
}

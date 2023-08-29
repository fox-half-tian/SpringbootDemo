package com.zhulang.qrcode.controller;

import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import com.zhulang.qrcode.util.QrCodePluginUtils;
import com.zhulang.qrcode.util.ZXingUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-08-29 21:37
 */
@CrossOrigin
@RestController
@RequestMapping("/qrcode-plugin")
public class QrCodePluginController {

    /**
     * 生成普通黑白二维码
     *
     * @param content 文本内容
     * @return 图片base64编码
     */
    @GetMapping("/getCommonBlackWhiteCode")
    public Map<String, String> getCommonBlackWhiteCode(@RequestParam("content") String content) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        map.put("imgEncode", QrCodePluginUtils.generateBlackWhiteCode(content));
        return map;
    }

    /**
     * 生成带 logo 的黑白二维码
     *
     * @param content 文本内容
     * @param logo    logo文件
     * @return 图片base64编码
     */
    @PostMapping("/getLogoBlackWhiteCode")
    public Map<String, String> getLogoBlackWhiteCode(@RequestParam("content") String content, @RequestParam("logo") MultipartFile logo) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        map.put("imgEncode", QrCodePluginUtils.generateLogoBlackWhiteCode(content, logo));
        return map;
    }

    /**
     * 生成彩色二维码
     *
     * @param content 文本内容
     * @return 图片base64编码
     */
    @GetMapping("/getColorCode")
    public Map<String, String> getColorCode(@RequestParam("content") String content) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        // 二维码颜色可以由前端传过来进行指定
        map.put("imgEncode", QrCodePluginUtils.generateColorCode(content, Color.BLUE));
        return map;
    }

    /**
     * 生成带背景图片的黑白二维码
     *
     * @param content         文本内容
     * @param backgroundImage 背景图文件
     * @return 图片base64编码
     */
    @PostMapping("/getBgBlackWhiteCode")
    public Map<String, String> getBgBlackWhiteCode(@RequestParam("content") String content, @RequestParam("backgroundImage") MultipartFile backgroundImage) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        map.put("imgEncode", QrCodePluginUtils.generateBgBlackWhiteCode(content, backgroundImage));
        return map;
    }

    /**
     * 生成带特殊形状的二维码
     *
     * @param content 文本内容
     * @return 图片base64编码
     */
    @GetMapping("/getShapeCode")
    public Map<String, String> getShapeCode(@RequestParam("content") String content) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        // 绘制样式可以由前端传过来进行指定，这里设定为 钻石
        map.put("imgEncode", QrCodePluginUtils.generateShapeCode(content, QrCodeOptions.DrawStyle.DIAMOND));
        return map;
    }

    /**
     * 生成图片填充二维码
     *
     * @param content 文本内容
     * @param fillImg 填充图片
     * @return 图片base64编码
     */
    @PostMapping("/getImgFillCode")
    public Map<String, String> getImgFillCode(@RequestParam("content") String content, @RequestParam("fillImg") MultipartFile fillImg) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        map.put("imgEncode", QrCodePluginUtils.generateImgFillCode(content, fillImg));
        return map;
    }
}

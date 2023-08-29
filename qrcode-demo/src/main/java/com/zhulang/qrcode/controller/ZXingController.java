package com.zhulang.qrcode.controller;

import com.google.zxing.WriterException;
import com.zhulang.qrcode.util.ZXingUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-08-28 21:35
 */
@CrossOrigin
@RestController
@RequestMapping("/zxing")
public class ZXingController {
    @GetMapping("/getCommonBlackWhite")
    public Map<String, String> getCommonBlackWhite(@RequestParam("content") String content) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        map.put("imgEncode", ZXingUtils.generateBlackWhiteCode(content));
        return map;
    }

    @PostMapping("/getLogo")
    public Map<String, String> getLogo(@RequestParam("content") String content, @RequestParam("logo") MultipartFile logo) throws IOException, WriterException {
        Map<String, String> map = new HashMap<>(1);
        map.put("imgEncode", ZXingUtils.generateLogoCode(content, logo));
        return map;
    }
}

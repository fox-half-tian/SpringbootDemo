package com.fox.cqhttp.service;

import cn.hutool.core.util.RandomUtil;
import com.fox.cqhttp.config.TbpClientConfig;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tbp.v20190627.TbpClient;
import com.tencentcloudapi.tbp.v20190627.models.TextProcessRequest;
import com.tencentcloudapi.tbp.v20190627.models.TextProcessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-04-05 22:16
 */
@Service
@Slf4j
public class TxAnswerService {
    @Resource
    private TbpClientConfig tbpClientConfig;
    @Resource
    private TbpClient tbpClient;

    public String getResponse(String inputText) {
        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            TextProcessRequest req = new TextProcessRequest();
            // 设置机器人标识
            req.setBotId(tbpClientConfig.getBotId());
            // 设置机器人版本
            req.setBotEnv(tbpClientConfig.getBotEnv());
            // 设置终端标识，每个终端(或线程)对应一个，区分并发多用户。
            req.setTerminalId(RandomUtil.randomString(16));
            // 设置对方的输入内容
            req.setInputText(inputText);
            // 返回的resp是一个TextProcessResponse的实例，与请求对象对应
            TextProcessResponse resp = tbpClient.TextProcess(req);
            // 获取响应内容并返回
            return resp.getResponseMessage().getGroupList()[0].getContent();
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
            return "抱歉噢，我出了一点点故障，可能是被你伤心了~";
        }
    }
}

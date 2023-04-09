package com.fox.cqhttp.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @create 2023-04-07 12:58
 */
@Service
@Slf4j
public class GptService {
    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

    private int count = 0;
    private long time = System.currentTimeMillis();

    public String getResponse(String inputText) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("prompt", inputText);
        if (count <= 10) {
            count++;
        } else {
            count = 0;
            time = System.currentTimeMillis();
        }
        map.put("userId", "#/chat/" + time);
        map.put("network", true);
        map.put("apikey", "");
        map.put("system", "");
        map.put("withoutContext", false);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<JSONObject> entity = new HttpEntity<>(new com.alibaba.fastjson.JSONObject(map), headers);
        byte[] result;
        try {
            result = restTemplate.postForObject("https://cbjtestapi.binjie.site:7777/api/generateStream", entity, byte[].class);
        } catch (RestClientException e) {
            log.error("openai机器人调用出错：{}",e.getMessage());
            return "抱歉噢，小浪好像出了点小问题。";
        }
        if (result == null) {
            log.error("openai机器人调用出错：结果为空");
            return "抱歉噢，小浪好像出了点小问题。";
        } else {
            return new String(result);
        }
    }
}

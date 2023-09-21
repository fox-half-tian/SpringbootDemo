package com.zhulang.xfxh.component;

import com.zhulang.xfxh.config.XfXhConfig;
import com.zhulang.xfxh.dto.RecordsArray;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 狐狸半面添
 * @create 2023-09-18 1:07
 */
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
@Component
public class ScheduleTask {
    @Resource
    private MemoryUserRecordSpace memoryUserRecordSpace;

    @Resource
    private XfXhConfig xfXhConfig;

    @Scheduled(initialDelayString = "${xfxh.scheduled.updateUserStatusFixedDelay}"
            ,fixedDelayString  = "${xfxh.scheduled.updateUserStatusFixedDelay}")
    @Async
    public void updateUserStatusTask(){
        ConcurrentHashMap<Long, RecordsArray> userRecordMap = memoryUserRecordSpace.getUserRecordMap();
        for (Map.Entry<Long, RecordsArray> userRecord : userRecordMap.entrySet()) {
            RecordsArray recordsArray = userRecord.getValue();
            // 如果已经加锁，说明正在交互消息中，直接跳过
            if (recordsArray.isLock()){
                continue;
            }
            // 将状态+1，当超过 xfXhConfig.userRecordMaxStatus 则从空间中移除该用户交互信息
            recordsArray.setStatus(recordsArray.getStatus()+1);
            if (recordsArray.getStatus()>xfXhConfig.getUserRecordMaxStatus()){
                userRecordMap.remove(userRecord.getKey());
            }
        }
    }
}

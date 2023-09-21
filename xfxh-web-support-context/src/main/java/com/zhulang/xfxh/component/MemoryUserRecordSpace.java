package com.zhulang.xfxh.component;

import com.zhulang.xfxh.config.XfXhConfig;
import com.zhulang.xfxh.dto.InteractMsg;
import com.zhulang.xfxh.dto.MsgDTO;
import com.zhulang.xfxh.dto.RecordsArray;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 狐狸半面添
 * @create 2023-09-17 1:38
 */
@Component
public class MemoryUserRecordSpace {
    @Resource
    private XfXhConfig xfXhConfig;

    private final ConcurrentHashMap<Long, RecordsArray> userRecordMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, RecordsArray> getUserRecordMap() {
        return userRecordMap;
    }

    public List<MsgDTO> getAllInteractMsg(Long id) {
        RecordsArray recordsArray = userRecordMap.get(id);
        if (recordsArray == null) {
            return new ArrayList<>(1);
        }
        return recordsArray.getAllInteractMsg();
    }

    /**
     * 尝试加锁
     *
     * @param id
     * @return true-加锁成功，false-加锁失败
     */
    public boolean tryLock(Long id) {
        synchronized (id.toString().intern()) {
            RecordsArray recordsArray = userRecordMap.get(id);
            // 如果查不到用户或者没有加锁，则允许操作
            if (recordsArray == null) {
                // 用户信息保存到内存
                RecordsArray newRecordsArray = storeUserRecord(id);
                // 加锁
                newRecordsArray.setLock(true);
                return true;
            } else if (!recordsArray.isLock()) {
                // 加锁
                recordsArray.setLock(true);
                // 返回
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 释放锁
     *
     * @param id
     */
    public void unLock(Long id) {
        RecordsArray recordsArray = userRecordMap.get(id);
        if (recordsArray != null) {
            recordsArray.setLock(false);
        }
    }

    public void storeInteractMsg(Long id, InteractMsg interactMsg) {
        RecordsArray recordsArray = userRecordMap.get(id);
        // 为空说明不存在该用户的记录
        if (recordsArray == null) {
            storeUserRecord(id,interactMsg);
            return;
        }

        // 不为空的处理
        recordsArray.addInteractMsg(interactMsg);
        // 刷新状态为最新
        recordsArray.setStatus(0);
    }

    private RecordsArray storeUserRecord(Long id, InteractMsg interactMsg) {
        // 判断是否满了
        while (userRecordMap.size() >= xfXhConfig.getMaxUserCount()) {
            // 需要移除状态最差(status 最高)的用户
            int maxStatus = -1;
            Long userId = 0L;
            for (Map.Entry<Long, RecordsArray> entry : userRecordMap.entrySet()) {
                RecordsArray recordsArray = entry.getValue();
                // 已锁用户不进行处理
                if (!recordsArray.isLock()) {
                    // 获取当前用户的状态
                    int userStatus = recordsArray.getStatus();
                    if (maxStatus < userStatus) {
                        maxStatus = userStatus;
                        userId = entry.getKey();
                    }
                }
            }
            userRecordMap.remove(userId);
        }
        RecordsArray newRecordArray = new RecordsArray(xfXhConfig.getMaxInteractCount());
        if (interactMsg != null) {
            newRecordArray.addInteractMsg(interactMsg);
        }
        userRecordMap.put(id, newRecordArray);
        return newRecordArray;
    }

    private RecordsArray storeUserRecord(Long id){
        return storeUserRecord(id,null);
    }

}

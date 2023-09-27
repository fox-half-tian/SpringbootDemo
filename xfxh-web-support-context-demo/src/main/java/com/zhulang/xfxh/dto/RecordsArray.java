package com.zhulang.xfxh.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-09-17 0:49
 */
public class RecordsArray {

    /**
     * 操作锁，默认 false 即未锁
     * true-已锁
     * false-未锁
     */
    private boolean lock;

    /**
     * 记录状态，默认为 0
     * 0: 最新记录
     * 1: 最近一次交互在 10 分钟前
     * 2: 最近一次交互在 20 分钟前
     * ...
     * 设置最大在 60 分钟后消息被销毁
     */
    private int status;

    /**
     * 表示数组的最大容量,始终留一个空间作为 rear 标记点,实际的允许的交互记录大小为 arraySize-1
     */
    private final int arrayMaxSize;

    /**
     * front 初始值为 0，指向队列的第一个元素, 也就是说 interactMsgArr[front] 就是队列的第一个元素
     */
    private int front;

    /**
     * 队列尾，rear的初始值 = 0，指向队列的最后一个元素的后一个位置，因为希望空出一个空间做为约定
     */
    private int rear;

    /**
     * 存放交互记录，模拟队列
     */
    private final InteractMsg[] interactMsgArr;

    public RecordsArray(int maxInteractCount) {
        // +1 留出一个空间作为 rear 标记点
        this.arrayMaxSize = maxInteractCount + 1;
        this.interactMsgArr = new InteractMsg[arrayMaxSize];
    }

    /**
     * 判断队列是否满
     *
     * @return true-队满，false-未满
     */
    public boolean isFull() {
        return (rear + 1) % arrayMaxSize == front;
    }


    /**
     * 添加交互消息
     */
    public void addInteractMsg(InteractMsg interactMsg) {
        // 判断队列是否满
        if (isFull()) {
            // 队满，front 向后移动相当于将最旧的数据出队列
            front = (front + 1) % arrayMaxSize;
        }
        // 队列未满，无需将最旧的数据出队列，即 front 无需移动

        // 无论是否队满，新的交互信息还是要添加的，因此 rear 处设置为最新的交互消息，并向后移动
        interactMsgArr[rear] = interactMsg;
        rear = (rear + 1) % arrayMaxSize;
    }

    /**
     * 获取存储的交互消息
     */
    public List<MsgDTO> getAllInteractMsg() {
        int realSize = size();
        ArrayList<MsgDTO> msgList = new ArrayList<>(realSize * 2);
        for (int i = front; i < front + realSize; i++) {
            InteractMsg interactMsg = interactMsgArr[i % arrayMaxSize];
            if (interactMsg.getUserMsg() != null) {
                msgList.add(interactMsg.getUserMsg());
            }
            if (interactMsg.getAssistantMsg() != null) {
                msgList.add(interactMsg.getAssistantMsg());
            }
        }
        return msgList;
    }

    /**
     * 求出当前队列有效数据的个数
     *
     * @return 有效个数
     */
    public int size() {
        //当rear>=front时，有效个数为rear-front
        //当rear<front时，有效个数为(maxSize-front)+rear
        return (rear + arrayMaxSize - front) % arrayMaxSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}

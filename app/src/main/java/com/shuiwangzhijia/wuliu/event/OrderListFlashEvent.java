package com.shuiwangzhijia.wuliu.event;

/**
 * created by wangsuli on 2018/8/27.
 */
public class OrderListFlashEvent {
    public boolean isFlash;
    public int type;

    public OrderListFlashEvent(boolean isBuy, int type) {
        isFlash = isBuy;
        this.type = type;
    }
}

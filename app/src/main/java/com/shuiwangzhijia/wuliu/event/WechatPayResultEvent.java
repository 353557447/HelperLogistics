package com.shuiwangzhijia.wuliu.event;

public class WechatPayResultEvent {
    //0 不成功  1 成功
    public int result;

    public WechatPayResultEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

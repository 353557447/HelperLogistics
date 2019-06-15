package com.shuiwangzhijia.wuliu.event;

/**
 * Created by Lijn on 2018/12/13.
 */

public class CommonEvent {
    private String msg;

    public CommonEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

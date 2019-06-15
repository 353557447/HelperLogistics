package com.shuiwangzhijia.wuliu.http;

import java.io.Serializable;

/**
 * 网络请求返回数据封装类
 * Created by wangsuli on 2018/8/22.
 */
public class EntityObject<T> implements Serializable {
    private int code;

    public int getScode() {
        return scode;
    }

    public void setScode(int scode) {
        this.scode = scode;
    }

    private int scode;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(T data) {
        this.data = data;
    }

    public T getResult() {
        return this.data;
    }

}

package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2018/12/7.
 */

public class CanShuipiaoBean implements Serializable {

    /**
     * s_name : 水票2
     * s_gid : 12346
     */

    private String s_name;
    private int s_gid;
    private boolean check;//选择状态
    private int count;//标记数量

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num;


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public int getS_gid() {
        return s_gid;
    }

    public void setS_gid(int s_gid) {
        this.s_gid = s_gid;
    }
}

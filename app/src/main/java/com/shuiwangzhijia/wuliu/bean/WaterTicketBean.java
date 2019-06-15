package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2019/2/19.
 */

public class WaterTicketBean implements Serializable{

    /**
     * s_name : 一号商品112水票
     * num : 6
     */

    private String s_name;
    private int num;
    private String gname;
    /**
     * gname : 二号商品1
     */
    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }
}

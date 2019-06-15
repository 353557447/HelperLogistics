package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2018/12/7.
 */

public class CanChangeBucketBean implements Serializable {

    /**
     * bid : 66666
     * bname : 云峰山泉 桶装饮用水 16.8L/桶 空桶
     */

    private int bid;
    private String bname;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}

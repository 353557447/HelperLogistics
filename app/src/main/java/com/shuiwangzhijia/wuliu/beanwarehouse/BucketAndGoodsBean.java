package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;

/**
 * Created by xxc on 2019/1/11.
 */

public class BucketAndGoodsBean implements Serializable{

    /**
     * bid : 44444
     * bname : 啊啊啊 空桶
     * num : 2
     */

    private String bid;
    private String bname;
    private String num;
    /**
     * gid : 27891416
     * gname : 福能达空气水 瓶装水 550ml*24箱装
     * num : 2
     */

    private int gid;
    private String gname;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

}

package com.shuiwangzhijia.wuliu.beanwarehouse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xxc on 2018/11/30.
 */

public class AddBucketBean implements Serializable {


    /**
     * bid : 77777
     * bname : 空桶
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
    @SerializedName("num")
    private int numX;

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

    public int getNumX() {
        return numX;
    }

    public void setNumX(int numX) {
        this.numX = numX;
    }
}

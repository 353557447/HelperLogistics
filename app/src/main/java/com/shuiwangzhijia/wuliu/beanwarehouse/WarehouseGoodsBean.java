package com.shuiwangzhijia.wuliu.beanwarehouse;


import java.io.Serializable;

/**
 * 商品实体类
 * created by wangsuli on 2018/8/22.
 */
public class WarehouseGoodsBean implements Serializable {
    /**
     * gid : 31351117
     * gname : 滴答桶装水(测试)
     * num : 2
     */

    private int gid;
    private String gname;
    private int num;
    private String bid;

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

    private String bname;
    private boolean check;//选择状态
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    /**
     * gname : 福能达空气水 瓶装水 550ml*24箱装
     * gid : 27891416
     * snum : 5
     */


}

package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * created by wangsuli on 2018/10/24.
 */
public class BucketBean implements Serializable {
    /**
     * gid : 53137621
     * gname : 健康
     */

    private String gid;
    private String gname;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    /**
     * id : 4
     * bname : 怡宝纯净水8.9L 空桶
     * bid : 11111111
     * b_picturl : 20180926/74b5154cf9723e8eca21f74c27fe7c0c.jpg
     * b_price : 40.00
     */

    private int sid;
    private int id;
    private int num;
    private int tnum;
    private String bname;
    private int bid;
    private String b_picturl;
    private double b_price;

    public String getPicturl() {
        return picturl;
    }

    public void setPicturl(String picturl) {
        this.picturl = picturl;
    }

    private String picturl;

    /**
     * sname : 华能水店4
     * bet : 5
     */

    private String sname;
    private int bet;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    private boolean check;//选择状态

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;//标记数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getB_picturl() {
        return b_picturl;
    }

    public void setB_picturl(String b_picturl) {
        this.b_picturl = b_picturl;
    }

    public double getB_price() {
        return b_price;
    }

    public void setB_price(double b_price) {
        this.b_price = b_price;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getTnum() {
        return tnum;
    }

    public void setTnum(int tnum) {
        this.tnum = tnum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }
}

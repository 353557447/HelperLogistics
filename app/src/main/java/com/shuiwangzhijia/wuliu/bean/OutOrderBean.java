package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2018/12/4.
 */

public class OutOrderBean implements Serializable {

    /**
     * uname : 独孤九败     提货人
     * out_order : 2018120316580640
     * total : 20       需提货
     * sum : 20     实际提货
     * tsum : 0     退水
     * hsum : 0     回桶
     * create_time : 1543822506     出仓时间
     * cname : 独孤九败
     * status : 3       3表示已完成
     */

    private String uname;
    private String out_order;
    private int total;
    private int sum;
    private int tsum;
    private int hsum;

    public int getZsum() {
        return zsum;
    }

    public void setZsum(int zsum) {
        this.zsum = zsum;
    }

    private int zsum;
    private int create_time;
    private String cname;
    private int status;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getOut_order() {
        return out_order;
    }

    public void setOut_order(String out_order) {
        this.out_order = out_order;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getTsum() {
        return tsum;
    }

    public void setTsum(int tsum) {
        this.tsum = tsum;
    }

    public int getHsum() {
        return hsum;
    }

    public void setHsum(int hsum) {
        this.hsum = hsum;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

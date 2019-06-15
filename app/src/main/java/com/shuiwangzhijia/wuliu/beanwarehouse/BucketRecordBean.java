package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.util.List;

/**
 * created by wangsuli on 2018/10/24.
 */
public class BucketRecordBean {

    /**
     * id : 35
     * sname : 华能水店7
     * tprice : 80.00
     * tnum : 2
     * order_no : 2018102383087101
     * data : [{"id":4,"bname":"怡宝纯净水8.9L 空桶","num":2}]
     */

    private int id;
    private String sname;
    private String tprice;
    private long update_time;
    private int tnum;
    private String order_no;

    public List<BucketBean> getData() {
        return data;
    }

    public void setData(List<BucketBean> data) {
        this.data = data;
    }

    private List<BucketBean> data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTprice() {
        return tprice;
    }

    public void setTprice(String tprice) {
        this.tprice = tprice;
    }

    public int getTnum() {
        return tnum;
    }

    public void setTnum(int tnum) {
        this.tnum = tnum;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}

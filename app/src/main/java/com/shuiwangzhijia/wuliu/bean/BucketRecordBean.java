package com.shuiwangzhijia.wuliu.bean;

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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getBucket_order_sn() {
        return bucket_order_sn;
    }

    public void setBucket_order_sn(String bucket_order_sn) {
        this.bucket_order_sn = bucket_order_sn;
    }

    private int sum;
    private long order_time;
    private String total_price;
    private String bucket_order_sn;

    public List<BucketBean> getData() {
        return goods;
    }

    public void setData(List<BucketBean> data) {
        this.goods = data;
    }

    private List<BucketBean> goods;

    public List<BucketBean> getGoods() {
        return goods;
    }

    public void setGoods(List<BucketBean> goods) {
        this.goods = goods;
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

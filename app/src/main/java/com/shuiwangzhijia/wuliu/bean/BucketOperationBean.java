package com.shuiwangzhijia.wuliu.bean;

import java.util.List;

/**
 * Created by xxc on 2019/1/16.
 */

public class BucketOperationBean {

    /**
     * sid : 3
     * sname : 华强北水店
     * sum : 1
     * update_time : 1547623146
     * total_price : 0.01
     * bucket_order_sn : 2019011656582515
     * goods : [{"gname":"健康","picturl":"20180926/d0fbeac261b595bc7b487854e6edf65b.jpg","num":1}]
     * total : 0
     */

    private int sid;
    private String sname;
    private int sum;
    private int update_time;
    private String total_price;
    private String bucket_order_sn;
    private int total;
    private List<BucketBean> goods;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BucketBean> getGoods() {
        return goods;
    }

    public void setGoods(List<BucketBean> goods) {
        this.goods = goods;
    }
}

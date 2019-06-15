package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * created by wangsuli on 2018/9/5.
 */
public class RecordBean implements Serializable {
    /**
     * id : 78
     * num : 73
     * update_time : 1536313604
     * stay_water : 0
     * already_water : 18
     */

    private int id;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    private int sid;
    private String num;
    private long update_time;
    private int stay_water;
    private String already_water;
    /**
     * sname : 测试水店121212
     * payment : null
     * driver_payment : null
     */

    private String sname;
    private double payment;
    private double driver_payment;
    /**
     * selfnum : 5
     * znum : 5
     * update_time : 1535961960
     */

    private int selfnum;
    private int znum;

    public int getZ_num() {
        return z_num;
    }

    public void setZ_num(int z_num) {
        this.z_num = z_num;
    }

    private int z_num;

    public int getTnum() {
        return tnum;
    }

    public void setTnum(int tnum) {
        this.tnum = tnum;
    }

    private int tnum;
    /**
     * update_time : 1535961960
     * num : 5
     * data : [{"id":1,"sname":"红火水店","num":3,"gname":"测试666"},{"id":5,"sname":"红火水店","num":5,"gname":"测试666"},{"id":6,"sname":"红火水店","num":10,"gname":"测试商品1"}]
     */

    private List<BucketBean> data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getStay_water() {
        return stay_water;
    }

    public void setStay_water(int stay_water) {
        this.stay_water = stay_water;
    }

    public String getAlready_water() {
        return already_water;
    }

    public void setAlready_water(String already_water) {
        this.already_water = already_water;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getDriver_payment() {
        return driver_payment;
    }

    public void setDriver_payment(double driver_payment) {
        this.driver_payment = driver_payment;
    }

    public int getSelfnum() {
        return selfnum;
    }

    public void setSelfnum(int selfnum) {
        this.selfnum = selfnum;
    }

    public int getZnum() {
        return znum;
    }

    public void setZnum(int znum) {
        this.znum = znum;
    }


    public List<BucketBean> getData() {
        return data;
    }

    public void setData(List<BucketBean> data) {
        this.data = data;
    }

}

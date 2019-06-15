package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;

/**
 * created by wangsuli on 2018/9/11.
 */
public class PreparePayBean implements Serializable {

    /**
     * order_sns : 2018091182402593
     * sprice : 162.00
     * time : 1536651676
     */

    private String order_sns;
    private String sprice;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    private String dname;

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    private String policy;
    private long time;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    /**
     * order_no : 2018091132722409
     * uid : 92
     * tamount : 81
     * amount : 81
     * tsum : 9
     * sum : 9
     * pay_status : 0
     * buk : 1
     * remark :
     * addr : 程 13612345678 华能大厦(深圳市福田区深南中路2068) 啦啦啦
     * create_time : 2018-09-11 15:24:50
     * update_time : 2018-09-11 15:24:50
     * id : 80
     */
    private String sname;
    private String payOrderSn;
    private String payOrderMoney;
    private String order_no;
    private String uid;
    private String tamount;
    private double amount;
    private int tsum;
    private int sum;

    public int getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(int payFrom) {
        this.payFrom = payFrom;
    }

    private int payFrom; //1代表1换1代付，2代表代下单代付   3追加订单代付
    private int pay_status;
    private String buk;
    private String remark;
    private String addr;
    private String create_time;
    private String update_time;
    private String id;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTamount() {
        return tamount;
    }

    public void setTamount(String tamount) {
        this.tamount = tamount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTsum() {
        return tsum;
    }

    public void setTsum(int tsum) {
        this.tsum = tsum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public String getBuk() {
        return buk;
    }

    public void setBuk(String buk) {
        this.buk = buk;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayOrderMoney() {
        return payOrderMoney;
    }

    public void setPayOrderMoney(String payOrderMoney) {
        this.payOrderMoney = payOrderMoney;
    }

    public String getPayOrderSn() {
        return payOrderSn;
    }

    public void setPayOrderSn(String payOrderSn) {
        this.payOrderSn = payOrderSn;
    }

    public String getOrder_sns() {
        return order_sns;
    }

    public void setOrder_sns(String order_sns) {
        this.order_sns = order_sns;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2019/3/18.
 */

public class ChangeModeBean implements Serializable{

    /**
     * pay_status : 1
     * change_way : 0
     * total_price : 0.00
     * mix_num : 6
     * sum : 0
     * bucket_order_sn : 2019031816587442
     * po_num : 6
     * water_ticket : null
     */

    private int pay_status;
    private int change_way;
    private String total_price;
    private int mix_num;
    private int sum;
    private String bucket_order_sn;
    private int po_num;
    private Object water_ticket;

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public int getChange_way() {
        return change_way;
    }

    public void setChange_way(int change_way) {
        this.change_way = change_way;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getMix_num() {
        return mix_num;
    }

    public void setMix_num(int mix_num) {
        this.mix_num = mix_num;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getBucket_order_sn() {
        return bucket_order_sn;
    }

    public void setBucket_order_sn(String bucket_order_sn) {
        this.bucket_order_sn = bucket_order_sn;
    }

    public int getPo_num() {
        return po_num;
    }

    public void setPo_num(int po_num) {
        this.po_num = po_num;
    }

    public Object getWater_ticket() {
        return water_ticket;
    }

    public void setWater_ticket(Object water_ticket) {
        this.water_ticket = water_ticket;
    }
}

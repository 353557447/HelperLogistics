package com.shuiwangzhijia.wuliu.bean;

/**
 * created by wangsuli on 2018/9/10.
 */
public class UserBean {
    /**
     * phone : 13912345678
     * uname : 司机1
     * is_policy : 0
     * today_water : 0
     * stay_water : 0
     * already_water : 0
     * today_price : 0
     * payment : 0
     * driver_payment : 0
     * today_buck : 0
     * self_buck : 0
     * znum_buck : 0
     */

    private String phone;
    private String uname;
    private int is_policy;
    private int today_water;
    private int stay_water;
    private int already_water;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private double price;
    private double today_price;
    private double payment;
    private double driver_payment;
    private int today_buck;
    private int self_buck;
    private int znum_buck;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getIs_policy() {
        return is_policy;
    }

    public void setIs_policy(int is_policy) {
        this.is_policy = is_policy;
    }

    public int getToday_water() {
        return today_water;
    }

    public void setToday_water(int today_water) {
        this.today_water = today_water;
    }

    public int getStay_water() {
        return stay_water;
    }

    public void setStay_water(int stay_water) {
        this.stay_water = stay_water;
    }

    public int getAlready_water() {
        return already_water;
    }

    public void setAlready_water(int already_water) {
        this.already_water = already_water;
    }

    public double getToday_price() {
        return today_price;
    }

    public void setToday_price(int today_price) {
        this.today_price = today_price;
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

    public void setDriver_payment(int driver_payment) {
        this.driver_payment = driver_payment;
    }

    public int getToday_buck() {
        return today_buck;
    }

    public void setToday_buck(int today_buck) {
        this.today_buck = today_buck;
    }

    public int getSelf_buck() {
        return self_buck;
    }

    public void setSelf_buck(int self_buck) {
        this.self_buck = self_buck;
    }

    public int getZnum_buck() {
        return znum_buck;
    }

    public void setZnum_buck(int znum_buck) {
        this.znum_buck = znum_buck;
    }
}

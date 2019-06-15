package com.shuiwangzhijia.wuliu.bean;

/**
 * created by wangsuli on 2018/9/10.
 */
public class PolicyBean {

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    /**
     * pay_no : 2018091175951437
     * price : 20.0
     * time : 1536644634
     */

    private String order_no;
    private String price;
    private long time;
    /**
     * dname : 什么水厂
     * sname : 华能水店7
     */

    private String dname;
    private String sname;

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    private String policy;



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
}

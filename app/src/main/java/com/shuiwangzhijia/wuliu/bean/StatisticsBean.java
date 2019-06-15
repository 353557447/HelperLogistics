package com.shuiwangzhijia.wuliu.bean;

/**
 * created by wangsuli on 2018/8/23.
 */
public class StatisticsBean {
    /**
     * purchasing_num : 110
     * total_price : 6941.00
     * order_num : 9
     * t_total : 1262.00
     */

    private String purchasing_num;
    private String total_price;
    private int order_num;
    private String t_total;

    public String getPurchasing_num() {
        return purchasing_num;
    }

    public void setPurchasing_num(String purchasing_num) {
        this.purchasing_num = purchasing_num;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getT_total() {
        return t_total;
    }

    public void setT_total(String t_total) {
        this.t_total = t_total;
    }
}

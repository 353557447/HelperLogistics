package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxc on 2018/11/30.
 */

public class WareHouseOperationBean implements Serializable {

    /**
     * uname : 测试司机1
     * out_order : 1234567891
     * sum : 4
     * miscellaneous : 3
     */

    private String uname;
    private String out_order;
    private int sum;
    private String miscellaneous;

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    private int order_type;

    public int getPobucket() {
        return pobucket;
    }

    public void setPobucket(int pobucket) {
        this.pobucket = pobucket;
    }

    private int pobucket;
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(String miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    public List<WarehouseGoodsBean> getRefund() {
        return refund;
    }

    public void setRefund(List<WarehouseGoodsBean> refund) {
        this.refund = refund;
    }

    public List<WarehouseGoodsBean> getBack() {
        return back;
    }

    public void setBack(List<WarehouseGoodsBean> back) {
        this.back = back;
    }

    public List<WarehouseGoodsBean> getActual() {
        return actual;
    }

    public void setActual(List<WarehouseGoodsBean> actual) {
        this.actual = actual;
    }

    private List<WarehouseGoodsBean> refund;//退水数据
    private List<WarehouseGoodsBean> back;  //回自营桶
    private List<WarehouseGoodsBean> actual; //实际数据

    public ArrayList<WarehouseGoodsBean> getZybucket() {
        return zybucket;
    }

    public void setZybucket(ArrayList<WarehouseGoodsBean> zybucket) {
        this.zybucket = zybucket;
    }

    private ArrayList<WarehouseGoodsBean> zybucket;//自营桶数据
}

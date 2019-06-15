package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by wangsuli on 2018/9/27.
 */
public class UseTicketBean implements Serializable{
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<TicketBean> getList() {
        return list;
    }

    public void setList(ArrayList<TicketBean> list) {
        this.list = list;
    }

    private double amount;
    private int count;
    private ArrayList<TicketBean> list;
    private List<GoodsBean> goodsData;

    public void setGoodsList(List<GoodsBean> buyData) {
        goodsData=buyData;
    }
    public List<GoodsBean> getGoodsList() {
        return goodsData;
    }
}

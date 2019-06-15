package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2019/2/19.
 */

public class EditDeliveryOrderBean implements Serializable{

    /**
     * id : 367
     * uname : C司机
     * out_order : 2019021916585981
     * create_time : 1550560036
     * remark : 暂无备注
     * sum : 5
     * status : 待出货
     * status_type : 0
     * goods : [{"gname":"幸福","num":5}]
     * goods_additional : []
     */

    private int id;
    private String uname;
    private String out_order;
    private int create_time;
    private String remark;
    private int sum;
    private String status;
    private int status_type;
    private List<GoodsBean> goods;
    private List<GoodsBean> goods_additional;
    /**
     * goods_num : 5
     * goods_additional : []
     * goods_additional_num : null
     */

    private String goods_num;
    private String goods_additional_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatus_type() {
        return status_type;
    }

    public void setStatus_type(int status_type) {
        this.status_type = status_type;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<GoodsBean> getGoods_additional() {
        return goods_additional;
    }

    public void setGoods_additional(List<GoodsBean> goods_additional) {
        this.goods_additional = goods_additional;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_additional_num() {
        return goods_additional_num;
    }

    public void setGoods_additional_num(String goods_additional_num) {
        this.goods_additional_num = goods_additional_num;
    }
}

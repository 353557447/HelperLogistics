package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 出货订单详情
 * Created by xxc on 2018/12/5.
 */

public class OutOrderDetailBean implements Serializable{

    /**
     * id : 367
     * uname : C司机
     * out_order : 2019021916585981
     * create_time : 1550560036
     * remark : 暂无备注
     * sum : 5
     * status : 待出货
     * status_type : 0
     * goods : [{"gid":"21250220","gname":"幸福","num":10}]
     * goods_num : 10
     */

    private int id;
    private String uname;
    private String out_order;
    private int create_time;
    private String remark;
    private int sum;
    private String status;
    private int status_type;
    private int goods_num;
    private List<GoodsBean> goods;
    /**
     * goods_additional : [{"gid":"98000723","gname":"一号商品112","num":2},{"gid":"91132136","gname":"二号商品1","num":1}]
     * goods_additional_num : 3
     */

    private int goods_additional_num;
    private List<GoodsBean> goods_additional;

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

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public int getGoods_additional_num() {
        return goods_additional_num;
    }

    public void setGoods_additional_num(int goods_additional_num) {
        this.goods_additional_num = goods_additional_num;
    }

    public List<GoodsBean> getGoods_additional() {
        return goods_additional;
    }

    public void setGoods_additional(List<GoodsBean> goods_additional) {
        this.goods_additional = goods_additional;
    }

    public static class GoodsBean {
        /**
         * gid : 21250220
         * gname : 幸福
         * num : 10
         */

        private String gid;
        private String gname;
        private int num;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}

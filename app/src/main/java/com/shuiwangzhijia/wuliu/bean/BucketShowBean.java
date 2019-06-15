package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/12/7.
 */

public class BucketShowBean implements Serializable {

    /**
     * order_no : 2018102488895782
     * z_num : 1
     * s_num : 1
     * num : 0
     * barrel_way : 0
     * price : 35.00
     * pay_status : 1
     * goods : [{"bid":44444,"bname":"啊啊啊 空桶","num":1}]
     */

    private String order_no;
    private int z_num;
    private int s_num;
    private int num;
    private int barrel_way;
    private String price;
    private int pay_status;
    private List<GoodsBean> goods;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getZ_num() {
        return z_num;
    }

    public void setZ_num(int z_num) {
        this.z_num = z_num;
    }

    public int getS_num() {
        return s_num;
    }

    public void setS_num(int s_num) {
        this.s_num = s_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getBarrel_way() {
        return barrel_way;
    }

    public void setBarrel_way(int barrel_way) {
        this.barrel_way = barrel_way;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * bid : 44444
         * bname : 啊啊啊 空桶
         * num : 1
         */

        private int bid;
        private String bname;
        private int num;

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}

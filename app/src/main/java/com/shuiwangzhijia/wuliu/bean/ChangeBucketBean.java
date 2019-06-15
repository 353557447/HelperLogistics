package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2019/2/19.
 */

public class ChangeBucketBean implements Serializable{
    /**
     * bucket : {"pay_status":1,"change_way":0,"total_price":"0.02","mix_num":1,"sum":3,"bucket_order_sn":"2019021916585937"}
     * goods : [{"gname":"一号商品112","num":1},{"gname":"二号商品1","num":1},{"gname":"三号商品13","num":1}]
     * water_ticket : [{"s_name":"一号商品112水票","num":6}]
     */

    private BucketBean bucket;
    private List<WaterTicketBean> goods;
    private List<WaterTicketBean> water_ticket;

    public BucketBean getBucket() {
        return bucket;
    }

    public void setBucket(BucketBean bucket) {
        this.bucket = bucket;
    }

    public List<WaterTicketBean> getGoods() {
        return goods;
    }

    public void setGoods(List<WaterTicketBean> goods) {
        this.goods = goods;
    }

    public List<WaterTicketBean> getWater_ticket() {
        return water_ticket;
    }

    public void setWater_ticket(List<WaterTicketBean> water_ticket) {
        this.water_ticket = water_ticket;
    }

    public static class BucketBean {
        /**
         * pay_status : 1
         * change_way : 0
         * total_price : 0.02
         * mix_num : 1
         * sum : 3
         * bucket_order_sn : 2019021916585937
         */

        private int pay_status;
        private int change_way;
        private String total_price;
        private int mix_num;
        private int sum;
        private String bucket_order_sn;

        public int getPo_num() {
            return po_num;
        }

        public void setPo_num(int po_num) {
            this.po_num = po_num;
        }

        private int po_num;

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
    }
}

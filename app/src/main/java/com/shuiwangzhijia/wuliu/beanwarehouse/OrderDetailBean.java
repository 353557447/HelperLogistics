package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/11/30.
 */

public class OrderDetailBean implements Serializable {

    /**
     * out_order : 2019012416584765
     * sender : 一号人员
     * driver : 二号司机
     * shipment_time : 1548322955
     * back_time : 1548324009
     * need : [{"gid":91132136,"gname":"二号商品","num":3},{"gid":78133110,"gname":"三号商品","num":1}]
     * actual : [{"gid":91132136,"gname":"二号商品","num":3},{"gid":78133110,"gname":"三号商品","num":1}]
     * need_count : 4
     * actual_count : 4
     * back_count : 3
     * back : [{"bname":"三号商品","bid":"78133110","num":1},{"bname":"二号商品","bid":"91132136","num":2}]
     * refund_count : 3
     * refund : [{"gname":"三号商品","gid":"78133110","num":1},{"gname":"二号商品","gid":"91132136","num":2}]
     * miscellaneous : 0
     */

    private String out_order;
    private String sender;
    private String driver;
    private int shipment_time;
    private int back_time;
    private int need_count;
    private int actual_count;
    private int back_count;
    private int refund_count;
    private int miscellaneous;
    private List<NeedBean> need;
    private List<ActualBean> actual;
    private List<BackBean> back;
    private List<RefundBean> refund;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String uname;

    public int getZybucket_count() {
        return zybucket_count;
    }

    public void setZybucket_count(int zybucket_count) {
        this.zybucket_count = zybucket_count;
    }

    private int zybucket_count;

    public List<NeedBean> getZybucket() {
        return zybucket;
    }

    public void setZybucket(List<NeedBean> zybucket) {
        this.zybucket = zybucket;
    }

    private List<NeedBean> zybucket;
    public int getPobucket() {
        return pobucket;
    }

    public void setPobucket(int pobucket) {
        this.pobucket = pobucket;
    }

    private int pobucket;

    public String getOut_order() {
        return out_order;
    }

    public void setOut_order(String out_order) {
        this.out_order = out_order;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getShipment_time() {
        return shipment_time;
    }

    public void setShipment_time(int shipment_time) {
        this.shipment_time = shipment_time;
    }

    public int getBack_time() {
        return back_time;
    }

    public void setBack_time(int back_time) {
        this.back_time = back_time;
    }

    public int getNeed_count() {
        return need_count;
    }

    public void setNeed_count(int need_count) {
        this.need_count = need_count;
    }

    public int getActual_count() {
        return actual_count;
    }

    public void setActual_count(int actual_count) {
        this.actual_count = actual_count;
    }

    public int getBack_count() {
        return back_count;
    }

    public void setBack_count(int back_count) {
        this.back_count = back_count;
    }

    public int getRefund_count() {
        return refund_count;
    }

    public void setRefund_count(int refund_count) {
        this.refund_count = refund_count;
    }

    public int getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(int miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    public List<NeedBean> getNeed() {
        return need;
    }

    public void setNeed(List<NeedBean> need) {
        this.need = need;
    }

    public List<ActualBean> getActual() {
        return actual;
    }

    public void setActual(List<ActualBean> actual) {
        this.actual = actual;
    }

    public List<BackBean> getBack() {
        return back;
    }

    public void setBack(List<BackBean> back) {
        this.back = back;
    }

    public List<RefundBean> getRefund() {
        return refund;
    }

    public void setRefund(List<RefundBean> refund) {
        this.refund = refund;
    }

    public static class NeedBean {
        /**
         * gid : 91132136
         * gname : 二号商品
         * num : 3
         */

        private int gid;
        private String gname;
        private int num;

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
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

    public static class ActualBean implements Serializable{
        /**
         * gid : 91132136
         * gname : 二号商品
         * num : 3
         */

        private int gid;
        private String gname;
        private int num;

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
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

    public static class BackBean {
        /**
         * bname : 三号商品
         * bid : 78133110
         * num : 1
         */

        private String bname;
        private String bid;
        private int num;

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    public static class RefundBean {
        /**
         * gname : 三号商品
         * gid : 78133110
         * num : 1
         */

        private String gname;
        private String gid;
        private int num;

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}

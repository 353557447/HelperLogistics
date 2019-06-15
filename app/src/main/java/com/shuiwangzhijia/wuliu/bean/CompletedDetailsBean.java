package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/12/4.
 */

public class CompletedDetailsBean implements Serializable {

    /**
     * uname : 1号司机
     * out_order : 2019022516587382
     * total : 71
     * sum : 71
     * tsum : 10
     * hsum : 77
     * zsum : 0
     * cname : 一号人员
     * update_time : 1551095360
     * status : 3
     * total_goods : [{"gid":98000723,"gname":"一号商品112","num":16},{"gid":91132136,"gname":"二号商品1","num":25},{"gid":78133110,"gname":"三号商品13","num":30}]
     * sum_goods : [{"gid":98000723,"gname":"一号商品112","snum":16},{"gid":91132136,"gname":"二号商品1","snum":25},{"gid":78133110,"gname":"三号商品13","snum":30}]
     * hsum_goods : [{"bid":"78133110","bname":"三号商品13","num":30},{"bid":"91132136","bname":"二号商品1","num":25},{"bid":"98000723","bname":"一号商品112","num":22}]
     * tsum_goods : [{"gid":"98000723","gname":"一号商品112","num":10}]
     */

    private String uname;
    private String out_order;
    private int total;
    private int sum;
    private int tsum;
    private int hsum;
    private int zsum;
    private String cname;
    private int update_time;

    public int getPobucket() {
        return pobucket;
    }

    public void setPobucket(int pobucket) {
        this.pobucket = pobucket;
    }

    private int pobucket;

    public int getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(int miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    private int miscellaneous;

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    private int create_time;
    private int status;
    private List<TotalGoodsBean> total_goods;
    private List<SumGoodsBean> sum_goods;
    private List<TotalGoodsBean> hsum_goods;
    private List<TotalGoodsBean> tsum_goods;

    public List<TotalGoodsBean> getSelf() {
        return zsum_goods;
    }

    public void setSelf(List<TotalGoodsBean> self) {
        this.zsum_goods = self;
    }

    private List<TotalGoodsBean> zsum_goods;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getTsum() {
        return tsum;
    }

    public void setTsum(int tsum) {
        this.tsum = tsum;
    }

    public int getHsum() {
        return hsum;
    }

    public void setHsum(int hsum) {
        this.hsum = hsum;
    }

    public int getZsum() {
        return zsum;
    }

    public void setZsum(int zsum) {
        this.zsum = zsum;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TotalGoodsBean> getTotal_goods() {
        return total_goods;
    }

    public void setTotal_goods(List<TotalGoodsBean> total_goods) {
        this.total_goods = total_goods;
    }

    public List<SumGoodsBean> getSum_goods() {
        return sum_goods;
    }

    public void setSum_goods(List<SumGoodsBean> sum_goods) {
        this.sum_goods = sum_goods;
    }

    public List<TotalGoodsBean> getHsum_goods() {
        return hsum_goods;
    }

    public void setHsum_goods(List<TotalGoodsBean> hsum_goods) {
        this.hsum_goods = hsum_goods;
    }

    public List<TotalGoodsBean> getTsum_goods() {
        return tsum_goods;
    }

    public void setTsum_goods(List<TotalGoodsBean> tsum_goods) {
        this.tsum_goods = tsum_goods;
    }

    public static class TotalGoodsBean {
        /**
         * gid : 98000723
         * gname : 一号商品112
         * num : 16
         */

        private int gid;
        private String gname;
        private int num;
        /**
         * bid : 91132136
         * bname : 二号商品1
         */

        private String bid;
        private String bname;

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

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }
    }

    public static class SumGoodsBean {
        /**
         * gid : 98000723
         * gname : 一号商品112
         * snum : 16
         */

        private int gid;
        private String gname;
        private int snum;

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

        public int getSnum() {
            return snum;
        }

        public void setSnum(int snum) {
            this.snum = snum;
        }
    }

    public static class HsumGoodsBean {
        /**
         * bid : 78133110
         * bname : 三号商品13
         * num : 30
         */

        private String bid;
        private String bname;
        private int num;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
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

    public static class TsumGoodsBean {
        /**
         * gid : 98000723
         * gname : 一号商品112
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

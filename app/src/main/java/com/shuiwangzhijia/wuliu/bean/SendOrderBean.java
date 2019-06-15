package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/12/5.
 */

public class SendOrderBean implements Serializable {

    /**
     * order_sn : 2018090339724230
     * sname : hahah水店
     * snum : 4
     * addr : 11 22 33
     * goods : [{"gid":"54491144","gname":"滴答山泉 桶装饮用水 16.8L/桶","snum":3},{"gid":"71486323","gname":"试测","snum":1}]
     */

    private String order_sn;
    private String sname;
    private int snum;
    private String addr;
    private int flag;
    private List<GoodsBean> goods;

    public int getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(int is_selected) {
        this.is_selected = is_selected;
    }

    private int is_selected;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Serializable{
        /**
         * gid : 54491144
         * gname : 滴答山泉 桶装饮用水 16.8L/桶
         * snum : 3
         */

        private String gid;
        private String gname;
        private int snum;

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

        public int getSnum() {
            return snum;
        }

        public void setSnum(int snum) {
            this.snum = snum;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "gid='" + gid + '\'' +
                    ", gname='" + gname + '\'' +
                    ", snum=" + snum +
                    '}';
        }
    }
}

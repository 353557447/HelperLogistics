package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/12/6.
 */

public class EditOutOrderBean implements Serializable {

    /**
     * order_sn : 2018092825992469
     * sname : hahah水店
     * snum : 10
     * addr : 是多少 18512345678 广东省广州市越秀区府前路1号 xzxzxzx
     * flag : 1
     * goods : [{"gid":"26333554","gname":"测试7","snum":10}]
     */

    private String order_sn;
    private String sname;
    private int snum;
    private String addr;
    private int flag;
    private List<GoodsBean> goods;

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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * gid : 26333554
         * gname : 测试7
         * snum : 10
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
    }
}

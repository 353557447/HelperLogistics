package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/28.
 */

public class StatisticalInfoBean implements Serializable {

    /**
     * count : 10
     * goods : [{"gname":"福能达空气水 瓶装水 550ml*24箱装","gid":27891416,"snum":"5"},{"gname":"滴答桶装水(测试) ","gid":31351117,"snum":"5"}]
     */

    private int count;
    private List<GoodsBean> goods;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * gname : 福能达空气水 瓶装水 550ml*24箱装
         * gid : 27891416
         * snum : 5
         */

        private String gname;
        private int gid;
        private String snum;

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getSnum() {
            return snum;
        }

        public void setSnum(String snum) {
            this.snum = snum;
        }
    }
}

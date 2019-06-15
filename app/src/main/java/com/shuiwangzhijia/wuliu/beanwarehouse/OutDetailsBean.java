package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */

public class OutDetailsBean implements Serializable {

    /**
     * uname : 3号司机
     * out_order : 2019011056582192
     * sum : 2
     * need : {"count":2,"goods":[{"gid":37961365,"gname":"1号商品","num":"1"},{"gid":44547640,"gname":"2号商品","num":"1"}]}
     * actual : {"count":2,"goods":[{"gid":37961365,"gname":"1号商品","num":1},{"gid":44547640,"gname":"2号商品","num":1}]}
     */

    private String uname;
    private String out_order;
    private int sum;
    private NeedBean need;
    private ActualBean actual;

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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public NeedBean getNeed() {
        return need;
    }

    public void setNeed(NeedBean need) {
        this.need = need;
    }

    public ActualBean getActual() {
        return actual;
    }

    public void setActual(ActualBean actual) {
        this.actual = actual;
    }

    public static class NeedBean {
        /**
         * count : 2
         * goods : [{"gid":37961365,"gname":"1号商品","num":"1"},{"gid":44547640,"gname":"2号商品","num":"1"}]
         */

        private int count;
        private List<OrderDetailBean.NeedBean> goods;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<OrderDetailBean.NeedBean> getGoods() {
            return goods;
        }

        public void setGoods(List<OrderDetailBean.NeedBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * gid : 37961365
             * gname : 1号商品
             * num : 1
             */

            private int gid;
            private String gname;
            private String num;

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

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }

    public static class ActualBean {
        /**
         * count : 2
         * goods : [{"gid":37961365,"gname":"1号商品","num":1},{"gid":44547640,"gname":"2号商品","num":1}]
         */

        private int count;
        private List<OrderDetailBean.ActualBean> goods;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<OrderDetailBean.ActualBean> getGoods() {
            return goods;
        }

        public void setGoods(List<OrderDetailBean.ActualBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBeanX {
            /**
             * gid : 37961365
             * gname : 1号商品
             * num : 1
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
    }
}

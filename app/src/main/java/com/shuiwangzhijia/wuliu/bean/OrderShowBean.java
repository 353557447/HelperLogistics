package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/12/7.
 */

public class OrderShowBean implements Serializable{

    /**
     * pay_style : 0
     * sname : hahah水店
     * address : 12
     * least_num : 0
     * order_sn : 2018090369788869
     * tprice : 216.01
     * goods : [{"gid":"54491144","gname":"滴答山泉 桶装饮用水 16.8L/桶","picturl":"20180610/a77307610bccab1698bec1b31f677e1d.jpg","snum":3},{"gid":"71486323","gname":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测","picturl":"20180822/449eaf5bba78d5cae5aa7a81f52b9f5b.jpg","snum":1}]
     * recycler : [{"bid":33333,"bname":"啊啊啊 空桶","b_picturl":"20180822/449eaf5bba78d5cae5aa7a81f52b9f5b.jpg","num":3}]
     * refund_water : [{"gid":"54491144","gname":"滴答山泉 桶装饮用水 16.8L/桶","picturl":"20180610/a77307610bccab1698bec1b31f677e1d.jpg","snum":3},{"gid":"71486323","gname":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测","picturl":"20180822/449eaf5bba78d5cae5aa7a81f52b9f5b.jpg","snum":1}]
     * change_bucket : false
     */

    private int pay_style;
    private String sname;
    private String address;
    private int least_num;
    private String order_sn;
    private String tprice;

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    private int snum;

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    private long create_time;
//    private boolean change_bucket;
    private List<GoodsBean> goods;
    private List<RecyclerBean> recycler;
    private List<GoodsBean> refund_water;
    private ChangeBucketBean change_bucket;


    public ChangeBucketBean getChange_bucket() {
        return change_bucket;
    }

    public void setChange_bucket(ChangeBucketBean change_bucket) {
        this.change_bucket = change_bucket;
    }

    public static class ChangeBucketBean {

        /**
         * order_sn : 2018112373975243
         * mix_num : 3
         * sum : 4
         * change_way : 0
         * total_price : 0.01
         * pay_status : 1
         * wpnum : 0
         * goods : [{"gid":65201504,"gname":"财富","snum":4}]
         */

        private String order_sn;
        private int mix_num;

        public int getBrokenBucketNum() {
            return brokenBucketNum;
        }

        public void setBrokenBucketNum(int brokenBucketNum) {
            this.brokenBucketNum = brokenBucketNum;
        }

        private int brokenBucketNum;
        private int sum;
        private int change_way;
        private String total_price;
        private int pay_status;
        private int wpnum;
        private List<OrderShowBean.RecyclerBean> goods;

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
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

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public int getWpnum() {
            return wpnum;
        }

        public void setWpnum(int wpnum) {
            this.wpnum = wpnum;
        }

        public List<OrderShowBean.RecyclerBean> getGoods() {
            return goods;
        }

        public void setGoods(List<OrderShowBean.RecyclerBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * gid : 65201504
             * gname : 财富
             * snum : 4
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
    }

    public int getPay_style() {
        return pay_style;
    }

    public void setPay_style(int pay_style) {
        this.pay_style = pay_style;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLeast_num() {
        return least_num;
    }

    public void setLeast_num(int least_num) {
        this.least_num = least_num;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getTprice() {
        return tprice;
    }

    public void setTprice(String tprice) {
        this.tprice = tprice;
    }

//    public boolean isChange_bucket() {
//        return change_bucket;
//    }

//    public void setChange_bucket(boolean change_bucket) {
//        this.change_bucket = change_bucket;
//    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<RecyclerBean> getRecycler() {
        return recycler;
    }

    public void setRecycler(List<RecyclerBean> recycler) {
        this.recycler = recycler;
    }

    public List<GoodsBean> getRefund_water() {
        return refund_water;
    }

    public void setRefund_water(List<GoodsBean> refund_water) {
        this.refund_water = refund_water;
    }

    public static class GoodsBean implements Serializable{
        /**
         * gid : 54491144
         * gname : 滴答山泉 桶装饮用水 16.8L/桶
         * picturl : 20180610/a77307610bccab1698bec1b31f677e1d.jpg
         * snum : 3
         */
        private int bid;
        private String bname;
        private String b_picturl;
        private String empty_price;
        private boolean check;//选择状态
        private int count;//标记数量

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

        public String getB_picturl() {
            return b_picturl;
        }

        public void setB_picturl(String b_picturl) {
            this.b_picturl = b_picturl;
        }

        public String getEmpty_price() {
            return empty_price;
        }

        public void setEmpty_price(String empty_price) {
            this.empty_price = empty_price;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        private String gid;
        private String gname;
        private String picturl;
        private int snum;   //标记数量
        private int payStyle;
        private int num;//订单管理-内部item数据
        private double price;
        public double getPrice() {
            return price;
        }

        public int getR_water() {
            return r_water;
        }

        public void setR_water(int r_water) {
            this.r_water = r_water;
        }

        public int getW_water() {
            return w_water;
        }

        public void setW_water(int w_water) {
            this.w_water = w_water;
        }

        private int r_water;
        private int w_water;

        public void setPrice(double price) {
            this.price = price;
        }

        public int getNum() {
            if (num == 0){
                num = snum;
                return snum;
            }else {
                return num;
            }
//            return num == 0 ? snum : num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getPayStyle() {
            return payStyle;
        }

        public void setPayStyle(int payStyle) {
            this.payStyle = payStyle;
        }

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

        public String getPicturl() {
            return picturl;
        }

        public void setPicturl(String picturl) {
            this.picturl = picturl;
        }

        public int getSnum() {
            return snum;
        }

        public void setSnum(int snum) {
            this.snum = snum;
        }
    }

    public static class RecyclerBean implements Serializable{
        /**
         * bid : 33333
         * bname : 啊啊啊 空桶
         * b_picturl : 20180822/449eaf5bba78d5cae5aa7a81f52b9f5b.jpg
         * num : 3
         */

        private int bid;
        private String bname;
        private String b_picturl;
        private int num;
        private boolean check;//选择状态
        private int count;//标记数量
        private String picturl;

        public int getSnum() {
            return snum;
        }

        public void setSnum(int snum) {
            this.snum = snum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        private int snum;
        private String price;

        public String getPicturl() {
            return picturl;
        }

        public void setPicturl(String picturl) {
            this.picturl = picturl;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        /**
         * gid : 53137621
         * gname : 健康
         * empty_price : 0
         */
        private String gid;
        private String gname;
        private String empty_price;

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }


        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

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

        public String getB_picturl() {
            return b_picturl;
        }

        public void setB_picturl(String b_picturl) {
            this.b_picturl = b_picturl;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getEmpty_price() {
            return empty_price;
        }

        public void setEmpty_price(String empty_price) {
            this.empty_price = empty_price;
        }
    }

//    public static class RefundWaterBean implements Serializable{
//        /**
//         * gid : 54491144
//         * gname : 滴答山泉 桶装饮用水 16.8L/桶
//         * picturl : 20180610/a77307610bccab1698bec1b31f677e1d.jpg
//         * snum : 3
//         */
//
//        private String gid;
//        private String gname;
//        private String picturl;
//        private int snum;
//        private String price;
//
//        public String getPrice() {
//            return price;
//        }
//
//        public void setPrice(String price) {
//            this.price = price;
//        }
//
//        public String getGid() {
//            return gid;
//        }
//
//        public void setGid(String gid) {
//            this.gid = gid;
//        }
//
//        public String getGname() {
//            return gname;
//        }
//
//        public void setGname(String gname) {
//            this.gname = gname;
//        }
//
//        public String getPicturl() {
//            return picturl;
//        }
//
//        public void setPicturl(String picturl) {
//            this.picturl = picturl;
//        }
//
//        public int getSnum() {
//            return snum;
//        }
//
//        public void setSnum(int snum) {
//            this.snum = snum;
//        }
//    }
}

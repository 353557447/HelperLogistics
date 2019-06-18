package com.shuiwangzhijia.wuliu.bean;

import java.util.List;

public class DSearchResultBean {

    /**
     * code : 200
     * msg : 查询成功
     * data : [{"order_sn":"2019022816586563","pay_statuses":2,"create_time":1551351119,"id":819,"sname":"南水店","phone":"13344004400","remark":"","address":"岳阳市","tprice":"450.00","snum":30,"tnum":30,"isout":2,"otype":1,"goods":[{"gname":"健康","picturl":"20180926/d0fbeac261b595bc7b487854e6edf65b.jpg","gid":"93902350","price":"15.00","num":30,"update_time":1551351326}],"remarks":"暂无备注"},{"out_order":"2019022816586555","driver":10,"remark":"暂无备注","status":3,"is_deal":0,"create_time":1551349256,"snum":60,"order_sn":"2019022816586554,2019022816586557","otype":2,"goods":[{"gname":"高兴","picturl":"20181108/e2511f688d732b12831ef8d0e531aba2.png","gid":"60470503","price":"14.00","num":15,"update_time":1551349466},{"gname":"财富（仅供测试）","picturl":"20180926/37ece42262262281dfed1cec8dd5333e.jpg","gid":"65201504","price":"16.00","num":30,"update_time":1551349466},{"gname":"平安","picturl":"20180926/170b6be2ac504ae655d1d0b04c9459f1.jpg","gid":"54754261","price":"1.00","num":10,"update_time":1551349480}]}]
     * scode : 0
     */

    private int code;
    private String msg;
    private int scode;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getScode() {
        return scode;
    }

    public void setScode(int scode) {
        this.scode = scode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * order_sn : 2019022816586563
         * pay_statuses : 2
         * create_time : 1551351119
         * id : 819
         * sname : 南水店
         * phone : 13344004400
         * remark :
         * address : 岳阳市
         * tprice : 450.00
         * snum : 30
         * tnum : 30
         * isout : 2
         * otype : 1
         * goods : [{"gname":"健康","picturl":"20180926/d0fbeac261b595bc7b487854e6edf65b.jpg","gid":"93902350","price":"15.00","num":30,"update_time":1551351326}]
         * remarks : 暂无备注
         * out_order : 2019022816586555
         * driver : 10
         * status : 3
         * is_deal : 0
         */

        private String order_sn;
        private int pay_statuses;
        private int create_time;
        private int id;
        private String sname;
        private String phone;
        private String remark;
        private String address;
        private String tprice;
        private int snum;
        private int tnum;
        private int isout;
        private int otype;
        private String remarks;
        private String out_order;
        private int driver;
        private int status;
        private int is_deal;
        private List<GoodsBean> goods;

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public int getPay_statuses() {
            return pay_statuses;
        }

        public void setPay_statuses(int pay_statuses) {
            this.pay_statuses = pay_statuses;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTprice() {
            return tprice;
        }

        public void setTprice(String tprice) {
            this.tprice = tprice;
        }

        public int getSnum() {
            return snum;
        }

        public void setSnum(int snum) {
            this.snum = snum;
        }

        public int getTnum() {
            return tnum;
        }

        public void setTnum(int tnum) {
            this.tnum = tnum;
        }

        public int getIsout() {
            return isout;
        }

        public void setIsout(int isout) {
            this.isout = isout;
        }

        public int getOtype() {
            return otype;
        }

        public void setOtype(int otype) {
            this.otype = otype;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getOut_order() {
            return out_order;
        }

        public void setOut_order(String out_order) {
            this.out_order = out_order;
        }

        public int getDriver() {
            return driver;
        }

        public void setDriver(int driver) {
            this.driver = driver;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_deal() {
            return is_deal;
        }

        public void setIs_deal(int is_deal) {
            this.is_deal = is_deal;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * gname : 健康
             * picturl : 20180926/d0fbeac261b595bc7b487854e6edf65b.jpg
             * gid : 93902350
             * price : 15.00
             * num : 30
             * update_time : 1551351326
             */

            private String gname;
            private String picturl;
            private String gid;
            private String price;
            private int num;
            private int update_time;

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

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }
        }
    }
}

package com.shuiwangzhijia.wuliu.beanwarehouse;


import java.util.List;

/**
 * created by wangsuli on 2018/9/17.
 */
public class PayOrderDetailBean {
    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public int getPay_style() {
        return pay_style;
    }

    public void setPay_style(int pay_style) {
        this.pay_style = pay_style;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * order_no : 2018091749273886
     * amount : 81.00
     * sum : 9
     * pay_status : 0
     * pay_style : 0
     * address : 啦啦啦啦
     * name : 15502066603
     * phone : 张三
     * list : [{"did":118,"sname":"产品","phone":"13912121212","order_sn":"2018091763196630","goods":[{"gname":"测试1","picturl":"20180914/dae1ddb02682043f51b271f3d9993a47.jpg","price":"9.00","snum":9}]}]
     */

    private String order_no;
    private String amount;
    private int sum;
    private int pay_status;
    private int pay_style;

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    private long create_time;
    private String address;
    private String name;
    private String phone;

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * did : 118
         * sname : 产品
         * phone : 13912121212
         * order_sn : 2018091763196630
         * goods : [{"gname":"测试1","picturl":"20180914/dae1ddb02682043f51b271f3d9993a47.jpg","price":"9.00","snum":9}]
         */

        private int did;
        private String sname;
        private String phone;
        private String order_sn;
        private List<WarehouseGoodsBean> goods;

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
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
            this.phone= phone;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public List<WarehouseGoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<WarehouseGoodsBean> goods) {
            this.goods = goods;
        }


    }
}

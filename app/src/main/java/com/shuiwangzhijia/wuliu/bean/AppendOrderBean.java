package com.shuiwangzhijia.wuliu.bean;

import java.util.List;

/**
 * created by wangsuli on 2018/9/18.
 */
public class AppendOrderBean {

    /**
     * order_sns : 2018091865666377
     * update_time : 1537250823
     * sname : 产品1
     * phone : 13923232323
     * address : 深南中路2072
     * goods : [{"gname":"测试1","picturl":"20180914/dae1ddb02682043f51b271f3d9993a47.jpg","price":"9.00","num":9}]
     * total_price : 81.00
     * sum : 9
     */

    private String order_sns;
    private long update_time;
    private String sname;
    private String phone;
    private String address;
    private String total_price;
    private String sum;
    private List<GoodsBean> goods;

    public String getOrder_sns() {
        return order_sns;
    }

    public void setOrder_sns(String order_sns) {
        this.order_sns = order_sns;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }


}

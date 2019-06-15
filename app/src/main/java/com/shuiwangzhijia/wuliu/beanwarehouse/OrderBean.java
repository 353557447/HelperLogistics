package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * created by wangsuli on 2018/8/24.
 */
public class OrderBean implements Serializable {


    /**
     * order_sn : 2018090787632746
     * time : 1536312709
     * sname : 测试水厂123
     * phone : 17687654321
     * remarks : 暂无备注
     * address : 燕南路9号1005
     * tprice : 82.00
     * tnum : 0
     * goods : [{"gname":"测试999","picturl":"20180906/10dbe17138be7292df2163734a4fa3db.jpg","gid":"72273236","price":"9.00","num":9},{"gname":"测试666","picturl":"20180906/7353374d1c8d26abc44a6730c58756cb.jpg","gid":"57134176","price":"1.00","num":1}]
     */


    private String order_sn;
    private long time;
    private String sname;
    private String phone;
    private String remarks;
    private String address;
    private String tprice;
    /**
     * order_sns : 2018091020591908
     * sprice : 162.00
     * time : 1536576935
     */

    private String order_sns;
    private String sprice;

    public int getLeast_num() {
        return least_num;
    }

    public void setLeast_num(int least_num) {
        this.least_num = least_num;
    }

    /**
     * pay_style : 1
     */

    private int least_num;//最低购买量
    private int pay_style;

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getSnum() {
        return snum;
    }

    public void setSnum(String snum) {
        this.snum = snum;
    }

    private String total_price;
    private String snum;
    private int id;

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    private long create_time;
    private int tnum;
    private List<WarehouseGoodsBean> goods;

    public List<BucketBean> getRecycler() {
        return recycler;
    }

    public void setRecycler(List<BucketBean> recycler) {
        this.recycler = recycler;
    }

    private List<BucketBean> recycler;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public int getTnum() {
        return tnum;
    }

    public void setTnum(int tnum) {
        this.tnum = tnum;
    }

    public List<WarehouseGoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<WarehouseGoodsBean> goods) {
        this.goods = goods;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_sns() {
        return order_sns;
    }

    public void setOrder_sns(String order_sns) {
        this.order_sns = order_sns;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public int getPay_style() {
        return pay_style;
    }

    public void setPay_style(int pay_style) {
        this.pay_style = pay_style;
    }

}

package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2018/11/28.
 */

public class WarehouseOutOrderBean implements Serializable {

    /**
     * uname : 测试司机1  //提货人
     * phone : 15555555555   //司机电话
     * total : 4        // 提货数量
     * sum : 4      //
     * out_order : 1234567893       //出货单号
     * status : 3       // 出货操作  1出货中  2 回仓确认
     * tsum : 3     //  退水数量
     * hsum : 0     //  回桶数量
     * zsum : 0     //  杂桶数量
     * create_time : 1542954535     //创建时间
     * sname : 独孤九败     // 仓库负责人
     * remark       // 备注
     */

    private String uname;
    private String phone;
    private int total;
    private int sum;
    private String out_order;
    private int status;
    private int tsum;
    private int hsum;
    private int zsum;
    private int create_time;
    private String sname;

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    private int order_type;
    public List<OrderDetailBean.ActualBean> getGoods() {
        return goods;
    }

    public void setGoods(List<OrderDetailBean.ActualBean> goods) {
        this.goods = goods;
    }

    private List<OrderDetailBean.ActualBean> goods;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getOut_order() {
        return out_order;
    }

    public void setOut_order(String out_order) {
        this.out_order = out_order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
}

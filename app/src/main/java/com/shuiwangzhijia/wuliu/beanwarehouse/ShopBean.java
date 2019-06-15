package com.shuiwangzhijia.wuliu.beanwarehouse;

/**
 * created by wangsuli on 2018/8/22.
 */
public class ShopBean {

    /**
     * id : 92
     * sname : 测试水店121212
     * address : 深南中路2044号
     * distance : 1
     */

    private int id;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    private String bname;
    private String sname;
    private String address;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num;
    private double distance;
    /**
     * num : 3
     * gname : 测试666
     */

    private String gname;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }
}

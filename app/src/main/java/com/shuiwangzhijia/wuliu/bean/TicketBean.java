package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * created by wangsuli on 2018/9/26.
 */
public class TicketBean implements Serializable {

    /**
     * sname : 深圳市天源水文章饮品有限公司
     * s_name : 水票3
     * gname : 测试7
     * gid : 26333554
     * s_gid : 12347
     * snum : 10
     * pprice : 35.00
     * sprice : 31.00
     * start : 1537939665
     * end : 1538326861
     */

    private String sname;
    private String s_name;
    private String gname;
    private String gid;
    private String s_gid;
    private int snum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public int getIs_up() {
        return is_up;
    }

    public void setIs_up(int is_up) {
        this.is_up = is_up;
    }

    private int is_up;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    private boolean check;//最终选中状态

    public int getUse() {
        return use;
    }

    public void setUse(int use) {
        this.use = use;
    }

    public int getCuse() {
        return cuse;
    }

    public void setCuse(int cuse) {
        this.cuse = cuse;
    }

    private int cuse;//use==1 亮色==0暗色
    private int use;//use==1 是选中状态==0是没有选中
    private double pprice;
    private String sprice;
    private long start;
    private long end;
    /**
     * sum : 10
     * total : 10
     * start : null
     * end : null
     */

    private int sum;
    private String total;

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    private String total_num;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getS_gid() {
        return s_gid;
    }

    public void setS_gid(String s_gid) {
        this.s_gid = s_gid;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public double getPprice() {
        return pprice;
    }

    public void setPprice(double pprice) {
        this.pprice = pprice;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

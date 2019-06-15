package com.shuiwangzhijia.wuliu.bean;


import java.io.Serializable;

/**
 * 商品实体类
 * created by wangsuli on 2018/8/22.
 */
public class GoodsBean implements Serializable {

    /**
     * id : 9
     * gid : 15262627891416
     * did : 46
     * gname : 福能达空气水 瓶装水 550ml*24箱装
     * gsum : 9999737
     * is_up : 1
     * g_number : null
     * barcode : null
     * gunit : 箱
     * pprice : 24.00
     * least_p : 100
     * gprice : 48.00
     * least_g : 1
     * descrip : 福能达空气水 瓶装水 550ml*24箱装
     * g_water : 34
     * picturl : 20180514/d6c499edc7d9e431b3d81286fd39879d.jpg
     * update_time : 1528281422
     * pid : 46
     * sid : 33
     * sgrade : 3
     * sname : 深圳市天源水文章饮品有限公司
     * amount : 100.00
     * full_free : 200.00
     * is_free : 0
     * price : 18.00
     */
    private static final long serialVersionUID = 7981560250804078637l;
    private int id;

    public int getAnum() {
        return anum;
    }

    public void setAnum(int anum) {
        this.anum = anum;
    }

    private int anum;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    private String order_sn;

    public int getDistribution_status() {
        return distribution_status;
    }

    public void setDistribution_status(int distribution_status) {
        this.distribution_status = distribution_status;
    }

    private int distribution_status;

    public int getNum() {
        return num == 0 ? count : num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    private int snum;
    private int num;//订单管理-内部item数据
    private String gid;
    private int did;
    private String gname;
    private int gsum;
    private int is_up;
    private Object g_number;
    private Object barcode;
    private String gunit;
    private String pprice;
    private int least_p;
    private String gprice;
    private int least_g;
    private String descrip;
    private String g_water;
    private String picturl;
    private String update_time;
    private int pid;
    private int sid;
    private int sgrade;
    private String sname;
    private String amount;
    private String full_free;
    private int is_free;
    private String price;

    private boolean check;//选择状态
    private int count;//标记数量
    /**
     * gtype : 7
     * g_number : null
     * barcode : null
     * is_delete : 0
     * sh_status : 2
     * refusal : null
     * update_time : null
     * delete_time : null
     * create_time : null
     */

    private int gtype;
    private int is_delete;
    private int sh_status;
    private Object refusal;
    private String delete_time;
    private String create_time;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    private String detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getGsum() {
        return gsum;
    }

    public void setGsum(int gsum) {
        this.gsum = gsum;
    }

    public int getIs_up() {
        return is_up;
    }

    public void setIs_up(int is_up) {
        this.is_up = is_up;
    }

    public Object getG_number() {
        return g_number;
    }

    public void setG_number(Object g_number) {
        this.g_number = g_number;
    }

    public Object getBarcode() {
        return barcode;
    }

    public void setBarcode(Object barcode) {
        this.barcode = barcode;
    }

    public String getGunit() {
        return gunit;
    }

    public void setGunit(String gunit) {
        this.gunit = gunit;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public int getLeast_p() {
        return least_p;
    }

    public void setLeast_p(int least_p) {
        this.least_p = least_p;
    }

    public String getGprice() {
        return gprice;
    }

    public void setGprice(String gprice) {
        this.gprice = gprice;
    }

    public int getLeast_g() {
        return least_g;
    }

    public void setLeast_g(int least_g) {
        this.least_g = least_g;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getG_water() {
        return g_water;
    }

    public void setG_water(String g_water) {
        this.g_water = g_water;
    }

    public String getPicturl() {
        return picturl;
    }

    public void setPicturl(String picturl) {
        this.picturl = picturl;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSgrade() {
        return sgrade;
    }

    public void setSgrade(int sgrade) {
        this.sgrade = sgrade;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFull_free() {
        return full_free;
    }

    public void setFull_free(String full_free) {
        this.full_free = full_free;
    }

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getGtype() {
        return gtype;
    }

    public void setGtype(int gtype) {
        this.gtype = gtype;
    }


    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getSh_status() {
        return sh_status;
    }

    public void setSh_status(int sh_status) {
        this.sh_status = sh_status;
    }

    public Object getRefusal() {
        return refusal;
    }

    public void setRefusal(Object refusal) {
        this.refusal = refusal;
    }


    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

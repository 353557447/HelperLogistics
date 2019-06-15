package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2018/12/11.
 */

public class ShowGoodsListBean implements Serializable{

    /**
     * id : 9
     * gid : 27891416
     * gname : 福能达空气水 瓶装水 550ml*24箱装
     * picturl : 20180514/d6c499edc7d9e431b3d81286fd39879d.jpg
     * least_p : 100
     * did : 46
     * price : 0.01
     */

    private int id;
    private String gid;
    private String gname;
    private String picturl;
    private int least_p;
    private int did;
    private String price;

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

    public int getLeast_p() {
        return least_p;
    }

    public void setLeast_p(int least_p) {
        this.least_p = least_p;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

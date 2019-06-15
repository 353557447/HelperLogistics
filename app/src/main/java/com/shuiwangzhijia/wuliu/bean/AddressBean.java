package com.shuiwangzhijia.wuliu.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by wangsuli on 2018/8/22.
 */
public class AddressBean implements Serializable {
    /**
     * id : 36
     * aname : 张磊
     * sphone : 15118033235
     * province : 广东省
     * city : 深圳市
     * dist : 福田区
     * daddress : 12348384674848484948499499回到酒店酒店就放假就放假放假开发看看回复疾风
     * ulnglat : 114.089685,22.541444
     * user_id : 33
     * default : 0
     * type : 1
     */

    private int id;
    private String aname;
    private String sphone;
    private String province;
    private String city;
    private String dist;
    private String daddress;
    private String ulnglat;
    private int user_id;

    public int getDefaultInt() {
        return defaultInt;
    }

    public void setDefaultInt(int defaultStr) {
        this.defaultInt = defaultStr;
    }

    @SerializedName("default")
    private int defaultInt;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getDaddress() {
        return daddress;
    }

    public void setDaddress(String daddress) {
        this.daddress = daddress;
    }

    public String getUlnglat() {
        return ulnglat;
    }

    public void setUlnglat(String ulnglat) {
        this.ulnglat = ulnglat;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

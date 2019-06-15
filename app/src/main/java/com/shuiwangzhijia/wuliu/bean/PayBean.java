package com.shuiwangzhijia.wuliu.bean;


public class PayBean {

    /**
     * appid : wx5866be27239dcc47
     * noncestr : v0mhioby5wiha1fdtiavyczlsn4q08zd
     * package : Sign=WXPay
     * partnerid : 1539358061
     * prepayid : wx13224507538010e201caf1711670265300
     * timestamp : 1560437107
     * sign : 5F51FDF1B4A584D088DA55E4242B4B6A
     */

    private String appid;
    private String noncestr;
    private String packageX;
    private String partnerid;
    private String prepayid;
    private int timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

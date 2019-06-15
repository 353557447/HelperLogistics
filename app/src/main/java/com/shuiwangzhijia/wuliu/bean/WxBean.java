package com.shuiwangzhijia.wuliu.bean;

/**
 * created by wangsuli on 2018/8/24.
 */
public class WxBean {
    /**
     * appId : wxc69ee81b4b07a290
     * partnerId : 1512979091
     * prepayId : wx241639234949524f95bb52250585423281
     * nonceStr : ef51401bf19ecb21f483e96d1ca02e60
     * timeStamp : 1535099963
     * packageValue : Sign=WXPay
     * sign : 11819415FF27EBB490235917ED019616
     */

    private String appId;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private int timeStamp;
    private String packageValue;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

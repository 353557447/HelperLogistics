package com.shuiwangzhijia.wuliu.bean;

/**
 * created by wangsuli on 2018/8/24.
 */
public class AlipayBean {
    /**
     * orderInfo : app_id=2018082161131225&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA2×tamp=2018-08-24%2017%3A18%3A00&version=1.0&biz_content=%7B%22body%22%3A%22%E6%B0%B4%E6%BB%B4%E5%BF%AB%E9%80%92%E5%95%86%E5%93%81%E6%94%AF%E4%BB%98%22%2C%22subject%22%3A%22%E6%B0%B4%E6%BB%B4%E5%BF%AB%E9%80%92%E5%95%86%E5%93%81%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%222018082458863961%22%2C%22total_amount%22%3A0.01%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%221440m%22%7D¬ify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_Ka5WPO9uH8SGKKurn1aTGqPS&sign=urNN1Mn4QgqGjL08plTsksXSWL3o%2Fv2%2BkVk2HgoKMd%2F46Z79A%2FYblzYU%2FDsk94kbhoP4jKglfLcnU4bd6g5SA4rF3xdIUSp2wd3v%2FI3GbkFjTeEZOUcz8abYY4AYkvyEVPwotpu7tWWCLFqOlZ%2B5LgIhg1ZyiphOZHWnBU%2Bn96x5Y8TVsTpR%2F%2BMcEGHMvP%2B5oMTRVwn6sM7JTudqJ6U2jA8VU19kOnXIEzIBM%2B3IblK1WEKuuJX9jVTKP4wBFxD%2Bht2cgnh8%2FW7ShFUjzpxXwXE4E8Mn27rkAsmb7B8ug44jFsdfuxMabr9ItyujUoLoD1BYiwCclSAVL9ym9VtDeA%3D%3D
     */

    private String orderInfo;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}

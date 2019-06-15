package com.shuiwangzhijia.wuliu.beanwarehouse;

import java.io.Serializable;

/**
 * created by wangsuli on 2018/10/24.
 */
public class BucketBean implements Serializable {

    /**
     * bid : 44444
     * bname : 啊啊啊 空桶
     * num : 2
     */

    private String bid;
    private String bname;
    private int num;

    private boolean check;//选择状态
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

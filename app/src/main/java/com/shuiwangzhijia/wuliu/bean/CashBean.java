package com.shuiwangzhijia.wuliu.bean;

/**
 * created by wangsuli on 2018/8/27.
 */
public class CashBean {
    /**
     * id : 10
     * banlance : 99998200.99
     * amount : 800//
     * remark : 我在这里
     * account : 张磊
     * bank : 招商银行
     * card_no : 6225768600623498
     * status : 1
     * refuse : null
     */

    private int id;
    private String banlance;
    private String amount;
    private String remark;
    private String account;
    private String bank;
    private String card_no;
    private int status;//0未提交 1已提交 2 已通过 3 驳回
    private String refuse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanlance() {
        return banlance;
    }

    public void setBanlance(String banlance) {
        this.banlance = banlance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }
}

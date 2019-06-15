package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by xxc on 2019/3/8.
 */

public class DefaultStoreBean implements Serializable{

    /**
     * id : 1
     * cname : 1号仓
     */

    private int id;
    private String cname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}

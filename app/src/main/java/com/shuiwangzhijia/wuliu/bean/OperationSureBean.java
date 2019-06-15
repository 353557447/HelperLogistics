package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/26.
 */

public class OperationSureBean implements Serializable {

    /**
     * order_sn : 2018122656581346
     * create_time : 1545820624
     */

    private String order_sn;
    private int create_time;
    private int jump_type;

    public int getJump_type() {
        return jump_type;
    }

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}

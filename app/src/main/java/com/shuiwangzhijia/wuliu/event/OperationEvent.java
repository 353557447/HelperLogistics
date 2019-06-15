package com.shuiwangzhijia.wuliu.event;

import com.shuiwangzhijia.wuliu.bean.OrderShowBean;

import java.util.List;

/**
 * Created by xxc on 2018/12/10.
 */

public class OperationEvent {
    private final List<OrderShowBean.GoodsBean> peisongData;


    public OperationEvent(List<OrderShowBean.GoodsBean> data) {
        this.peisongData = data;
    }


    public List<OrderShowBean.GoodsBean> getPeisongData() {
        return peisongData;
    }



}

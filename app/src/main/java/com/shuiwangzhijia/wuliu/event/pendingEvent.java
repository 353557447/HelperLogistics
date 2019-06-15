package com.shuiwangzhijia.wuliu.event;

import com.shuiwangzhijia.wuliu.bean.SendOrderBean;

import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */

public class pendingEvent {

    public final List<SendOrderBean> mData;

    public pendingEvent(List<SendOrderBean> buyData) {
        this.mData = buyData;
    }

    public List<SendOrderBean> getData(){
        return mData;
    }
}

package com.shuiwangzhijia.wuliu.event;

import com.shuiwangzhijia.wuliu.bean.OrderShowBean;

import java.util.List;

/**
 * Created by Administrator on 2018/12/12.
 */

public class HuitongEvent {
    public List<OrderShowBean.RecyclerBean> getData() {
        return mData;
    }

    private final List<OrderShowBean.RecyclerBean> mData;

    public HuitongEvent(List<OrderShowBean.RecyclerBean> mHuitongAdapterData) {
        this.mData = mHuitongAdapterData;
    }
}

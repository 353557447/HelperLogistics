package com.shuiwangzhijia.wuliu.eventwarehouse;


import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;

import java.util.ArrayList;

/**
 * Created by xxc on 2019/1/11.
 */

public class HuitongEvent {
    public ArrayList<WarehouseGoodsBean> getData() {
        return mData;
    }

    private final ArrayList<WarehouseGoodsBean> mData;

    public HuitongEvent(ArrayList<WarehouseGoodsBean> mHuitongAdapterData) {
        this.mData = mHuitongAdapterData;
    }
}

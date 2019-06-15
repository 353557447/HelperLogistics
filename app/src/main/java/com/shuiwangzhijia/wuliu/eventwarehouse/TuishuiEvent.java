package com.shuiwangzhijia.wuliu.eventwarehouse;


import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/12/12.
 */

public class TuishuiEvent {
    public ArrayList<WarehouseGoodsBean> getData() {
        return mData;
    }

    private final ArrayList<WarehouseGoodsBean> mData;

    public TuishuiEvent(ArrayList<WarehouseGoodsBean> mTuishuiAdapterData) {
        this.mData = mTuishuiAdapterData;
    }
}

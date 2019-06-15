package com.shuiwangzhijia.wuliu.event;


import com.shuiwangzhijia.wuliu.bean.AddressBean;

/**
 * created by wangsuli on 2018/8/28.
 */
public class AddressEvent {
    public AddressEvent(AddressBean data,boolean isDelete) {
        this.data = data;
        this.isDelete = isDelete;
    }

    public AddressBean data;
    public boolean isDelete;
}

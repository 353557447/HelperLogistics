package com.shuiwangzhijia.wuliu.bean;

import java.util.List;

/**
 * created by wangsuli on 2018/8/23.
 */
public class GoodsManageBean {
    /**
     * gtype : 7
     * gname : 瓶装水
     */

    private int gtype;
    private String gname;

    public int getGtype() {
        return gtype;
    }

    public void setGtype(int gtype) {
        this.gtype = gtype;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    private List<GoodsBean>  list;

    public List<GoodsBean> getList() {
        return list;
    }

    public void setList(List<GoodsBean> list) {
        this.list = list;
    }
}

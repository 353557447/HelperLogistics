package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxc on 2019/1/16.
 */

public class TuiTongBean implements Serializable {

    /**
     * list : [{"sid":3,"sname":"华强北水店","sum":"1"}]
     * total : 1
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * sid : 3
         * sname : 华强北水店
         * sum : 1
         */

        private int sid;
        private String sname;
        private String sum;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        private int num;

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }
    }
}

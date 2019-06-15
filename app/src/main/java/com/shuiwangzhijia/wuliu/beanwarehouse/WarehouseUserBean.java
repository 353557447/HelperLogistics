package com.shuiwangzhijia.wuliu.beanwarehouse;

/**
 * created by wangsuli on 2018/9/10.
 */
public class WarehouseUserBean {


    /**
     * personalInfo : {"uname":"独孤九败","phone":"13333333333"}
     * outOrderInfo : {"un_today":"5","today":"5","pre_day":"10"}
     */

    private PersonalInfoBean personalInfo;
    private OutOrderInfoBean outOrderInfo;

    public PersonalInfoBean getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfoBean personalInfo) {
        this.personalInfo = personalInfo;
    }

    public OutOrderInfoBean getOutOrderInfo() {
        return outOrderInfo;
    }

    public void setOutOrderInfo(OutOrderInfoBean outOrderInfo) {
        this.outOrderInfo = outOrderInfo;
    }

    public static class PersonalInfoBean {
        /**
         * uname : 独孤九败
         * phone : 13333333333
         */

        private String uname;
        private String phone;

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class OutOrderInfoBean {
        /**
         * un_today : 5
         * today : 5
         * pre_day : 10
         */

        private String un_today;
        private String today;
        private String pre_day;

        public String getUn_today() {
            return un_today;
        }

        public void setUn_today(String un_today) {
            this.un_today = un_today;
        }

        public String getToday() {
            return today;
        }

        public void setToday(String today) {
            this.today = today;
        }

        public String getPre_day() {
            return pre_day;
        }

        public void setPre_day(String pre_day) {
            this.pre_day = pre_day;
        }
    }
}

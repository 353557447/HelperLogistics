package com.shuiwangzhijia.wuliu.bean;

import java.util.List;

/**
 * created by wangsuli on 2018/8/23.
 */
public class CommentBean {
    /**
     * user_id : 459
     * score : 5
     * comment : 好文章
     * update_time : 1529673828
     * name : 水若善
     * phone : 13902900181
     * header_pic : https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eor1jaQgJia4P8V061DIpPgeVDpTpMyzcJQjcbHkllHs1AsUIZkEGKwxngKMmRxGnenjMgnKwiaswEg/132
     */

    private int user_id;
    private int score;
    private String comment;
    private int update_time;
    private String name;
    private String phone;
    private String header_pic;
    private List<String> list;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}

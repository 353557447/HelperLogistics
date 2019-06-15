package com.shuiwangzhijia.wuliu.beanwarehouse;

/**
 * created by wangsuli on 2018/8/29.
 */
public class UpdateInfo {
    /**
     * version : v1.0.9
     * v_url : https://...2android.com
     * content : 123123
     * update_time : 1534754156
     */

    private String version;
    private String v_url;
    private String content;
    private long update_time;
    private int download_type;

    public int getDownload_type() {
        return download_type;
    }

    public void setDownload_type(int download_type) {
        this.download_type = download_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}

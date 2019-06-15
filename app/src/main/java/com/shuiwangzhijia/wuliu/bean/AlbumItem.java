package com.shuiwangzhijia.wuliu.bean;

import java.io.Serializable;

/**
 * created by wangsuli on 2018/9/3.
 */
public class AlbumItem implements Serializable {
    private long time;
    private String imageId;
    private String thumbnail;
    private String filePath;
    private String uploadUrl;//上传的地址


    @Override
    public String toString() {
        return "AlbumItem{" +
                "time=" + time +
                ", imageId='" + imageId + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", filePath='" + filePath + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                '}';
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}

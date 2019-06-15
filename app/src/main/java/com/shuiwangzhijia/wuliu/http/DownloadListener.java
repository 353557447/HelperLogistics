package com.shuiwangzhijia.wuliu.http;

import java.io.File;

/**
 * created by wangsuli on 2018/8/29.
 */
public interface DownloadListener {
    void onStart();

    void onProgress(int currentLength);

    void onFinish(File localPath);

    void onFailure();
}

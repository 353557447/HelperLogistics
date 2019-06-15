package com.shuiwangzhijia.wuliu.base;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 * Created by wangsuli on 2017/5/23.
 */

public class Constant {
//        public static final String url = "http://192.168.1.5:8010/";
//    public static final String url = "https://apiwaters.zhidian668.com/";
//        public static final String url = "https://testapi.fndwater.com/";
  //  public static final String url = "https://api.fndwater.com/";
    public static final String url="https://api.waterhelper.cn/";

    public static final String GOODS_IMAGE_URL = url + "goods/";
    public static final String SHOP_IMAGE_URL = url + "wtshop/";

    //图片裁剪
    public static final int IMAGE_CROP = 1001;
    //默认图片压缩质量
    public static final int IMAGE_QUALITY = 40;
    public static final String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String BASE = absolutePath + File.separator + "waterDriver/";
    // 更新版本缓存
    public static final String UPDATE_FILE_DIR = BASE + "updateFile/";
    // 图片缓存
    public static final String CACHE_DIR_IMAGE = BASE + "image/";

    static {
        File f1 = new File(UPDATE_FILE_DIR);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        File f2 = new File(CACHE_DIR_IMAGE);
        if (!f2.exists()) {
            f2.mkdirs();
        }
    }
    //登录返回
    public static final String WATER_FACTORY_ID = "jcy666888";
    public static final String WATER_FACTORY_NAME = "九重岩水厂";
    public static final String WXAPPID="wx5866be27239dcc47";
}
